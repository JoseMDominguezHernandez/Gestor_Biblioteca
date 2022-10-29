package es.florida.AE06_GUI;

import static com.mongodb.client.model.Filters.*;

import java.util.ArrayList;
import java.util.Collections;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.json.JSONObject;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

public class Modelo {
	
	static String[] fields = {"Titulo","Autor","Anyo_Nacimiento","Anyo_Publicacion","Editorial","Paginas"};

	/*FUNCTION:	Catalogo()
	 * ACTION:	recorre la coleccion obtiendo la id y el titulo de todos los libros, los junta en 
	 * un String y los incluye en una lista que posteriormente ordenaremos en otra lista que es la
	 * que devolveremos al controlador
	 * INPUT: 'coleccion' obtenida a través de la conexión ala BDD.
	 * OUTPUT: ArrayList de String con todo el catálogo ya ordenado */
	public ArrayList<String> Catalogo() {

		// Conexión a BDD:
		MongoClient mongoClient = new MongoClient("localhost", 27017);
		MongoDatabase database = mongoClient.getDatabase("Biblioteca");
		MongoCollection<Document> coleccion = database.getCollection("Libros");

		quitarBlancos(coleccion);

		String id;
		ArrayList<String> consulta = new ArrayList<>(); // Array con el resultado para almacenar
		ArrayList<String> mostrar = new ArrayList<>(); // Array con el resultado para mostrar ordenado

		MongoCursor<Document> cursor = coleccion.find().iterator();

		while (cursor.hasNext()) {
			JSONObject obj = new JSONObject(cursor.next().toJson());
			// a los números de una cifra le añadimos un cero para ordenarlos correctamente
			// con .sort()
			if (obj.getString("Id").length() == 1)
				id = "0" + obj.getString("Id");
			else
				id = obj.getString("Id");

			consulta.add(id + " - " + obj.getString("Titulo"));
		}
		cursor.close();

		// Presentacion ordenada:
		Collections.sort(consulta);
		int cont = 0;
		for (String obj : consulta) {
			mostrar.add(obj);
			cont++;
		}
		mostrar.add(" \n Total de libros: " + cont);
		mongoClient.close();
		return mostrar;
	}

	
	/*FUNCTION:	detalleLibro()
	 * ACTION:	recibe una id y comprueba si es valida; crea un filtro y recorre la colección
	 * hasta encontrar el documento, entonces obtiene todos los datos de este, los mete en un 
	 * Array de String y lo devuelve al controlador para que lo lea y lo presente.
	 * INPUT:	recibe por parámetro la 'id'
	 * OUTPUT:	Array de Strings con  los parámetros del libro y un 'chivato' en la pos. 0
	 * que nos indica si la id es válida o no para gestionarlo en el controlador. */
	public String[] detalleLibro(String id) {

		MongoClient mongoClient = new MongoClient("localhost", 27017);
		MongoDatabase database = mongoClient.getDatabase("Biblioteca");
		MongoCollection<Document> coleccion = database.getCollection("Libros");
		quitarBlancos(coleccion);

		String[] detalleLibro = new String[7];

		boolean idValida = idExist(coleccion, id);
		if (idValida) {
			Bson query = eq("Id", id);
			MongoCursor<Document> cursor = coleccion.find(query).iterator();

			while (cursor.hasNext()) {
				JSONObject obj = new JSONObject(cursor.next().toJson());
				int cont = 0;
				for (int i = 0; i < detalleLibro.length; i++) {
					if (cont == 0)
						detalleLibro[i] = "1";
					else
						detalleLibro[i] = obj.getString(fields[i - 1]);
					cont++;
				}
			}
		} else
			detalleLibro[0] = "0";
		mongoClient.close();
		return detalleLibro;
	}
	
	
	/*FUNCTION	anyadirLibro()
	 * ACTION:	recibe un objeto documento con los parámetros del libro, busca una id libre, 
	 * se la asigna al documento e introduce este al final de la coleccion.
	 * INPUT: Objeto Document con la información de todos los 'fields' del documento o libro.
	 * OUTPUT: devuelve la id para que desde controlador pueda mostrarse el mensaje la nueva id 
	 * asignada al libro introducido*/
	public String anyadirLibro(Document doc) {

		MongoClient mongoClient = new MongoClient("localhost", 27017);
		MongoDatabase database = mongoClient.getDatabase("Biblioteca");
		MongoCollection<Document> coleccion = database.getCollection("Libros");

		String id = nextId(coleccion);
		doc.append("Id", id);
		coleccion.insertOne(doc);

		mongoClient.close();
		return id;
	}


	/*FUNCTION:	actualizarCampo()
	 * ACTION:	recibe un objeto documento con los parámetros del libro, obtiene su id y crea
	 * un filtro para buscar el documento en la coleccion. Con un bucle el documento. Para facilitar
	 * la tarea, actualiza todo el documento aunque ese campo no haya cambiado.
	 *  INPUT: 	Objeto Document con la información de todos los 'fields' del documento o libro.
	 *  OUTPUT:	nada. Actualiza el documento correspondiente*/
	public void actualizarCampo(Document documento) {

		MongoClient mongoClient = new MongoClient("localhost", 27017);
		MongoDatabase database = mongoClient.getDatabase("Biblioteca");
		MongoCollection<Document> coleccion = database.getCollection("Libros");

		String id = documento.getString("Id");
		Bson query = eq("Id", id);

		for (int i = 0; i < fields.length; i++) {
			coleccion.updateOne(query, new Document("$set", new Document(fields[i], documento.getString(fields[i]))));
		}
		mongoClient.close();
	}
		
	
	/*FUNCTION: borrarLibro()
	 * ACTION: 	recibe una id con la que crea el filtro para buscar el documento y borrarlo;
	 * luego recorre la coleccion en busca de la id y confirmar que se ha borrado correctamente.
	 * INPUT:	id del documento a borrar como parámetro.
	 * OUTPUT:	booleano con la confirmación de si existe vía esa id para gestionar el mensaje 
	 * de salida desde controlador */
	public boolean borrarLibro(String id) {

		MongoClient mongoClient = new MongoClient("localhost", 27017);
		MongoDatabase database = mongoClient.getDatabase("Biblioteca");
		MongoCollection<Document> coleccion = database.getCollection("Libros");

		Bson query = eq("Id", id);
		coleccion.deleteOne(query);

		boolean confirmacion = idExist(coleccion, id);
		mongoClient.close();
		return confirmacion;
	}
	
	
	/*FUNCTION: idExists()
	 * ACTION:	comprueba si un id existe en la coleccion recorriendala toda.
	 * INPUT: 	recibe como parámetro la coleccion y el id a comprobar
	 * OUTPUT:	booleano con true si existe la id y false si no existe */
	public static boolean idExist(MongoCollection<Document> coleccion, String id) {

		MongoCursor<Document> cursor = coleccion.find().iterator();
		boolean Exists = false;
		while (cursor.hasNext()) {
			JSONObject obj = new JSONObject(cursor.next().toJson());
			if (obj.getString("Id").equals(id)) {
				Exists = true;
			}
		}
		return Exists;
	}
	
	
	/*FUNCTION: nextId()
	 * ACTION:	recorre todas las id de la coleccion incrementando y comparando números
	 * hasta que encuentra uno que no existe y lo asigna como siguiente id libre. Permite
	 * reciclar ids que se habían borrado aunque no las coloca ordenadas, los coloca al 
	 * final de la BDD
	 * INPUT:	'coleccion' por parámetro.
	 * OUTPUT:	 String con el número de la siguiente ID libre.*/
	public static String nextId(MongoCollection<Document> coleccion) {

		int nextIdValue = 0;
		String nextId = "1";

		// Comprueba si la siguiente ID ya existe y le suma 1
		boolean flag = false;
		while (!flag) {
			if (idExist(coleccion, nextId) == true) {
				nextIdValue = Integer.valueOf(nextId) + 1;
				nextId = String.valueOf(nextIdValue);
			} else flag = true;
		}
		return nextId;
	}
	
	
	/*FUNCTION: quitarBlancos()
	 * ACTION: 	recorre todos los 'fileds' y sustituye los campos en blanco por 'N.D.' 
	 * INPUT:	'coleccion' por parámetro.
	 * OUTPUT:	Nada. Sustituye campos en blaco dentro de los 'fields' por 'N.D'*/
	public static void quitarBlancos(MongoCollection<Document> coleccion) {

		for (int i = 0; i < fields.length; i++) {
			Bson query = or(eq(fields[i], ""), eq(fields[i], " "));
			coleccion.updateMany(query, new Document("$set", new Document(fields[i], "N.D.")));
		}
	}
}
