package co.edu.eam.ingesoft.distribuidos.compartitrpantalla.modelo;

import java.io.Serializable;

/**
 * usuario del sistema
 * @author caferrer
 *
 */
public class Usuario implements Serializable{

	private String usuario;
	private transient String pass;
	private String ip;
	
	
	public Usuario(String usuario, String pass) {
		super();
		this.usuario = usuario;
		this.pass=pass;
	}

	public Usuario(String usuario, String pass,String ip) {
		super();
		this.usuario = usuario;
		this.pass=pass;
		this.ip = ip;
	}
	
	
	public Usuario() {
	}
	
	/**
	 * @return the usuario
	 */
	public String getUsuario() {
		return usuario;
	}
	/**
	 * @param usuario the usuario to set
	 */
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	/**
	 * @return the pass
	 */
	public String getPass() {
		return pass;
	}
	/**
	 * @param pass the pass to set
	 */
	public void setPass(String pass) {
		this.pass = pass;
	}
	/**
	 * @return the ip
	 */
	public String getIp() {
		return ip;
	}
	/**
	 * @param ip the ip to set
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}
	
	
	@Override
	public String toString() {
		return usuario+"-"+ip;
	}
	
	
	
	
	
	
	
}
