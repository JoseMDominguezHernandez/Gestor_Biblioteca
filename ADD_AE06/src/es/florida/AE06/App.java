package es.florida.AE06;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.json.JSONObject;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.*;

public class App {

	static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	
	/*FUNCTION: main()
	 * ACTION:	Conecta con la BDD, despliega el menú y ejecuta las funciones seleccionadas
	 * por el usuario. Después de conectarse al BDD la recorre y sustituye los campos en 
	 * blanco por 'N.D.' con la función 'quitarBlancos()'
	 * INPUT:	opcion elegida por el usuario
	 * OUTPUT:	diferentes consultas según opción seleccionada por el usuario.*/
	public static void main(String[] args) throws InterruptedException, IOException {

		String[] menu = { 
				" 1. Mostrar catálogo",
				" 2. Detalle libro", 
				" 3. Crear un libro", 
				" 4. Actualizar libro",
				" 5. Borrar libro", 
				" 6. Salir" 
				};
		int opcion = 0;
		boolean flag = false;
		
		// Conexión a BDD:
		MongoClient mongoClient = new MongoClient("localhost", 27017);
		MongoDatabase database = mongoClient.getDatabase("Biblioteca");
		MongoCollection<Document> coleccion = database.getCollection("Libros");

		Thread.sleep(500); // pausa para no intercalar mensaje con menu

		while (flag == false) {
			quitarBlancos(coleccion);
			
			//Menú:
			System.out.println("\n***********************\n    MENU BIBLIOTECA\n***********************");
			for (int i = 0; i < menu.length; i++) {
				System.out.println(menu[i]);
			}
			System.out.println("\n***********************");
			System.out.print(" Indique una opción: ");
			
			try {
				opcion = Integer.parseInt(br.readLine());
			} catch (NumberFormatException e) {
				System.out.println(" Error. Debe introducir un número");
			}
			
			//Opciones:
			if (opcion == 1) mostrarCatalogo(coleccion);
			else if (opcion == 2) mostrarPorId(coleccion);
			else if (opcion == 3) anyadirLibro(coleccion);
			else if (opcion == 4) actualizarCampo(coleccion);
			else if (opcion == 5) borrarLibro(coleccion);
			else if (opcion == 6) flag = true;
			else System.out.println("Ha indicado una opción incorrecta. Teclee una opción valida.\n");
			
			if (opcion < 6) {
				System.out.println("\nPresione 'Enter' para volver al menú");
				String enter = br.readLine();	
			};
		}
		System.out.println("\n Fin de la aplicación");
		br.close();
		mongoClient.close();
	}
	
	
	/*FUNCTION:	borrarLibro()
	 * ACTION:	borra un libro en función de la id que le proporcione el usuario por consola.
	 * Comprueba que la id es válida (existe) y solicita confirmación para borrar. Una vez borrado, 
	 * chequea de nuevo la coleccion para ver si lo encunetra; si no lo encuentra confirma el borrado.
	 * INPUT:	recibe la 'coleccion' como parámetro.
	 * OUTPUT: 	resultado de la opoeración por consola.*/
	public static void borrarLibro(MongoCollection<Document> coleccion) throws IOException {
		
		System.out.println("\n\nBORRAR UN LIBRO\n---------------");

		boolean idValida = false;
		String id = "";
		while (!idValida) {
			System.out.print("Introduzca la Id del libro: ");
			id = br.readLine();
			idValida = idExist(coleccion, id);
			if (!idValida) System.out.println("ID inexistente. Introduzca una ID válida.\n");
		}
		
		if (idValida) {
			detalleLibro(coleccion, id);
			System.out.print("\n¿Desea borrar el libro " + id + "?  (s/n) ");
			String confirm = br.readLine();

			if (confirm.toLowerCase().equals("s")) {
				Bson query = eq("Id", id);
				coleccion.deleteOne(query);

				// Comprueba que se ha borrado buscándolo de nuevo en la colección
				boolean confirmacion = idExist(coleccion, id);
				if (!confirmacion)
					System.out.println("\nEl libro " + id + " se ha borrado correctamente");
				else
					System.out.println("\nEl libro no se ha podido borrar");
			} else {
				System.out.print("\nOperación cancelada.");
			}
		}
	}

	
	/*FUNCTION:	actualizarCampo()
	 * ACTION:	actualiza los campos que deseemos de un libro en concreto obtenido por su 'id'.
	 *  Comprueba que la id exista, presenta el detalle del libro que vamos a modificar para 
	 *  tener una referencia directa y despliega un menú para indicar el campo a modificar. Una 
	 *  vez modificado pregunta si queremos modificar más campos o no.
	 *  INPUT: 'coleccion' por parámetro / id del libro a modificar por consola.
	 *  OUTPUT:	modifica los campos del 'documento' en la BDD.*/
	public static void actualizarCampo(MongoCollection<Document> coleccion) throws IOException {

		String[] fields = { "Título", "Autor", "Año de nacimiento", "Año de publicacion", "Editorial", "Páginas" };
		String campoFiltro = "Id";
		String campo = "";

		boolean exit = false;
		while (!exit) {
			System.out.println("\nACTUALIZAR LIBRO\n----------------");
			System.out.print("Indique la ID del libro al que desea actualizar: ");
			String idFiltro = br.readLine();

			boolean idValida = idExist(coleccion, idFiltro);
			if (idValida) {
				detalleLibro(coleccion, idFiltro);

				boolean flag = false;
				while (!flag) {
					System.out.println(
							"\nQué campo desea actualizar:\n 1. Título\n 2. Autor\n 3. Año nacimineto\n 4. Año publicación\n 5. Editorial\n 6. Nº Páginas");
					System.out.print("Elija una opción: ");
					String opcion = br.readLine();

					if (opcion.equals("1")) campo = "Titulo";
					else if (opcion.equals("2")) campo = "Autor";
					else if (opcion.equals("3")) campo = "Anyo_Nacimiento";
					else if (opcion.equals("4")) campo = "Anyo_Publicacion";
					else if (opcion.equals("5")) campo = "Editorial";
					else if (opcion.equals("6")) campo = "Paginas";

					System.out.print("\nTeclee " + fields[Integer.parseInt(opcion) - 1] + ": ");
					String nuevoValor = br.readLine();

					Bson query = eq(campoFiltro, idFiltro);
					coleccion.updateOne(query, new Document("$set", new Document(campo, nuevoValor)));
					System.out.println(campo + " >>> " + nuevoValor);
					System.out.print("\n¿Desea actualizar algún campo más? (s/n): ");
					String respuesta = br.readLine();
					if (respuesta.toLowerCase().equals("n")) {
						flag = true;
						exit = true;
						System.out.println("\nLibro " + idFiltro + " actualizado.");
					}
				}
			} else {
				System.out.println("ID inexistente. Introduzca una ID válida.\n");
				exit = false;
			}
		}
	}
	
	/*FUNCTION: idExists()
	 * ACTION:	comprueba si un id existe en la coleccion recorriendala toda.
	 * INPUT:	recibe como parámetro la coleccion, el id seleccionado por el usuario.
	 * OUTPUT:	booleano con true si existe la id y false si no existe */
	public static boolean idExist(MongoCollection<Document> coleccion, String id) {
		
		MongoCursor<Document> cursor = coleccion.find().iterator();
		boolean Exists = false;
		while (cursor.hasNext()) {
			JSONObject obj = new JSONObject(cursor.next().toJson());
			if(obj.getString("Id").equals(id)) Exists = true;
		}
		return Exists;
	}

	
	/*FUNCTION	anyadirLibro()
	 * ACTION:	solicita todos los campos para completar un nuevo documento y lo inserta
	 * en la BDD. Si alguno se deja en blanco lo sustituye por 'N.D.' antes de insertarlo
	 * INPUT: 'coleccion' por parámetro y contenido de los campos por consola
	 * OUTPUT: Nada. Inserta documento en la BDD*/
	public static void anyadirLibro(MongoCollection<Document> coleccion) throws IOException {
		
		System.out.println("\n\nAÑADIR UN LIBRO\n---------------\nIntroduzca los datos del nuevo libro:");
		
		System.out.print("Título: "); String titulo = br.readLine(); 
		System.out.print("Autor: "); String autor = br.readLine();	
		System.out.print("Anyo_Nacimiento: "); String nacimiento = br.readLine();	
		System.out.print("Anyo_Publicacion: "); String publicacion = br.readLine();	
		System.out.print("Editorial: "); String editorial = br.readLine(); 
		System.out.print("Paginas: "); String paginas = br.readLine();
		
		String id = nextId(coleccion);
		System.out.println("\nId asignada: " + id);
		
		titulo = (titulo != "") ? titulo : "N.D.";
		autor = (autor != "") ? autor : "N.D.";
		nacimiento = (nacimiento != "") ? nacimiento : "N.D.";
		publicacion = (publicacion != "") ? publicacion : "N.D.";
		editorial = (editorial != "") ? editorial : "N.D.";
		paginas = (paginas != "") ? paginas : "N.D.";
		
		Document doc = new Document();
		doc.append("Id", id);
		doc.append("Titulo", titulo);
		doc.append("Autor", autor);
		doc.append("Anyo_Nacimiento", nacimiento);
		doc.append("Anyo_Publicacion", publicacion);
		doc.append("Editorial", editorial);
		doc.append("Paginas", paginas);
		coleccion.insertOne(doc);
		
//		String arrayTitulos[] ={"a","b","c","c","e"};
//		String arrayAutores[] ={"a","b","c","c","e"};
//		ArrayList<Document> listaDocs = new ArrayList<Document>();
//		for (int i = 0 ; i < arrayTitulos.length; i++) {
//			doc = new Document();
//			doc.append(titulo, arrayTitulos[i]);
//			doc.append(autor, arrayAutores[i]);
//			//...//
//			listaDocs.add(doc);
//		}
//		coleccion.insertMany(listaDocs);
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


	/*FUNCTION:	mostrarCatalogo()
	 * ACTION:	obtiene la id y el titulo de todos los libros, los junta en un String y los 
	 * incluye en una lista que posteriormente ordenaremos y presentaremos por pantalla
	 * INPUT: 'coleccion' por parámetro.
	 * OUTPUT: por consola listado de strings ordenados por id con las id y título de
	 * todos los libros contenidos en la Coleccion.*/
	@SuppressWarnings("deprecation")
	public static void mostrarCatalogo(MongoCollection<Document> coleccion) {

		String campoMostrar1 = "Id";
		String campoMostrar2 = "Titulo";
		String id;
		ArrayList<String> consulta = new ArrayList<>(); // Array con el resultado para mostrar
		
		MongoCursor<Document> cursor = coleccion.find().iterator(); // Puntero

		while (cursor.hasNext()) {
			JSONObject obj = new JSONObject(cursor.next().toJson());
			if (obj.getString(campoMostrar1).length() == 1) id = "0" + obj.getString(campoMostrar1);
			else id = obj.getString(campoMostrar1);
			
			consulta.add(id + " - " + obj.getString(campoMostrar2));
		}
		cursor.close();
		
		// Presentacion ordenada:
		int cont = 0;
		Collections.sort(consulta);
		System.out.println("\n\nCATALOGO:\n---------");

		for (String obj : consulta) {
			System.out.println(obj);
			cont++;
		}
		//System.out.println("\nCantidad total de libros: " + cont);
		System.out.println("\nCantidad total de libros: " + coleccion.count());
	}

	/*FUNCTION:	mostrarPorId();
	 *ACTION:	comprueba que la id iuntroducida por consola es correcta y si lo es muestra
	 * el detelle del libro correspondiente llamando a la función detalleLibro(), esta última 
	 * se ha separado en otra función para reciclar esa parte del código con otras funciones 
	 * que también lo llaman.
	 * INPUT: 	'coleccion' por parámetro e ID por consola.
	 * OUTPUT:	presenta los datos correspondientes al libro o mensaje de Id inexistente en su 
	 * caso*/
	public static void mostrarPorId(MongoCollection<Document> coleccion) throws IOException {

		boolean flag = false;
		System.out.println("\n\nDETALLE LIBRO\n---------------");
		while (!flag) {
			System.out.print("Introduzca la Id del libro: ");
			String id = br.readLine();

			boolean idValida = idExist(coleccion, id);
			if (idValida) {
				detalleLibro(coleccion, id);
				flag = true;
			} else
				System.out.println("ID inexistente. Indique una ID válida.\n");
		}
	}
	
	/*FUNCTIN:	detalleLibro()
	 * ACTION:	crea el filtro y busca los detalles del 'Documento', con ellos monta un String
	 * que presenta por pantalla con todos ordenados.
	 * INPUT:	recibe por parámetro la 'coleccion' y la id introducida por consola
	 * OUTPUT: 	presenta por pantalla un String con los datos del libro correspondiente a la id*/
	public static void detalleLibro(MongoCollection<Document> coleccion, String id) {

		String consulta = "";
		System.out.println("\nDATOS LIBRO CON ID " + id + ":\n----------------------");

		Bson query = eq("Id", id); // Filtro (eq = "igual id")
		MongoCursor<Document> cursor = coleccion.find(query).iterator(); // Creamos puntero. Obtiene la BDD y la mete

		while (cursor.hasNext()) {
			JSONObject obj = new JSONObject(cursor.next().toJson());

			String titulo = obj.getString("Titulo");
			String autor = obj.getString("Autor");
			String nacimiento = obj.getString("Anyo_Nacimiento");
			String publicacion = obj.getString("Anyo_Publicacion");
			String editorial = obj.getString("Editorial");
			String paginas = obj.getString("Paginas");

			consulta = "Título:		" + titulo + "\nAutor:		" + autor + " (" + nacimiento + ")\nA. Publicación:	"
					+ publicacion + "\nEditorial:	" + editorial + "\nPáginas:	" + paginas;
			System.out.println(consulta);
		}
	}
	
	/*FUNCTION: quitarBlancos()
	 * ACTION: recorre todos los 'fileds' y sustituye los campos en blanco por 'N.D.' 
	 * INPUT:	'coleccion' por parámetro.
	 * OUTPUT:	Nada. Sustituye campos en blaco dentro de los 'fields' por 'N.D'*/
	public static void quitarBlancos(MongoCollection<Document> coleccion) {

		String[] fields = {"Titulo","Autor","Anyo_Nacimiento","Anyo_Publicacion","Editorial","Paginas"};
		for(int i = 0; i<fields.length; i++) {
			Bson query = or(eq(fields[i], ""),eq(fields[i], " "));
			coleccion.updateMany(query, new Document("$set", new Document(fields[i], "N.D.")));
		}
	}
}
