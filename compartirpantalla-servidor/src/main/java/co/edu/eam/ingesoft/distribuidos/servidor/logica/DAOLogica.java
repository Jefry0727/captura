package co.edu.eam.ingesoft.distribuidos.servidor.logica;

import java.sql.SQLException;

import co.edu.eam.ingesoft.distribuidos.compartitrpantalla.modelo.Usuario;

public class DAOLogica extends ClsConexion {

	public boolean buscarUsuario(String usuario, String password) {

		System.out.println("llego al dao verificar");
		String consulta = "select usuario,contrasenia from cliente where usuario='" + usuario + "' and contrasenia='"
				+ password + "'";
		super.ejecutarRetorno(consulta);
		System.out.println(consulta);
		try {
			if (resultadoDB.next()) {
				return true;
			}
		} catch (SQLException ex) {
			System.out.println("Esto se tosto");
			return false;
		}
		return false;
	}

	public boolean create(Usuario usu) {

		System.out.println("llego al dao verificar");
		String consulta = "insert into cliente (usuario,contrasenia,ip) " + "values('" + usu.getUsuario() + "','"
				+ usu.getPass() +"','"+ usu.getIp()+"')";
		System.out.println(consulta);
		super.ejecutar(consulta);		
		return true;

	}

}
