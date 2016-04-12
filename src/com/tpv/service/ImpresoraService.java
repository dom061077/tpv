/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.service;

import com.tpv.exceptions.TpvException;
import com.tpv.util.Connection;
import java.io.IOException;
import java.math.BigDecimal;
import org.apache.log4j.Logger;
import org.tpv.print.fiscal.FiscalPacket;
import org.tpv.print.fiscal.exception.FiscalPrinterIOException;
import org.tpv.print.fiscal.exception.FiscalPrinterStatusError;
import org.tpv.print.fiscal.hasar.HasarFiscalPacket;
import org.tpv.print.fiscal.hasar.HasarFiscalPrinter;
import org.tpv.print.fiscal.hasar.HasarPrinterP715F;
import org.tpv.print.fiscal.msg.FiscalMessages;

/**
 *
 * @author daniel
 */
public class ImpresoraService {
    Logger log = Logger.getLogger(ImpresoraService.class);
    private HasarPrinterP715F hfp;
    
    /**
     * Método para recuperar Nro. de punto de venta y Nro. de Ticket
     * Respueta de la impresora
     * Posicion	Datos
        *1	Status de la Impresora
        *2	Status Fiscal
        *3	Ultimo Nro Ticket B/C
        *4	Status Auxiliar
        *5	Ultimo Nro Ticket A
        *6	Status Documento
        *7	Ultimo Nro Nota credito B/C
        *8	Ultimo Nro Nota credito A
        * 
        * Posicion	Datos
            *1	Status de la Impresora
            *2	Status Fiscal
            *3	Nro de CUIT
            *4	Razon Social
            *5	Nro de registro de la impresora
            *6	Fecha de Inicializacion
            *7	Nro. Punto de Venta
            *8	Nro. Inscripción en ing. Brutos
            *9	Fecha de inicio de actividades
            *10	Responsabiliad de IVA
     * 
     * @return 
     */
    public ImpresoraService(){
        hfp = new HasarPrinterP715F(Connection.getStcp());
    }
            
    public String getNroPuntoVenta() throws TpvException{
        //HasarFiscalPrinter hfp = new HasarPrinterP715F(Connection.getStcp()); //new HasarPrinterP320F(stcp);
        FiscalPacket request;
        FiscalPacket response;
        FiscalMessages fMsg;
        try{
            request = getHfp().cmdGetInitData();
            response = getHfp().execute(request);
        }catch(FiscalPrinterIOException e){
            throw new TpvException("Error al obtener datos de la impresora. "
                +e.getFullMessage());
        }
        //response.
        return response.getString(7);
    }
    
    /**
     * 
        *1	Status de la Impresora
        *2	Status Fiscal
        *3	Ultimo Nro Ticket B/C
        *4	Status Auxiliar
        *5	Ultimo Nro Ticket A
        *6	Status Documento
        *7	Ultimo Nro Nota credito B/C
        *8	Ultimo Nro Nota credito A
     * 
     * 
     * @return 
     */
    
    public String getNroUltimoTicketBC()throws TpvException{
        //HasarFiscalPrinter hfp = new HasarPrinterP715F(Connection.getStcp()); //new HasarPrinterP320F(stcp);
        FiscalPacket request;
        FiscalPacket response;
        FiscalMessages fMsg;
        try{
            request = getHfp().cmdStatusRequest();
            response = getHfp().execute(request);
        }catch(FiscalPrinterIOException e){
            throw new TpvException("Error al obtener datos de la impresora. "
                +e.getFullMessage());
        }
        return response.getString(3);
    }
    
    
    
    public String getNroUltimoTicketA() throws TpvException{
        
        //HasarFiscalPrinter hfp = new HasarPrinterP715F(Connection.getStcpStatus()); //new HasarPrinterP320F(stcp);
        FiscalPacket request;
        FiscalPacket response;
        FiscalMessages fMsg;
        try{
            request = getHfp().cmdStatusRequest();
            response = getHfp().execute(request);
        }catch(FiscalPrinterIOException e){
            throw new TpvException("Error al obtener datos de la impresora. "
                +e.getFullMessage());
        }

        return response.getString(5);

    }
    
    public String[] getPtoVtaNrosTicket() throws TpvException{
        String[] retorno = new String[3];
        //HasarFiscalPrinter hfp = new HasarPrinterP715F(Connection.getStcp()); //new HasarPrinterP320F(stcp);
        FiscalPacket request;
        FiscalPacket response;
        try{
            request = getHfp().cmdStatusRequest();
            response = getHfp().execute(request);
            
        }catch(FiscalPrinterIOException e){
            throw new TpvException("Error al obtener datos de la impresora. "
                +e.getFullMessage());
        }catch(IllegalStateException e){
            throw new TpvException(e.getMessage());
        }
        
        retorno[1]=response.getString(3);
        retorno[2]=response.getString(5);
        response.getFiscalStatus();
        try{
           request = getHfp().cmdGetInitData();
           response = getHfp().execute(request);
        }catch(FiscalPrinterIOException e){
            throw new TpvException("Error al obtener datos de la impresora  "
                    +e.getFullMessage());
        }
        retorno[0]=response.getString(7);
        
        return retorno;
    }
    
    public void abrirTicket() throws TpvException{
        if(!Connection.getStcp().isConnected()){
            throw new TpvException("La impresora no está conectada");
        }
        
        //HasarFiscalPrinter hfp = new HasarPrinterP715F(Connection.getStcp()); //new HasarPrinterP320F(stcp);
        FiscalPacket request;
        FiscalPacket response;
        FiscalMessages fMsg;
        
        try{
            request = getHfp().cmdOpenFiscalReceipt("B");
            response = getHfp().execute(request);
        }catch(FiscalPrinterStatusError e){
            fMsg = getHfp().getMessages();
            log.error(fMsg.getErrorsAsString());
            throw new TpvException("Error en el estado de la impresora: "
                +fMsg.getErrorsAsString());
        }catch(FiscalPrinterIOException e){
            log.error(e.getFullMessage());
            throw new TpvException("Error al obtener datos de la impresora. "
                +e.getFullMessage());
        }
        
        
    }
    
    public void imprimirLineaTicket(String descripcion,BigDecimal cantidad
            ,BigDecimal precio, BigDecimal iva,boolean imprimeNegativo,BigDecimal impuestoInterno) throws TpvException{
        String _2daLineaDetalle = descripcion.substring(descripcion.length()-21,descripcion.length()-1);
        String _1erLineaDetalle = descripcion.substring(0,descripcion.length()-21);
        
        
        //HasarFiscalPrinter hfp = new HasarPrinterP715F(Connection.getStcp()); //new HasarPrinterP320F(stcp);
        FiscalPacket request2daLineaDetalle,request1eraLineaDetalle;
        FiscalPacket response;
        FiscalMessages fMsg;
        //cmdPrintLineItem(String description, BigDecimal quantity, BigDecimal price, BigDecimal ivaPercent
        //, boolean substract, BigDecimal internalTaxes, boolean basePrice, Integer display) {
        //request = hfp.cmdPrintLineItem("CACAO", new BigDecimal("1"), new BigDecimal("1"), new BigDecimal("21"), false, new BigDecimal("0"), false,0);
        request1eraLineaDetalle = getHfp().cmdPrintFiscalText(_1erLineaDetalle,0);
        request2daLineaDetalle = getHfp().cmdPrintLineItem(_2daLineaDetalle,  cantidad, precio, iva, imprimeNegativo, impuestoInterno, false,0);
//        hfp.cmdPrintFiscalText(descripcion, Integer.SIZE)
        try{
            response = getHfp().execute(request1eraLineaDetalle);
            response = getHfp().execute(request2daLineaDetalle);
        }catch(FiscalPrinterStatusError e){
            fMsg = getHfp().getMessages();
            log.error(fMsg.getErrorsAsString());
            throw new TpvException("Error en el estado de la impresora: "
                +fMsg.getErrorsAsString());
            
        }catch(FiscalPrinterIOException e){
            log.error(e.getFullMessage());
            throw new TpvException("Error al obtener datos de la impresora. "
                +e.getFullMessage());
        }
        
        
    }
    
    public void cerrarTicket() throws TpvException{
        //HasarFiscalPrinter hfp = new HasarPrinterP715F(Connection.getStcp()); //new HasarPrinterP320F(stcp);
        FiscalPacket request;
        FiscalPacket response;
        FiscalMessages fMsg;
        request = getHfp().cmdCloseFiscalReceipt(null);
        try{
          response = getHfp().execute(request);
        }catch(FiscalPrinterStatusError e){
            fMsg = getHfp().getMessages();
            log.error(fMsg.getErrorsAsString());
            throw new TpvException("Error en el estado de la impresora: "
                +fMsg.getErrorsAsString());
            
        }catch(FiscalPrinterIOException e){
            log.error(e.getFullMessage());
            throw new TpvException("Error al obtener datos de la impresora. "
                +e.getFullMessage());
        }
        
        
    }
    
    public void cancelarTicket() throws TpvException{
        //HasarFiscalPrinter hfp = new HasarPrinterP715F(Connection.getStcp()); //new HasarPrinterP320F(stcp);
        FiscalPacket request;
        FiscalPacket response;
        FiscalMessages fMsg;
        request = getHfp().cmdCancelDocument();
        try{
          response = getHfp().execute(request);
        }catch(FiscalPrinterStatusError e){
            fMsg = getHfp().getMessages();
            log.error(fMsg.getErrorsAsString());
            throw new TpvException("Error en el estado de la impresora: "
                +fMsg.getErrorsAsString());
            
        }catch(FiscalPrinterIOException e){
            log.error(e.getFullMessage());
            throw new TpvException("Error al obtener datos de la impresora. "
                +e.getFullMessage());
        }        
    }

    /**
     * @return the hfp
     */
    public HasarPrinterP715F getHfp() {
        return hfp;
    }

    /**
     * @param hfp the hfp to set
     */
    public void setHfp(HasarPrinterP715F hfp) {
        this.hfp = hfp;
    }
    
    
}
