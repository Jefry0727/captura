package co.edu.eam.ingesoft.distribuidos.compartitrpantalla.dto;

import java.io.Serializable;
import java.util.List;

import co.edu.eam.ingesoft.distribuidos.compartitrpantalla.modelo.Usuario;

/**
 * dto para el login
 * 
 * @author caferrer
 *
 */
public class ListaUsuariosDTO implements Serializable {

	private List<Usuario> usuarios;

	public ListaUsuariosDTO() {

	}

	/**
	 * constructor
	 * 
	 * @param usuarios
	 */
	public ListaUsuariosDTO(List<Usuario> usuarios) {
		super();
		this.usuarios = usuarios;
	}

	/**
	 * @return the usuarios
	 */
	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	/**
	 * @param usuarios
	 *            the usuarios to set
	 */
	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

}
