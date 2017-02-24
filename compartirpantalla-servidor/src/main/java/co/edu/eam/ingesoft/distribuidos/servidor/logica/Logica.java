package co.edu.eam.ingesoft.distribuidos.servidor.logica;

import co.edu.eam.ingesoft.distribuidos.compartitrpantalla.dto.LoginDTO;
import co.edu.eam.ingesoft.distribuidos.compartitrpantalla.dto.RegistroDTO;
import co.edu.eam.ingesoft.distribuidos.compartitrpantalla.modelo.Usuario;

public class Logica {	

	DAOLogica logica = new DAOLogica();	
	
	/**
	 * metodo para verificar si esta el usuario.
	 * 
	 * @param usuario
	 * @return true
	 */
	public boolean verificarUsuario(LoginDTO usuario) {
		if(logica.buscarUsuario(usuario.getUsuario(), usuario.getPass())){
			return true;
		}else{
			return false;
		}
				
	}

	/**
	 * crear usuario
	 * 
	 * @param regDTO
	 */
	public boolean crearUsuario(RegistroDTO regDTO,String ip) {		
		Usuario u = new Usuario(regDTO.getUsuario(), regDTO.getPass(),ip);
		return logica.create(u);
	}

	/**
	 * metodo para actualizar la IP del usuario
	 * 
	 * @param ip
	 * @param usuario
	 */
	public void actualizarUsuario(String ip, String usuario) {

	}
}
