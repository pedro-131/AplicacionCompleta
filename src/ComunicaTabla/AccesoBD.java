package ComunicaTabla;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

/**
 * 
 * Conexi�n a la base de datos y sus funciones a implementar por el programa.
 * @author pedro
 *
 */

public class AccesoBD {
	
	static Connection conexion;
	private static String userStatic;
	private static String passwdStatic;
	
	/**
	 * Crea una conexi�n mediante jdbc a la base de datos en l�nea, en caso de no funcionar, se comunica un error en la conexi�n.
	 */
	
	public AccesoBD() {
		
		try {
			conexion = DriverManager.getConnection("jdbc:mysql://sql11.freesqldatabase.com/sql11441597","sql11441597","KKy9XLqc7Q");
			System.out.println("Conexi�n correcta.");
		} catch (SQLException e1) {
			
			JOptionPane.showMessageDialog(null, "Fallo en la conexi�n.", "Error de conexi�n", JOptionPane.INFORMATION_MESSAGE);
		}
		
	}
	
	/**
	 * Cierra la conexi�n con la base de datos.
	 * @return Devuelve si se ha cerrado correctamente la conexi�n.
	 */
	
	public static boolean cerrarConexion() {
		Boolean cerrado=false;
		try {
			conexion.close();
			cerrado=conexion.isClosed();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return cerrado;
	}
	
	public static String getUsuario() {
		return userStatic;
	}
	
	/**
	 * Limpia el usuario y contrase�a por defecto en los par�metros.
	 */
	
	public static void limpiar() {
		userStatic="";
		passwdStatic="";
		
	}
	
	/**
	 * Comprueba si un usuario es admin en la bbdd
	 * @deprecated
	 * @param nombre Nombre de usuario
	 * @return Devuelve si es administrador o no.
	 */
	
	public static Boolean esAdmin(String nombre) {
		
		boolean valido=false;
		
		if(existeUsuario(nombre)&&primerValorConsultaNulo("SELECT * FROM usr WHERE user='"+nombre+"' AND admin=true")) 
			valido=true;
		
		return valido;
		
	}
	
	/**
	 * En caso de existir el usuario y coincidir con su contrase�a, se guarda est�ticamente como par�metro para las pr�ximas consultas
	 * @param user Usuario a buscar
	 * @param passwd Contrase�a a comprobar
	 * @return Devuelve si se ha iniciado sesi�n
	 */
	
	public static Boolean inicioSesion(String user, String passwd) {
		Boolean correcto=false;
		
		if(existeUsuario(user)) {
			ResultSet rs=hacerConsulta("SELECT * FROM usr WHERE user='"+user+"';");
			try {
				rs.next();
				if(rs.getString("user").equals(user)&&rs.getString("password").equals(passwd)) {
					correcto=true;
					userStatic=rs.getString("user");
					passwdStatic=rs.getString("password");
					
				}
				else
					correcto=false;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		else
			correcto=false;
			
		System.out.println(correcto);
		
		return correcto;
	}
	
	/**
	 * Busca si el usuario recibido como par�metro existe en la bbdd
	 * @param nombreUsuario Usuario a buscar
	 * @return Devuelve si existe en la bbdd
	 */
	
	public static Boolean existeUsuario(String nombreUsuario) {
			
			if(primerValorConsultaNulo("SELECT * FROM usr WHERE user='"+nombreUsuario+"';"))
				return true;
			else
				return false;
		
	}
	
	public static Boolean primerValorConsultaNulo(String consulta) {
		try {
			if(hacerConsulta(consulta).next()) {
				return true;
			}
			else
				return false;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static ResultSet hacerConsulta(String consulta) {
		ResultSet rs=null;
		try {
			rs=conexion.prepareStatement(consulta).executeQuery();	
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}
	
	public static ResultSet buscarDocsUsuarioActual() {
		
		return hacerConsulta("SELECT * FROM text WHERE user='"+userStatic+"'");
		
	}
	
	public static ResultSet buscarDocsTodosUsuarios() {
		
		return hacerConsulta("SELECT * FROM text;");
		
	}
	
	public static ResultSet buscarDocsUsuario(String user) {
		
		return hacerConsulta("SELECT * FROM text WHERE user='"+user+"';");
		
	}
	
	public static ResultSet buscarDocUsuarioYCompartidos(String user) {
		return hacerConsulta("SELECT * FROM text WHERE user='"+user+"' AND compartido='"+1+"';");
	}
	
	public static ResultSet buscarDocUsuarioYNoCompartidos(String user) {
		return hacerConsulta("SELECT * FROM text WHERE user='"+user+"' AND compartido='"+0+"';");
	}
	
	public static void realizarAccionBD(String sentencia) {
		try {
			conexion.prepareStatement(sentencia).executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String getTextoUsuario(String nombreFichero) {
		ResultSet r=buscarDocsUsuarioActual();
		String texto="";
		try {
			while(r.next()) {
				if(r.getString(3).equals(nombreFichero)) {
					texto=r.getString(1);
				}
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return texto;
	}
	
	public static void guardarFichero(String usuario, String nombre, int compartido, String texto) {
		realizarAccionBD("INSERT INTO text VALUES ('"+usuario+"','"+texto+"','"+compartido+"','"+nombre+"');");
	}
	
	public static Boolean existeFicheroUsuario(String usuario, String nombre) {
		return primerValorConsultaNulo("SELECT * FROM text WHERE user='"+usuario+"' AND nombre='"+nombre+"'");
	}
	
	public static void borrarFila(String nombreFichero,String usuario) {
		realizarAccionBD("DELETE FROM text WHERE user='"+usuario+"' AND nombre='"+nombreFichero+"';");
	}
	
}
