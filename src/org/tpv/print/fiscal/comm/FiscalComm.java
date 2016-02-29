package org.tpv.print.fiscal.comm;

import java.io.IOException;

import org.tpv.print.fiscal.FiscalPacket;

/**
 * Define la interfaz para la comunicación con el dispositivo fiscal.
 * @author Daniel Orlando Medina
 
 */
public interface FiscalComm {

	/**
	 * Conecta la interfaz de comunicación con el dispositivo fiscal.
	 * @throws IOException si ocurre un error en la comunicación con el
	 * dispositivo.
	 */
	public void connect(int socketTimeout) throws IOException;
	
	/**
	 * @return Verdadero si la interfaz de comunicación está conectada
	 * con el dispositivo.
	 */
	public boolean isConnected();
	
	
	/**
	 * Cierra la conexión de la interfaz de comunicación.
	 * No realiza ninguna acción si la interfaz ya se encuentra desconectada. 
	 */
	public void close() throws IOException;
	
	/** 
	 * Ejecuta un comando fiscal.
	 * @throws IllegalArgumentException si request == response. 
	 */
	public void execute(FiscalPacket request, FiscalPacket response) throws IOException;
}
