package ComunicaTabla;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import javax.swing.JTable;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Marco extends JFrame{
	
	private JTextField campoUsuario;
	private JPasswordField campoContrasenya;
	private JLabel msgCredenciales;
	private JButton botonInicioSesion;
	
	private MenuUsuario ventanaUsuario;

	public Marco() {
		setLocation(500,300);
		setSize(360,280);
		
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		campoUsuario = new JTextField();
		campoUsuario.setBounds(182, 66, 86, 20);
		panel.add(campoUsuario);
		campoUsuario.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("Usuario: ");
		lblNewLabel.setBounds(70, 69, 84, 14);
		panel.add(lblNewLabel);
		
		campoContrasenya = new JPasswordField();
		campoContrasenya.setBounds(182, 110, 86, 20);
		panel.add(campoContrasenya);
		campoContrasenya.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("Contrase\u00F1a: ");
		lblNewLabel_1.setBounds(68, 113, 86, 14);
		panel.add(lblNewLabel_1);
		
		botonInicioSesion = new JButton("Iniciar sesi\u00F3n");
		botonInicioSesion.setBounds(109, 141, 116, 23);
		panel.add(botonInicioSesion);
		
		msgCredenciales = new JLabel("Credenciales incorrectas!");
		msgCredenciales.setForeground(Color.RED);
		msgCredenciales.setBounds(109, 41, 215, 14);
		panel.add(msgCredenciales);
		
		credencialesMsgSwitch(false);
		
		setTitle("Gestión de usuarios y documentos");
		
		listenerCredenciales();
		
		setVisible(true);
		
		setDefaultCloseOperation(this.getDefaultCloseOperation());
	}
	
	public void listenerCredenciales() {
		botonInicioSesion.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(AccesoBD.inicioSesion(campoUsuario.getText(), campoContrasenya.getText())) {
					credencialesMsgSwitch(false);
					ventanaUsuario=new MenuUsuario();
					cerrarVentana();
				}
				else
					credencialesMsgSwitch(true);
				
			}
			
		});
	}
	
	public void credencialesMsgSwitch(Boolean tof) {
		msgCredenciales.setVisible(tof);
	}
	
	public void cerrarVentana() {
		this.dispose();
	}
}
