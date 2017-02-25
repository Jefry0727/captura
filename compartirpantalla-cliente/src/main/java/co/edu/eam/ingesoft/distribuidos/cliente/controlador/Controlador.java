package co.edu.eam.ingesoft.distribuidos.cliente.controlador;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.Socket;
import java.util.List;
import java.util.Observable;

import javax.swing.JOptionPane;

import co.edu.eam.ingesoft.distribuidos.cliente.gui.Pantalla;
import co.edu.eam.ingesoft.distribuidos.cliente.gui.Ventana;
import co.edu.eam.ingesoft.distribuidos.cliente.hilos.HiloDestino;
import co.edu.eam.ingesoft.distribuidos.cliente.hilos.HiloOrigen;
import co.edu.eam.ingesoft.distribuidos.compartitrpantalla.dto.ListaUsuariosDTO;
import co.edu.eam.ingesoft.distribuidos.compartitrpantalla.dto.LoginDTO;
import co.edu.eam.ingesoft.distribuidos.compartitrpantalla.dto.RegistroDTO;
import co.edu.eam.ingesoft.distribuidos.compartitrpantalla.dto.SolicitarConexionDTO;
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
	private List<Usuario> usuarios;
	private SolicitarConexionDTO dto2;
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
				
				if(obj instanceof SolicitarConexionDTO){
					//hilo destino
					
					
					System.out.println(" ip del alguien"+con.getInetAddress().getHostAddress());
					//pantalla a compartir
					Pantalla p = new Pantalla(hiloDe);
					//casteo el objeto
					SolicitarConexionDTO dto = (SolicitarConexionDTO) obj;
					
					System.out.println(dto.getEstado()+"jjjjjjjjjjjjj");
					
					if(dto.getEstado().equals("PRIMERA")){
						//System.out.println("llego a cli destino: "+dto.getIpDestino().getUsuario());
						int resp = JOptionPane.showConfirmDialog(null, "Desea aceptar conexion con "+dto.getIpCliente().getUsuario());
						
						if(resp == 0){
							dto2 = new SolicitarConexionDTO(dto.getIpCliente(),dto.getIpDestino(),"ACEPTADO");
							enviarMsj(dto2);
							
							System.out.println("despues de enviar mesaja cuando se acepta ");
							
							p.setVisible(true);
							
							con2 = new Socket("localhost", 45001);
							hiloDe = new HiloDestino(con2);
							new Thread(hiloDe).start();
							
							
						}
						System.out.println("que mierda esta llegando aqui: "+dto.getEstado());
						if(dto.getEstado().equals("ACEPTADO")){
						
							System.out.println("SI BER");
							//System.out.println("Esta entrando por aqui: ");
							con2 = new Socket("localhost", 45001);
							hiloDe = new HiloDestino(con);
							//SolicitarConexionDTO dt = (SolicitarConexionDTO) entrada.readObject();
							JOptionPane.showMessageDialog(null,"Solicitud aceptada");
							
							HiloOrigen hiloOr = new HiloOrigen(con2);
							new Thread(hiloOr).start();
						}
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
	
	public void solicitarCompartir(Object o){
		//se castea el objeto que llego por parametro a un dto
		SolicitarConexionDTO dto = (SolicitarConexionDTO) o;
		//setear los datos en el objeto que se va a enviar a HiloProcesarCliente
		SolicitarConexionDTO dto2 = new SolicitarConexionDTO(this.usuario,dto.getIpDestino(),"PRIMERA");
		try {	
			//Se enviar el objeto al Hilo Procesar Cliente
			enviarMsj(dto2);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
