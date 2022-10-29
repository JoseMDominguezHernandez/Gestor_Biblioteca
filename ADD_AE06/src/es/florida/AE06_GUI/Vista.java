package es.florida.AE06_GUI;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.Color;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JTextArea;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class Vista {

	private JFrame frmBiblioteca;
	private JTextField txtField_Autor;
	private JTextField txtField_Titulo;
	private JTextField txtField_Nacimiento;
	private JTextField txtField_Publicacion;
	private JTextField txtField_Editorial;
	private JTextField txtField_IdConsulta;
	private JTextField txtField_Paginas;
	private JButton btnConsultaRef;
	private JButton btnActualizar;
	private JButton btnCatalogo;
	private JButton btnAgregar;
	private JButton btnBorrar;
	private JButton btnLimpiar;
	private JTextArea textArea;

	public Vista() {
		initialize();
	}

	private void initialize() {

		frmBiblioteca = new JFrame();
		frmBiblioteca.setTitle("BIBLIOTECA");
		frmBiblioteca.setBounds(100, 100, 885, 617);
		frmBiblioteca.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmBiblioteca.getContentPane().setLayout(null);

		JPanel panel_consultas = new JPanel();
		panel_consultas.setBorder(new LineBorder(new Color(169, 169, 169), 1, true));
		panel_consultas.setBounds(10, 11, 332, 556);
		frmBiblioteca.getContentPane().add(panel_consultas);
		panel_consultas.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 40, 310, 505);
		panel_consultas.add(scrollPane);

		textArea = new JTextArea();
		textArea.setWrapStyleWord(true);
		textArea.setFont(new Font("Consolas", Font.PLAIN, 14));
		textArea.setBackground(Color.BLACK);
		textArea.setForeground(Color.GREEN);
		scrollPane.setViewportView(textArea);

		JPanel panel_editar = new JPanel();
		panel_editar.setBounds(352, 142, 505, 425);
		frmBiblioteca.getContentPane().add(panel_editar);
		panel_editar.setLayout(null);

		JLabel lblEditarRegistro = new JLabel("B\u00FAsqueda");
		lblEditarRegistro.setBounds(10, 11, 131, 14);
		panel_editar.add(lblEditarRegistro);
		lblEditarRegistro.setHorizontalAlignment(SwingConstants.LEFT);
		lblEditarRegistro.setFont(new Font("Tahoma", Font.BOLD, 12));

		JLabel lblTextoBorrar = new JLabel("Introduzca la id del libro desea consultar, editar o borrar");
		lblTextoBorrar.setBounds(10, 26, 349, 24);
		panel_editar.add(lblTextoBorrar);
		lblTextoBorrar.setHorizontalAlignment(SwingConstants.LEFT);

		btnCatalogo = new JButton("Actualizar");
		btnCatalogo.setBounds(223, 11, 97, 23);
		panel_consultas.add(btnCatalogo);

		JLabel lblConsultarCatlogo = new JLabel("Cat\u00E1logo");
		lblConsultarCatlogo.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblConsultarCatlogo.setHorizontalAlignment(SwingConstants.LEFT);
		lblConsultarCatlogo.setBounds(10, 9, 74, 24);
		panel_consultas.add(lblConsultarCatlogo);

		btnActualizar = new JButton("Actualizar");
		btnActualizar.setBounds(262, 346, 99, 23);
		panel_editar.add(btnActualizar);

		btnAgregar = new JButton("Agregar");
		btnAgregar.setBounds(127, 346, 99, 23);
		panel_editar.add(btnAgregar);

		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(153, 153, 153), 1, true));
		panel.setBounds(10, 271, 483, 64);
		panel_editar.add(panel);
		panel.setLayout(null);

		txtField_Autor = new JTextField();
		txtField_Autor.setBounds(118, 11, 349, 20);
		panel.add(txtField_Autor);
		txtField_Autor.setBackground(new Color(255, 255, 204));
		txtField_Autor.setForeground(Color.BLACK);
		txtField_Autor.setHorizontalAlignment(SwingConstants.LEFT);
		txtField_Autor.setColumns(10);

		JLabel lblAutor = new JLabel("Nombre");
		lblAutor.setBounds(62, 14, 46, 14);
		panel.add(lblAutor);
		lblAutor.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblAutor.setHorizontalAlignment(SwingConstants.RIGHT);

		txtField_Nacimiento = new JTextField();
		txtField_Nacimiento.setBounds(118, 35, 52, 20);
		panel.add(txtField_Nacimiento);
		txtField_Nacimiento.setBackground(new Color(255, 255, 204));
		txtField_Nacimiento.setForeground(Color.BLACK);
		txtField_Nacimiento.setHorizontalAlignment(SwingConstants.LEFT);
		txtField_Nacimiento.setColumns(10);

		JLabel lblFechaNacimiento = new JLabel("\rA\u00F1o nacimiento");
		lblFechaNacimiento.setBounds(0, 38, 108, 14);
		panel.add(lblFechaNacimiento);
		lblFechaNacimiento.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblFechaNacimiento.setToolTipText("");
		lblFechaNacimiento.setHorizontalAlignment(SwingConstants.RIGHT);

		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new LineBorder(new Color(153, 153, 153), 1, true));
		panel_1.setBounds(10, 159, 483, 88);
		panel_editar.add(panel_1);
		panel_1.setLayout(null);

		JLabel lblTitulo = new JLabel("T\u00EDtulo");
		lblTitulo.setBounds(65, 14, 46, 14);
		panel_1.add(lblTitulo);
		lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblTitulo.setHorizontalAlignment(SwingConstants.RIGHT);

		txtField_Titulo = new JTextField();
		txtField_Titulo.setBounds(121, 11, 349, 20);
		panel_1.add(txtField_Titulo);
		txtField_Titulo.setBackground(new Color(255, 255, 204));
		txtField_Titulo.setHorizontalAlignment(SwingConstants.LEFT);
		txtField_Titulo.setForeground(Color.BLACK);
		txtField_Titulo.setColumns(10);

		txtField_Editorial = new JTextField();
		txtField_Editorial.setBounds(121, 35, 349, 20);
		panel_1.add(txtField_Editorial);
		txtField_Editorial.setBackground(new Color(255, 255, 204));
		txtField_Editorial.setHorizontalAlignment(SwingConstants.LEFT);
		txtField_Editorial.setForeground(Color.BLACK);
		txtField_Editorial.setColumns(10);

		JLabel lblEditorial = new JLabel("Editorial");
		lblEditorial.setBounds(65, 38, 46, 14);
		panel_1.add(lblEditorial);
		lblEditorial.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblEditorial.setHorizontalAlignment(SwingConstants.RIGHT);

		txtField_Publicacion = new JTextField();
		txtField_Publicacion.setBounds(121, 59, 52, 20);
		panel_1.add(txtField_Publicacion);
		txtField_Publicacion.setBackground(new Color(255, 255, 204));
		txtField_Publicacion.setForeground(Color.BLACK);
		txtField_Publicacion.setHorizontalAlignment(SwingConstants.LEFT);
		txtField_Publicacion.setColumns(10);

		JLabel lblFechaPublicacion = new JLabel("A\u00F1o");
		lblFechaPublicacion.setBounds(10, 62, 101, 14);
		panel_1.add(lblFechaPublicacion);
		lblFechaPublicacion.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblFechaPublicacion.setHorizontalAlignment(SwingConstants.RIGHT);

		JLabel lblPaginas = new JLabel("P\u00E1ginas");
		lblPaginas.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblPaginas.setBounds(207, 62, 46, 14);
		panel_1.add(lblPaginas);
		lblPaginas.setHorizontalAlignment(SwingConstants.RIGHT);

		txtField_Paginas = new JTextField();
		txtField_Paginas.setBounds(263, 59, 52, 20);
		panel_1.add(txtField_Paginas);
		txtField_Paginas.setBackground(new Color(255, 255, 204));
		txtField_Paginas.setForeground(Color.BLACK);
		txtField_Paginas.setHorizontalAlignment(SwingConstants.LEFT);
		txtField_Paginas.setColumns(10);

		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new LineBorder(new Color(153, 153, 153), 1, true));
		panel_2.setBounds(10, 51, 483, 35);
		panel_editar.add(panel_2);
		panel_2.setLayout(null);

		txtField_IdConsulta = new JTextField();
		txtField_IdConsulta.setBounds(118, 7, 46, 20);
		panel_2.add(txtField_IdConsulta);
		txtField_IdConsulta.setBackground(new Color(255, 255, 204));
		txtField_IdConsulta.setHorizontalAlignment(SwingConstants.RIGHT);
		txtField_IdConsulta.setForeground(Color.BLACK);
		txtField_IdConsulta.setColumns(10);

		JLabel lblId = new JLabel("ID");
		lblId.setBounds(62, 10, 46, 14);
		panel_2.add(lblId);
		lblId.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblId.setHorizontalAlignment(SwingConstants.RIGHT);

		btnConsultaRef = new JButton("Consultar");
		btnConsultaRef.setBounds(372, 6, 101, 23);
		panel_2.add(btnConsultaRef);

		JLabel lblBorrarRegistro = new JLabel("AUTOR");
		lblBorrarRegistro.setBounds(10, 251, 57, 21);
		panel_editar.add(lblBorrarRegistro);
		lblBorrarRegistro.setHorizontalAlignment(SwingConstants.LEFT);
		lblBorrarRegistro.setFont(new Font("Tahoma", Font.BOLD, 11));

		JLabel lblConsultas = new JLabel("Agregar / Actualizar / Borrar");
		lblConsultas.setBounds(10, 115, 231, 14);
		panel_editar.add(lblConsultas);
		lblConsultas.setHorizontalAlignment(SwingConstants.LEFT);
		lblConsultas.setFont(new Font("Tahoma", Font.BOLD, 12));

		btnBorrar = new JButton("Borrar");
		btnBorrar.setBounds(396, 346, 99, 23);
		panel_editar.add(btnBorrar);

		JLabel lblPublicacion = new JLabel("PUBLICACION");
		lblPublicacion.setHorizontalAlignment(SwingConstants.LEFT);
		lblPublicacion.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblPublicacion.setBounds(10, 140, 86, 21);
		panel_editar.add(lblPublicacion);

		btnLimpiar = new JButton("Limpiar");
		btnLimpiar.setBounds(382, 96, 101, 23);
		panel_editar.add(btnLimpiar);

		JLabel lblNewLabel_1 = new JLabel("by Jos\u00E9 Manuel Dom\u00EDnguez Hern\u00E1ndez");
		lblNewLabel_1.setForeground(new Color(169, 169, 169));
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewLabel_1.setBounds(239, 400, 256, 14);
		panel_editar.add(lblNewLabel_1);

		JPanel panel_3 = new JPanel();
		panel_3.setBounds(353, 11, 504, 120);
		frmBiblioteca.getContentPane().add(panel_3);
		panel_3.setLayout(null);

		JLabel lblNewLabel = new JLabel("Bibliotecas Municip\u00E1les");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Consolas", Font.BOLD | Font.ITALIC, 34));
		lblNewLabel.setBounds(10, 11, 484, 74);
		panel_3.add(lblNewLabel);

		JLabel lblGestinDeCatlogo = new JLabel("Gesti\u00F3n de cat\u00E1logo");
		lblGestinDeCatlogo.setHorizontalAlignment(SwingConstants.CENTER);
		lblGestinDeCatlogo.setFont(new Font("Consolas", Font.BOLD | Font.ITALIC, 26));
		lblGestinDeCatlogo.setBounds(10, 77, 484, 32);
		panel_3.add(lblGestinDeCatlogo);

		this.frmBiblioteca.setVisible(true);
	}

	// GETTERS

	public JTextArea getTextArea() {
		return textArea;
	}

	public JTextField getTextIdConsulta() {
		return txtField_IdConsulta;
	}

	public JTextField getTextTitulo() {
		return txtField_Titulo;
	}

	public JTextField getTextAutor() {
		return txtField_Autor;
	}

	public JTextField getTextNacimnineto() {
		return txtField_Nacimiento;
	}

	public JTextField getTextPublicacion() {
		return txtField_Publicacion;
	}

	public JTextField getTextEditorial() {
		return txtField_Editorial;
	}

	public JTextField getTextPaginas() {
		return txtField_Paginas;
	}

	public JButton getBtnConsulta() {
		return btnConsultaRef;
	}

	public JButton getBtnCatalogo() {
		return btnCatalogo;
	}

	public JButton getBtnActualizar() {
		return btnActualizar;
	}

	public JButton getBtnAgregar() {
		return btnAgregar;
	}

	public JButton getBtnBorrar() {
		return btnBorrar;
	}

	public JButton getBtnLimpiar() {
		return btnLimpiar;
	}
}
