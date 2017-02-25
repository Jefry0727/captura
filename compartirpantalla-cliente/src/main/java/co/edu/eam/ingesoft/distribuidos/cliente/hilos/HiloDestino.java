package co.edu.eam.ingesoft.distribuidos.cliente.hilos;

import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Observable;

public class HiloDestino extends Observable implements Runnable {

	private Socket con;
	private ObjectInputStream entrada;

	public HiloDestino(Socket con) {
		this.con = con;
	}

	@Override
	public void run() {
		try {
			System.out.println("esperando para compartir pantalla.....");
			//se le pone puerto al servidor cliente
			ServerSocket soc = new ServerSocket(45001);
			//el socket se conecta al server
			con = soc.accept();
			//sirve para obtener el objeto
			entrada = new ObjectInputStream(con.getInputStream());
			System.out.println("aqui paso del srver.....");
			

			while (true) {			
				
				//se lee el arreglo de bytes que envio el cliente origen
				byte[] arreglo = (byte[]) entrada.readObject();
				
				System.out.println("arrelooooooo" + arreglo[0]);
				
				try {
					
					System.out.println("" + arreglo[0]);
					setChanged();
					notifyObservers(arreglo);

				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}

			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}
}
