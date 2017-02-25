package co.edu.eam.ingesoft.distribuidos.cliente.hilos;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.imageio.stream.MemoryCacheImageOutputStream;

public class HiloOrigen implements Runnable {

	private ObjectOutputStream salida;
	private Socket soc;

	public HiloOrigen(Socket con) {
		this.soc = con;
	}

	@Override
	public void run() {

		try {

			while (true) {
				//se instancia lo que se va a mandar
				salida = new ObjectOutputStream(soc.getOutputStream());
				//aqui se obtendra el capturar la pantalla
				byte[] arre = getDesktop();
				//se envia los bytes que representan la pantalla
				salida.writeObject(arre);
				//no se
				salida.flush();
			}

		} catch (

		Exception e) {
			// TODO: handle exception
		}

	}

	/**
	 * 
	 * Método que captura un pantallazo. <br>
	 * 
	 * @author Camilo Andres Ferrer Bustos<br>
	 * 
	 * @date 11/08/2016
	 * @version 1.0
	 * @return
	 * @throws IOException
	 *
	 */
	public byte[] getDesktop() throws AWTException, IOException {
		Robot robot = new Robot();
		Rectangle rect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
		BufferedImage image = robot.createScreenCapture(rect);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageOutputStream ios = new MemoryCacheImageOutputStream(baos);
		Iterator<ImageWriter> iter = ImageIO.getImageWritersByFormatName("jpg");
		ImageWriter writer = iter.next();
		ImageWriteParam iwp = writer.getDefaultWriteParam();
		iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
		iwp.setCompressionQuality(0.1f);
		writer.setOutput(ios);
		writer.write(null, new IIOImage(image, null, null), iwp);
		writer.dispose();
		return baos.toByteArray();
	}

}
