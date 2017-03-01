package co.edu.eam.ingesoft.distribuidos.cliente.camara;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.github.sarxos.webcam.Webcam;
import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.Iterator;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.imageio.stream.MemoryCacheImageOutputStream;

/**
 * Clase que permite capturar el escritorio o la camara WEB.
 * 
 * @author caferrerb
 */
public class CamaraUtil {

	private Webcam webcam = null;

	/**
	 * 
	 * M�todo que inicializa la camara <br>
	 * 
	 * @author Camilo Andres Ferrer Bustos<br>
	 * 
	 * @date 11/08/2016
	 * @version 1.0
	 *
	 */
	public void initCamera() {
		webcam = Webcam.getDefault();
		webcam.setViewSize(new Dimension(320, 240));
		webcam.open();

	}

	/**
	 * 
	 * M�todo que captura un Frame de la camara. <br>
	 * 
	 * @author Camilo Andres Ferrer Bustos<br>
	 * 
	 * @date 11/08/2016
	 * @version 1.0
	 * @return
	 * @throws IOException
	 *
	 */
	public byte[] getFrame() throws IOException {

		BufferedImage image = webcam.getImage();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ImageIO.write(image, "jpg", out);
		return out.toByteArray();
	}

	/**
	 * 
	 * M�todo que captura un pantallazo. <br>
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
