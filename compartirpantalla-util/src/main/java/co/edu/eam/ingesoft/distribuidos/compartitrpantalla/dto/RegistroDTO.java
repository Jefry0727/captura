package co.edu.eam.ingesoft.distribuidos.compartitrpantalla.dto;

import java.io.Serializable;

/**
 * dto para el login
 * @author caferrer
 *
 */
public class RegistroDTO implements Serializable{

	private String usuario;
	private String pass;
	
	
	public RegistroDTO() {
		// TODO Auto-generated constructor stub
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
	
	
}
