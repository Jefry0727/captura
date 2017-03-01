package co.edu.eam.ingesoft.distribuidos.cliente.hilos;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Observable;

import co.edu.eam.ingesoft.distribuidos.cliente.camara.CamaraUtil;

public class SalidaVideo extends Observable implements Runnable {

	private DatagramSocket dts;

	public SalidaVideo(DatagramSocket sock) {
		this.dts = sock;
	}

	public void run() {

		CamaraUtil camara = new CamaraUtil();
		camara.initCamera();

		while (true) {
			try {

				byte[] arreglo = camara.getFrame();
				DatagramPacket dtp = new DatagramPacket(arreglo, arreglo.length, InetAddress.getByName("localhost"),
						27000);
				dts.send(dtp);

			} catch (Exception e) {

			}
			try {
				Thread.sleep(50);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}

	}

}
