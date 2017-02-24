package co.edu.eam.ingesoft.distribuidos.servidor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

import co.edu.eam.ingesoft.distribuidos.compartitrpantalla.dto.ListaUsuariosDTO;
import co.edu.eam.ingesoft.distribuidos.compartitrpantalla.dto.LoginDTO;
import co.edu.eam.ingesoft.distribuidos.compartitrpantalla.dto.RegistroDTO;
import co.edu.eam.ingesoft.distribuidos.compartitrpantalla.dto.SolicitarConexionDTO;
import co.edu.eam.ingesoft.distribuidos.compartitrpantalla.modelo.Usuario;
import co.edu.eam.ingesoft.distribuidos.servidor.logica.Logica;

/**
 * clase para recibir y enviar mensajes al cliente.
 * 
 * @author caferrer
 *
 */
public class HiloProcesarCliente implements Runnable {

	/**
	 * flujo de salida
	 */
	private ObjectOutputStream salida;

	/**
	 * flujo de netrada
	 */
	private ObjectInputStream entrada;

	/**
	 * el usuariuo.
	 */
	private Usuario usuario;

	/**
	 * socket con el cliente
	 */
	private Socket con;

	/**
	 * servidor
	 */
	private Servidor servidor;

	/**
	 * logica
	 */
	private Logica logica;

	/**
	 * constructor
	 * 
	 * @param con
	 */
	public HiloProcesarCliente(Socket con) {
		super();
		this.con = con;
	}

	/**
	 * constructor
	 * 
	 * @param con
	 * @param servidor
	 */
	public HiloProcesarCliente(Socket con, Servidor servidor, Logica logica) {
		super();
		this.con = con;
		this.servidor = servidor;
		this.logica = logica;
	}

	/**
	 * procesa mensajes del cliente.
	 */
	public void run() {
		String ip = con.getInetAddress().getHostAddress();
		System.out.println("conectando usuario.............................desde " + ip);
		// abrir los flujos...
		try {
			salida = new ObjectOutputStream(con.getOutputStream());
			entrada = new ObjectInputStream(con.getInputStream());

			while (true) {

				try {
					System.out.println("esperando mensaje.......................");
					Object obj = entrada.readObject();
					
					if (obj instanceof LoginDTO) {
						System.out.println("LOGIN.......................");

						LoginDTO dto = (LoginDTO) obj;
						// si es valido el usuario

						if (logica.verificarUsuario(dto)) {
							List<Usuario> usuarios = servidor.listarUsuarios();							
							usuario = new Usuario();
							usuario.setUsuario(dto.getUsuario());
							usuario.setIp(ip);
							usuarios.add(usuario);
							enviarMsj(new ListaUsuariosDTO(usuarios));
							servidor.enviarTodos(new ListaUsuariosDTO(usuarios));
							servidor.agregarUsuario(this);
							logica.actualizarUsuario(ip, dto.getUsuario());

						} else {
							enviarMsj("ERROR");
						}
					}

					if (obj instanceof RegistroDTO) {
						System.out.println("registro usuario.......................");

						RegistroDTO dto = (RegistroDTO) obj;
						
						// si es valido el usuario
						if (logica.crearUsuario(dto,ip)) {
							enviarMsj("OK");
							logica.actualizarUsuario(ip, dto.getUsuario());

						} else {
							enviarMsj("ERROR");
						}
					}
					
					if(obj instanceof SolicitarConexionDTO){
						System.out.println("solicito conexion..............");
						SolicitarConexionDTO dot = (SolicitarConexionDTO) obj;
						System.out.println("C"+dot.getIpCliente()+",D"+dot.getIpDestino());
					}

				} catch (Exception e) {
					e.printStackTrace();
					return;
				}

			}
		} catch (IOException e1) {
			e1.printStackTrace();
			return;
		} finally {
			try {
				con.close();
				// eleiminar cliente de la lista del servidor.
				servidor.quitarUsuario(this);
				System.out.println("desconectando usuario " + getUsuario().getUsuario());

			} catch (IOException e) {
				e.printStackTrace();
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
		System.out.println("es un: "+obj);
		salida.writeObject(obj);
		salida.flush();
	}

	/**
	 * usuario
	 * 
	 * @return
	 */
	public Usuario getUsuario() {
		return usuario;
	}

}
