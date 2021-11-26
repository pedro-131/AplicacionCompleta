package ComunicaTabla;

import javax.swing.JFrame;
import javax.swing.JEditorPane;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 * La clase nuevo documento abre una nueva ventan en la que podremos introducir información, guardarla y asignar un nombre.
 * @author pedro
 *
 */

public class NuevoDocumento extends JFrame{
	
	private JTextField textField;
	private JEditorPane editorPane;
	private JButton btnGuardar;
	private JRadioButton btnCompartido;
	private JLabel lblNewLabel;
	
	/**
	 * Construye un nuevo documento con los datos en blanco.
	 */
	
	public NuevoDocumento() {
		this("","",false);
	}
	
	/**
	 * Crea un documento con información ya existente.
	 * @param nombre Nombre del documento
	 * @param texto Contenido del documento
	 * @param compartido Compartido al resto de usuarios en la base de datos
	 */
	
	public NuevoDocumento(String nombre, String texto, boolean compartido) {
		setSize(500,600);
		setLocation(400,100);
		setTitle("");
		getContentPane().setLayout(null);
		
		editorPane = new JEditorPane();
		editorPane.setBounds(10, 45, 464, 505);
		getContentPane().add(editorPane);
		
		btnGuardar = new JButton("Guardar");
		btnGuardar.setBounds(385, 11, 89, 23);
		getContentPane().add(btnGuardar);
		
		btnCompartido = new JRadioButton("Compartido");
		btnCompartido.setBounds(283, 11, 89, 23);
		getContentPane().add(btnCompartido);
		
		textField = new JTextField();
		textField.setBounds(66, 12, 196, 20);
		getContentPane().add(textField);
		textField.setColumns(10);
		
		lblNewLabel = new JLabel("Nombre:");
		lblNewLabel.setBounds(10, 11, 46, 14);
		getContentPane().add(lblNewLabel);
		
		listenerGuardar();
		
		setVisible(true);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		cargarFichero(nombre,texto,compartido);
	}
	
	/**
	 * 
	 * @return Devuelve el texto del editorPane
	 */
	
	public String getEditorPane() {
		return editorPane.getText();
	}
	
	/**
	 * 
	 * @return Devuelve si el documento está compartido
	 */
	
	public int getCompartido() {
		int b=0;
		if(btnCompartido.isSelected())
			b=1;
		
		return b;
	}
	
	/**
	 * 
	 * @return Nombre del fichero
	 */
	
	public String getNombreFichero() {
		return textField.getText();
	}
	
	/**
	 * Acción de guardar del btnGuardar.
	 */
	
	public void listenerGuardar() {
		btnGuardar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(!getNombreFichero().equals(""))
					guardar();
				else
					JOptionPane.showMessageDialog(null, "Debes asignar un nombre al fichero!");
				
			}
			
		});
	}
	
	/**
	 * Carga los datos del documento en la interfaz
	 * @param nombre nombre del documento
	 * @param texto Texto del documento
	 * @param compartido Si el documento es compartido
	 */
	
	public void cargarFichero(String nombre, String texto, Boolean compartido) {
		
		this.btnCompartido.setSelected(compartido);
		this.editorPane.setText(texto);
		this.textField.setText(nombre);
		setTitle(nombre);
		
	}
	
	/**
	 * Accede a la base de datos y guarda/sobreescribe los datos del documento. 
	 */
	
	public void guardar() {
		int x=1;
		if(!AccesoBD.existeFicheroUsuario(AccesoBD.getUsuario(), textField.getText())) {
			AccesoBD.guardarFichero(AccesoBD.getUsuario(), getEditorPane(), getCompartido(), getNombreFichero());
			JOptionPane.showMessageDialog(null, "El archivo '"+getNombreFichero()+"' se ha guardado correctamente");
		}
		else {
			x=JOptionPane.showConfirmDialog(null, "Deseas sobreescribir el archivo '"+textField.getText()+"?");
		
			if(x==0) {
				AccesoBD.borrarFila(textField.getText(),AccesoBD.getUsuario());
				AccesoBD.guardarFichero(AccesoBD.getUsuario(), getEditorPane(), getCompartido(), getNombreFichero());
				JOptionPane.showMessageDialog(null, "El archivo '"+getNombreFichero()+"' se ha guardado correctamente");
			}
		}
			
	}
	
}
