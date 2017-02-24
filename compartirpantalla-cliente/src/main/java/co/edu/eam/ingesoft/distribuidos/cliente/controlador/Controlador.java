package co.edu.eam.ingesoft.distribuidos.cliente.controlador;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.Socket;
import java.util.List;
import java.util.Observable;

import co.edu.eam.ingesoft.distribuidos.compartitrpantalla.dto.ListaUsuariosDTO;
import co.edu.eam.ingesoft.distribuidos.compartitrpantalla.dto.LoginDTO;
import co.edu.eam.ingesoft.distribuidos.compartitrpantalla.dto.RegistroDTO;
import co.edu.eam.ingesoft.distribuidos.compartitrpantalla.modelo.Usuario;

/**
 * clase para controlar la comm con el servidor
 * 
 * @author caferrer
 *
 */
public class Controlador extends Observable implements Runnable {

	private Socket con;
	private ObjectOutputStream salida;
	private ObjectInputStream entrada;

	private List<Usuario> usuarios;
	/**
	 * mi usuario
	 */
	private Usuario usuario;

	/**
	 * metodo para loguearse al servidor
	 * 
	 * @param user
	 * @param pass
	 */
	public boolean login(String user, String pass) {

		try {

			con = new Socket("localhost", 45000);
			salida = new ObjectOutputStream(con.getOutputStream());
			entrada = new ObjectInputStream(con.getInputStream());

			LoginDTO login = new LoginDTO();
			login.setPass(pass);
			login.setUsuario(user);
			enviarMsj(login);
			usuario=new Usuario(user, InetAddress.getLocalHost().getHostName());

			Object resp = entrada.readObject();
			if (resp instanceof ListaUsuariosDTO) {
				ListaUsuariosDTO lista = (ListaUsuariosDTO) resp;
				usuarios=lista.getUsuarios();
				setChanged();
				notifyObservers(lista);
				// -----------------corriendo el hhilo para que empiece a
				// recibir mensajes...........................
				new Thread(this).start();
				// --------------------------------------------------------------------------------------------------
				return true;
			}

			if (resp instanceof String) {
				String str = (String) resp;
				if (str.equals("ERROR")) {
					return false;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;

	}

	/**
	 * metodo para loguearse al servidor
	 * 
	 * @param user
	 * @param pass
	 */
	public boolean registrar(String user, String pass) {

		try {

			con = new Socket("localhost", 45000);
			salida = new ObjectOutputStream(con.getOutputStream());
			entrada = new ObjectInputStream(con.getInputStream());

			RegistroDTO dto = new RegistroDTO();
			dto.setPass(pass);
			dto.setUsuario(user);
			enviarMsj(dto);

			Object resp = entrada.readObject();
			
			if (resp instanceof String) {				
				String str = (String) resp;
				if (str.equals("ERROR")) {
					return false;
				}
				if (str.equals("OK")) {
					return true;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;

	}

	public void run() {
		while (true) {

			try {
				System.out.println("esperando mensaje.......................");
				Object obj = entrada.readObject();

				if (obj instanceof ListaUsuariosDTO) {
					System.out.println("lista recibida:");
					ListaUsuariosDTO lista = (ListaUsuariosDTO) obj;
					usuarios = lista.getUsuarios();
					System.out.println(usuario.getUsuario()+"::lista recibida:"+usuarios);
					setChanged();
					notifyObservers(lista);
				}

			} catch (Exception e) {
				e.printStackTrace();
				break;
			}

		}

	}	
	

	/**
	 * metodo para enviar un mensaje a un cliente
	 * 
	 * @param obj
	 * @throws IOException
	 */
	public void enviarMsj(Object obj) throws IOException {
		salida.writeObject(obj);
		salida.flush();
	}

	public List<Usuario> getUsuarios() {
		return usuarios;
	}
	
	public Usuario getUsuario() {
		return usuario;
	}

}
