package co.edu.eam.ingesoft.distribuidos.cliente.hilos;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Observable;

public class EntradaVideo extends Observable implements Runnable{

	private DatagramSocket dts;
	
	
	public void run() {
		try {
			
			dts = new DatagramSocket(27000);
			
			while(true){
				try{
					//recibe el video
					DatagramPacket dtp = new DatagramPacket(new byte[65535],65535);
					dts.receive(dtp);
					setChanged();
					notifyObservers(dtp);
					
				}catch (Exception e) {
					// TODO: handle exception
				}
			}
					
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
