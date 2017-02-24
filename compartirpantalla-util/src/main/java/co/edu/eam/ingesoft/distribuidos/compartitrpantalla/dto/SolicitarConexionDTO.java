package co.edu.eam.ingesoft.distribuidos.compartitrpantalla.dto;

import java.io.Serializable;

public class SolicitarConexionDTO implements Serializable {

	private String ipCliente;
	private String ipDestino;

	public SolicitarConexionDTO() {
		
	}

	public SolicitarConexionDTO(String ipCli, String ipDest) {
		this.ipCliente = ipCli;
		this.ipDestino = ipDest;
	}

	public String getIpCliente() {
		return ipCliente;
	}

	public void setIpCliente(String ipCliente) {
		this.ipCliente = ipCliente;
	}

	public String getIpDestino() {
		return ipDestino;
	}

	public void setIpDestino(String ipDestino) {
		this.ipDestino = ipDestino;
	}
	
	

}
