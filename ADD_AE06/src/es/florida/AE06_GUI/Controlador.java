package es.florida.AE06_GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.util.ArrayList;
import javax.swing.JOptionPane;

import org.bson.Document;

public class Controlador {

	private Modelo modelo;
	private Vista vista;
	private ActionListener actionListenerCatalogo, actionListenerLibro, actionListenerLimpiar,
			actionListenerEditarLibro, actionListenerNuevoLibro, actionListenerBorrar;

	// Constructor pasamos los parametros desde main en principal
	public Controlador(Modelo modelo, Vista vista) {
		this.modelo = modelo;
		this.vista = vista;
		control();
	}

	/*FUNCTION: control()
	 * ACTION: asigna los ActionListener correspondientes a la interfaz gráfica y ejecuta
	 * los comandos correspondientes para obtener el resultado deseado cuando presionamos
	 * un botón en la interfaz.
	 * INPUT:	diferentes informaciones en función de los comandos que ejecute.
	 * OUTPUT:	diferentes informaciones en función de los comandos que ejecute.*/
	public void control() {

		// Muestra de incio el catálogo completo y desactiva los botones de edición
		mostrarCatalogo();
		vista.getBtnBorrar().setEnabled(false);
		vista.getBtnActualizar().setEnabled(false);

		// Actualiza el listado de la base de datos que se muestra
		actionListenerCatalogo = new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {

				mostrarCatalogo();
			}
		};
		vista.getBtnCatalogo().addActionListener(actionListenerCatalogo);

		// Consulta libro: presenta los datos de la id solicitada en el campo correspondiente
		actionListenerLibro = new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {

				// Activa botones de edidción
				vista.getBtnBorrar().setEnabled(true);
				vista.getBtnActualizar().setEnabled(true);

				String id = vista.getTextIdConsulta().getText();
				String[] libro = (String[]) modelo.detalleLibro(id);
				if (libro[0].equals("1")) {
					vista.getTextTitulo().setText(libro[1]);
					vista.getTextAutor().setText(libro[2]);
					vista.getTextNacimnineto().setText(libro[3]);
					vista.getTextPublicacion().setText(libro[4]);
					vista.getTextEditorial().setText(libro[5]);
					vista.getTextPaginas().setText(libro[6]);
				} else {
					JOptionPane.showMessageDialog(null, "Error. ID inexistente");
				}
			}
		};
		vista.getBtnConsulta().addActionListener(actionListenerLibro);

		// Nuevo libro: crea y guarda en la BDD un nuevo documento, actualiza el catálogo
		actionListenerNuevoLibro = new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {

				Document doc = capturarTexto(0);
				String id = modelo.anyadirLibro(doc);
				JOptionPane.showMessageDialog(null,
						"Libro añadido correctamente.\nNueva referencia:\n\t\"" + id + ". " + doc.getString("Titulo"));
				mostrarCatalogo();
				limpiarCampos();
			}
		};
		vista.getBtnAgregar().addActionListener(actionListenerNuevoLibro);

		// Editar libro: edita cualquier atributo del libro directamente en el
		// JTextField luego sustituye esta info en la BDD
		actionListenerEditarLibro = new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {

				Document doc = capturarTexto(1);
				modelo.actualizarCampo(doc);
				;
				String id = doc.getString("Id");
				JOptionPane.showMessageDialog(null,
						"Libro: " + id + ". " + doc.getString("Titulo") + "\nActualizado correctamente");
				mostrarCatalogo();
				limpiarCampos();
			}
		};
		vista.getBtnActualizar().addActionListener(actionListenerEditarLibro);

		// Borrar libro: borra documento de la BDD indicando la ID
		actionListenerBorrar = new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {

				String id = vista.getTextIdConsulta().getText();
				boolean borrado = modelo.borrarLibro(id);

				if (!borrado)
					JOptionPane.showMessageDialog(null, "El libro con id " + id + " se ha borrado correctamente");
				else
					JOptionPane.showMessageDialog(null, "Libro con id " + id + " no se ha podido borrar");
				limpiarCampos();
				mostrarCatalogo();
			}
		};
		vista.getBtnBorrar().addActionListener(actionListenerBorrar);

		// Limpiar campos: borra todos los campos para dejarlos preparados para escribir
		// y desactiva
		// los botones de edición
		actionListenerLimpiar = new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {

				limpiarCampos();
			}
		};
		vista.getBtnLimpiar().addActionListener(actionListenerLimpiar);
	}
	
	
	/* METODO: 	capturarTexto()
	 * ACTION:	extrae los valores de los JTextField, los convierte a String y crea un 
	 * objeto Document.
	 * INPUT: 	recibe un Integer que indica el constructor a usar; 1 > constructor sin 'id' / 2 > constructor con 'id'
	 * OUTPUT: 	devuelve un objeto Document creado con la información delos JTextField de la GUI*/
	public Document capturarTexto(int n) {

		String id = vista.getTextIdConsulta().getText().toString();
		String titulo = vista.getTextTitulo().getText().toString();
		String autor = vista.getTextAutor().getText().toString();
		String nacimiento = vista.getTextNacimnineto().getText().toString();
		String publicacion = vista.getTextPublicacion().getText().toString();
		String editorial = vista.getTextEditorial().getText().toString();
		String paginas = vista.getTextPaginas().getText().toString();

		Document doc = new Document();
		if (n == 0) {
			doc.append("Titulo", titulo);
			doc.append("Autor", autor);
			doc.append("Anyo_Nacimiento", nacimiento);
			doc.append("Anyo_Publicacion", publicacion);
			doc.append("Editorial", editorial);
			doc.append("Paginas", paginas);
		} else if (n == 1) {
			doc.append("Id", id);
			doc.append("Titulo", titulo);
			doc.append("Autor", autor);
			doc.append("Anyo_Nacimiento", nacimiento);
			doc.append("Anyo_Publicacion", publicacion);
			doc.append("Editorial", editorial);
			doc.append("Paginas", paginas);
		}
		return doc;
	}

	
	/* METODO: 	mostrarCatalogo()
	 * ACTION:	presenta el contenido de la BDD en el TextArea de la GUI
	 * INPUT:	ArrayList de String con el contenido de la BDD desde Modelo.
	 * OUTPUT:	Listado de objetos de la BDD en el TextArea. Fecha y Hora de la última actualización*/
	public void mostrarCatalogo() {

		vista.getTextArea().setText("\n");
		ArrayList<String> listaLibros = modelo.Catalogo();
		for (String libro : listaLibros) {
			vista.getTextArea().append(" " + libro + "\n");
		}
		vista.getTextArea().append("\n Ultima actualización:\n\t\t" + fechaActual());
	}
	

	/* METODO:	fechaActual()
	 * ACTION: 	obtiene la fecha y hora del momneto en que es invocado
	 * INPUT:	nada
	 * OUTPUT:	String formateado con la fecha y hora actuales*/
	public String fechaActual() {

		LocalDateTime localDate = LocalDateTime.now();
		String fecha = localDate.getYear() + "-" + localDate.getMonthValue() + "-" + localDate.getDayOfMonth() + " "
				+ localDate.getHour() + ":" + localDate.getMinute() + ":" + localDate.getSecond();
		return fecha;
	}
	
	
	/* METODO:	limpiarCampos() 
	 * ACTION:	borra el contenido de los JTextField escribiendo en blanco en cada uno de ellos(""). 
	 * El campo de ID sólo lo borra si tiene texto si no, provoca un error. 
	 * INPUT:	nada. 
	 * OUTPUT: 	borra el contenido de los JTextField escribiendo ("") en cada uno. Desactiva los
	 * botones de edición*/
	public void limpiarCampos() {

		if (!vista.getTextIdConsulta().getText().toString().equals(""))
			vista.getTextIdConsulta().setText("");
		vista.getTextTitulo().setText("");
		vista.getTextAutor().setText("");
		vista.getTextNacimnineto().setText("");
		vista.getTextPublicacion().setText("");
		vista.getTextEditorial().setText("");
		vista.getTextPaginas().setText("");

		vista.getBtnBorrar().setEnabled(false);
		vista.getBtnActualizar().setEnabled(false);
	}
}
