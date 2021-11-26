package ComunicaTabla;

import javax.swing.JFrame;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;

import com.jgoodies.forms.layout.FormSpecs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JList;

/**
 * Permite mediante una ventana visualizar los documentos asociados al usuario en la base de datos, a demás de acceder a su
 * edición y visualización.
 * @author pedro
 * @version 1.0
 * @see JFrame
 */

public class MenuUsuario extends JFrame{

	private JButton btnCerrar;
	private JButton btnCambiar;
	
	private JButton btnArchivosLocales;
	private JButton btnArchivosCompartidos;
	private JButton btnArchivosTodos;
	
	private JTable table;
	private DefaultTableModel modelo;
	private JScrollPane scrollPane;
	private JButton btnBorrar;
	private JButton btnAbrir;
	private JButton btnNuevo;
	
	/**
	 * Construye la estructura y diseño inicial del panel.
	 */
	
	public MenuUsuario() {
		
		setSize(600,400);
		setLocation(300,300);
		
		modelo = new DefaultTableModel();
		modelo.addColumn("Documento");
		modelo.addColumn("Compartido");
		
		setTitle(AccesoBD.getUsuario());
		getContentPane().setLayout(new FormLayout(new ColumnSpec[] {
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,},
			new RowSpec[] {
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),}));
		
		btnCerrar = new JButton("Cerrar programa");
		getContentPane().add(btnCerrar, "2, 2");
		
		btnCambiar = new JButton("Cambiar de usuario");
		getContentPane().add(btnCambiar, "20, 2");
		
		scrollPane = new JScrollPane();
		getContentPane().add(scrollPane, "2, 5, 9, 14, fill, fill");
		
		table = new JTable(modelo);
		scrollPane.setViewportView(table);
		
		table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
		
		table.setBackground(new Color(240,230,230));
		
		table.setRowHeight(30);
		table.validate();
		
		btnArchivosLocales = new JButton("Locales");
		getContentPane().add(btnArchivosLocales, "18, 6");
		
		btnArchivosCompartidos = new JButton("Compartidos");
		getContentPane().add(btnArchivosCompartidos, "18, 8");
		
		btnArchivosTodos = new JButton("Todos");
		getContentPane().add(btnArchivosTodos, "18, 10");
		
		btnBorrar = new JButton("Borrar");
		getContentPane().add(btnBorrar, "18, 12");
		
		btnAbrir = new JButton("Abrir");
		getContentPane().add(btnAbrir, "18, 16");
		
		btnNuevo = new JButton("Nuevo");
		getContentPane().add(btnNuevo, "18, 18");
		
		actionCerrar();
		actionCambiarUsuario();
		rellenarNuevo(AccesoBD.buscarDocsUsuario(AccesoBD.getUsuario()));
		rellenarArchivosLocales();
		rellenarArchivosTodos();
		rellenarArchivosCompartidos();
		actionBorrar();
		nuevoDocumento();
		actionAbrirFichero();
		
		setVisible(true);
		
	}
	
	/**
	 * Implementa el listener a btnAbrir, mediante el registro seleccionado en table abrirá un nuevo Frame NuevoDocumento
	 * @see NuevoDocumento
	 */
	
	private void actionAbrirFichero() {
		btnAbrir.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Boolean compartido=false;
				if(table.getModel().getValueAt(table.getSelectedRow(), 1).toString().equals("si")) {
					compartido=true;
				}
				
				new NuevoDocumento(table.getModel().getValueAt(table.getSelectedRow(), 0).toString(),AccesoBD.getTextoUsuario(table.getModel().getValueAt(table.getSelectedRow(), 0).toString()),compartido);
				
			}
			
		});
	}
	
	/**
	 * Implementa el listener a btnArchivosTodos, el cual muestra todos los registros de la bbdd compartidos y locales en table.
	 */
	
	private void rellenarArchivosTodos() {
		
		btnArchivosTodos.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				rellenarNuevo(AccesoBD.buscarDocsUsuarioActual());
			}
		});
	}
	
	/**
	 * Rellena table mediante los registros recibidos por la base de datos llamándola directamente.
	 * @see AccesoBD
	 */
	
	private void rellenarArchivosLocales() {
		
		btnArchivosLocales.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				rellenarNuevo(AccesoBD.buscarDocUsuarioYNoCompartidos(AccesoBD.getUsuario()));
			}
		});
	}
	
	/**
	 * Implementa el listener a btnArchivos compartidos, que muestra los archivos compartidos de la bbdd.
	 */
	
	private void rellenarArchivosCompartidos() {
		
		btnArchivosCompartidos.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				rellenarNuevo(AccesoBD.buscarDocUsuarioYCompartidos(AccesoBD.getUsuario()));
			}
		});
	}
	
	/**
	 * Rellena table mediante los registros recibidos por la base de datos.
	 * @param rs Registros pasados como parámetro que rellenarán la tabla.
	 */
	
	private void rellenarNuevo(ResultSet rs) {
		
		modelo = new DefaultTableModel();
		modelo.addColumn("Documento");
		modelo.addColumn("Compartido");
		table = new JTable(modelo);
		scrollPane.setViewportView(table);
		
		anyadirFilas(rs);
		
		table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
		
		table.setBackground(new Color(240,230,230));
		
		table.setRowHeight(30);
		table.validate();
		
	}
	
	/**
	 * Añade filas a los registros existentes en table.
	 * @param r Registros a añadir.
	 */
	
	private void anyadirFilas(ResultSet r) {
		
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		try {
			while(r.next()) {
				String vof="no";
				if(r.getBoolean("compartido"))
					vof="si";
				model.addRow(new Object[]{r.getString("nombre"),vof});
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Implementa el listener a btnCerrar, que cerrará el programa por completo.
	 */
	
	private void actionCerrar() {
		btnCerrar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(AccesoBD.cerrarConexion()) {
					System.exit(0);
				}
				
				
			}
			
		});
	}
	
	/**
	 * Implementa el listener de btnBorrar, que elimina un registro de la bbdd mediante el registro seleccionado en table
	 */
	
	private void actionBorrar() {
		btnBorrar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				AccesoBD.borrarFila(table.getModel().getValueAt(table.getSelectedRow(), 0).toString(),AccesoBD.getUsuario());
				rellenarArchivosTodos();
			}
		});
	}
	
	/**
	 * Crea un nuevo documento en blanco.
	 * @see NuevoDocumento
	 */
	
	private void nuevoDocumento() {
		btnNuevo.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				new NuevoDocumento();
				
			}
			
		});
	}
	
	/**
	 * Implementa el listener a btnCambiar, que cierra este Frame y vuelve al menú de inicio de sesión.
	 * @see Marco
	 */
	
	private void actionCambiarUsuario() {
		
		btnCambiar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				dispose();
				AccesoBD.limpiar();
				new Marco();
				
			}
			
		});
		
	}
	
}
