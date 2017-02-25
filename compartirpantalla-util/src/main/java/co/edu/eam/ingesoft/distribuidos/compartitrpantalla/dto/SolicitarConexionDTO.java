package co.edu.eam.ingesoft.distribuidos.compartitrpantalla.dto;

import java.io.Serializable;

import co.edu.eam.ingesoft.distribuidos.compartitrpantalla.modelo.Usuario;

public class SolicitarConexionDTO implements Serializable {

	private Usuario ipCliente;
	private Usuario ipDestino;
	private String estado;

	public SolicitarConexionDTO() {
		
	}

	public SolicitarConexionDTO(Usuario Cli, Usuario Dest,String est) {
		this.ipCliente = Cli;
		this.ipDestino = Dest;
		this.estado=est;
	}
	
	

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Usuario getIpCliente() {
		return ipCliente;
	}

	public void setIpCliente(Usuario ipCliente) {
		this.ipCliente = ipCliente;
	}

	public Usuario getIpDestino() {
		return ipDestino;
	}

	public void setIpDestino(Usuario ipDestino) {
		this.ipDestino = ipDestino;
	}

	
	

}
