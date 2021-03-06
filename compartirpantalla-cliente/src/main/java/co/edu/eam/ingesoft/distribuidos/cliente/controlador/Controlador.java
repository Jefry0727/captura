package co.edu.eam.ingesoft.distribuidos.cliente.controlador;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.List;
import java.util.Observable;

import javax.swing.JOptionPane;

import co.edu.eam.ingesoft.distribuidos.cliente.gui.Pantalla;
import co.edu.eam.ingesoft.distribuidos.cliente.gui.Video;
import co.edu.eam.ingesoft.distribuidos.cliente.hilos.EntradaVideo;
import co.edu.eam.ingesoft.distribuidos.cliente.hilos.HiloDestino;
import co.edu.eam.ingesoft.distribuidos.cliente.hilos.HiloOrigen;
import co.edu.eam.ingesoft.distribuidos.cliente.hilos.SalidaVideo;
import co.edu.eam.ingesoft.distribuidos.compartitrpantalla.dto.ListaUsuariosDTO;
import co.edu.eam.ingesoft.distribuidos.compartitrpantalla.dto.LoginDTO;
import co.edu.eam.ingesoft.distribuidos.compartitrpantalla.dto.RegistroDTO;
import co.edu.eam.ingesoft.distribuidos.compartitrpantalla.dto.SolicitarConexionDTO;
import co.edu.eam.ingesoft.distribuidos.compartitrpantalla.dto.SolicitarVideoDTO;
import co.edu.eam.ingesoft.distribuidos.compartitrpantalla.modelo.Usuario;

/**
 * clase para controlar la comm con el servidor
 * 
 * @author caferrer
 *
 */
public class Controlador extends Observable implements Runnable {

	private Socket con;
	private Socket con2;
	private ObjectOutputStream salida;
	private ObjectInputStream entrada;
	private HiloDestino hiloDe;
	private SalidaVideo salidaVideo;
	private List<Usuario> usuarios;
	private SolicitarConexionDTO dto2;
	private SolicitarVideoDTO dtoVideo;
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
			usuario = new Usuario(user, InetAddress.getLocalHost().getHostName());

			Object resp = entrada.readObject();
			if (resp instanceof ListaUsuariosDTO) {
				ListaUsuariosDTO lista = (ListaUsuariosDTO) resp;
				usuarios = lista.getUsuarios();
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
					System.out.println(usuario.getUsuario() + "::lista recibida:" + usuarios);
					setChanged();
					notifyObservers(lista);
				}

				if (obj instanceof SolicitarConexionDTO) {
					// hilo destino
					System.out.println(usuario.getUsuario() + "::" + obj);

					System.out.println(" ip del alguien" + con.getInetAddress().getHostAddress());
					// pantalla a compartir
					
					// casteo el objeto
					SolicitarConexionDTO dto = (SolicitarConexionDTO) obj;

					// Se mira si es la primera vez que envia peticion
					if (dto.getEstado().equals("PRIMERA")) {
						// System.out.println("llego a cli destino:
						// "+dto.getIpDestino().getUsuario());
						int resp = JOptionPane.showConfirmDialog(null,
								"Desea aceptar conexion con " + dto.getIpCliente().getUsuario());

						if (resp == 0) {

							hiloDe = new HiloDestino();
							// se corre el hilo destino
							new Thread(hiloDe).start();

							// Se obtienen los datos del objeto que llego y se
							// le castea a Aceptado
							dto2 = new SolicitarConexionDTO(dto.getIpCliente(), dto.getIpDestino(), "ACEPTADO");
							// Se envia al hilo procesar cliente
							enviarMsj(dto2);

							System.out.println("despues de enviar mensaja cuando se acepta ");
							// se muestra la pantalla
							Pantalla p = new Pantalla(hiloDe);
							hiloDe.addObserver(p);
							p.setVisible(true);
							// Se le asigna local host y puerto al socket
							// con2 = new Socket("localhost", 45001);
							// se le envia al hilo destino el socket

						}
					}
					System.out.println("que mierda esta llegando aqui: " + dto.getEstado());
					if (dto.getEstado().equals("ACEPTADO")) {

						System.out.println("SI BER");
						// lo mismo de arriba
						con2 = new Socket("localhost", 45001);

						// SolicitarConexionDTO dt = (SolicitarConexionDTO)
						// entrada.readObject();
						JOptionPane.showMessageDialog(null, "Solicitud aceptada");
						// se le envia el socket al hilo origen
						HiloOrigen hiloOr = new HiloOrigen(con2);
						// se corre el hilo origen
						new Thread(hiloOr).start();
					}

				}
				
				//--------------------------------------------------
				
				if (obj instanceof SolicitarVideoDTO) {
					// hilo destino
					System.out.println(usuario.getUsuario() + "::" + obj);

					System.out.println(" ip del alguien" + con.getInetAddress().getHostAddress());
					// pantalla a compartir
					
					// casteo el objeto
					SolicitarVideoDTO dto = (SolicitarVideoDTO) obj;

					// Se mira si es la primera vez que envia peticion
					if (dto.getEstado().equals("PRIMERA")) {
						// System.out.println("llego a cli destino:
						// "+dto.getIpDestino().getUsuario());
						int resp = JOptionPane.showConfirmDialog(null,
								"Desea aceptar llamada? " + dto.getIpCliente().getUsuario());

						if (resp == 0) {

							EntradaVideo e= new EntradaVideo();
							// se corre el hilo destino
							new Thread(e).start();

							// Se obtienen los datos del objeto que llego y se
							// le castea a Aceptado
							dtoVideo = new SolicitarVideoDTO(dto.getIpCliente(), dto.getIpDestino(), "ACEPTADO");
							// Se envia al hilo procesar cliente
							enviarMsj(dtoVideo);

							System.out.println("despues de enviar mensaja cuando se acepta ");
							// se muestra la pantalla
							Video v = new Video(e);
							e.addObserver(v);
							v.setVisible(true);

						}
					}
					
					if (dto.getEstado().equals("ACEPTADO")) {

						DatagramSocket dts = new DatagramSocket();
						
						salidaVideo = new SalidaVideo(dts);
						
						new Thread(salidaVideo).start();				
					}

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

	public void solicitarCompartir(Object o) {
		// se castea el objeto que llego por parametro a un dto
		SolicitarConexionDTO dto = (SolicitarConexionDTO) o;
		// setear los datos en el objeto que se va a enviar a
		// HiloProcesarCliente
		SolicitarConexionDTO dto2 = new SolicitarConexionDTO(this.usuario, dto.getIpDestino(), "PRIMERA");
		try {
			// Se enviar el objeto al Hilo Procesar Cliente
			enviarMsj(dto2);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	public void solicitarVideo(Object o) {
		// se castea el objeto que llego por parametro a un dto
		SolicitarVideoDTO dto = (SolicitarVideoDTO) o;
		// setear los datos en el objeto que se va a enviar a
		// HiloProcesarCliente
		SolicitarVideoDTO dto2 = new SolicitarVideoDTO(this.usuario, dto.getIpDestino(), "PRIMERA");
		try {
			// Se enviar el objeto al Hilo Procesar Cliente
			enviarMsj(dto2);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
