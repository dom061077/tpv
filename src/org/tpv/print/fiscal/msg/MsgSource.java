package org.tpv.print.fiscal.msg;


/**
 * Contenedor de mensajes. Provee la funcionalidad para retornar mensajes
 * de texto.
 * @author Daniel Orlando Medina
 
 */
public interface MsgSource {

	/** 
	 * @return Retorna el mensaje de texto identificado por la <code>key</code> 
	 */
	public String get(String key);

}
