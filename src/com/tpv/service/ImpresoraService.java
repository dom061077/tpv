/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.service;

import com.tpv.exceptions.TpvException;
import com.tpv.modelo.Factura;
import com.tpv.modelo.FacturaDetalleCombo;
import com.tpv.modelo.FacturaDetalleConcurso;
import com.tpv.modelo.RetiroDinero;
import com.tpv.modelo.RetiroDineroDetalle;
import com.tpv.principal.Context;
import com.tpv.util.BinaryFiscalPacketParser;
import com.tpv.util.Connection;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.log4j.Logger;
import org.tpv.print.fiscal.FiscalPacket;
import org.tpv.print.fiscal.exception.FiscalPrinterIOException;
import org.tpv.print.fiscal.exception.FiscalPrinterStatusError;
import org.tpv.print.fiscal.hasar.HasarFiscalPrinter;
import org.tpv.print.fiscal.hasar.HasarPrinter250F;
import org.tpv.print.fiscal.hasar.HasarPrinterP715F;
import org.tpv.print.fiscal.msg.FiscalMessages;

/**
 *
 * @author daniel
 */
public class ImpresoraService {
    Logger log = Logger.getLogger(ImpresoraService.class);
    private HasarPrinterP715F hfpP715F ;
    private HasarPrinter250F hfp250F;
    private String modeloImpresora;
    
    public static final String MODELOIMPRESORA_SMH_P_441F="SMH/P-441F";
    public static final String MODELOIMPRESORA_SMH_PT_1000F="SMH/PT-1000F";
    
    public static final String IVA_RESPONSABLE_INS = "I";
    public static final String IVA_RESPONSABLE_NO_INS = "N";
    public static final String IVA_EXENTO = "E";
    public static final String IVA_NO_RESPONSABLE = "A";
    public static final String IVA_CONSUMIDOR_FINAL = "C";
    public static final String IVA_RESPONSALE_NO_INSCRIPTO = "B";
    public static final String IVA_RESPONSABLE_MONOTRIBUTO = "M";
    public static final String IVA_MONOTRIBUTO_SOCIAL = "S";
    public static final String IVA_PEQUENIO_CONTRIBUYENTE_EVENTUAL = "V";
    public static final String IVA_PEQUENIO_CONTRIBUYENTE_EVENTUAL_SOCIAL = "W";
    public static final String IVA_NO_CATEGORIZADO = "T";
    
    public static final String TIPODOC_CUIT="C";
    public static final String TIPODOC_CUIL="L";
    public static final String TIPODOC_LIBRETA_ENROLAMIENTO="0";
    public static final String TIPODOC_LIBRETA_CIVICA="1";
    public static final String TIPODOC_DNI="2";
    public static final String TIPODOC_PASAPORTE="3";
    public static final String TIPODOC_CEDULA_IDENTIDAD="4";
    public static final String TIPODOC_SIN_CALIFICADOR=" ";
    
    
    
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
            hfpP715F = new HasarPrinterP715F(Connection.getStcp());
            hfp250F = new HasarPrinter250F(Connection.getStcp());
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
            log.warn("Error al obtener el Nro de punto de venta",e);
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
            log.warn("Error al obtener el ultimo Nro de Ticket",e);
            throw new TpvException(e.getMessage());
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
            log.warn("Error al obtener el ultimo Nro de ticket A",e);
            throw new TpvException(e.getMessage());
        }

        return response.getString(5);

    }
    
    public String[] getPtoVtaNrosTicket() throws TpvException{
        String[] retorno = new String[4];
        //HasarFiscalPrinter hfp = new HasarPrinterP715F(Connection.getStcp()); //new HasarPrinterP320F(stcp);
        FiscalPacket request;
        FiscalPacket response;
        try{
            request = getHfp().cmdStatusRequest();
            response = getHfp().execute(request);
            
        }catch(FiscalPrinterIOException e){
            log.warn("Error al obtener Pto de Venta y Nros de ticket",e);
            throw new TpvException(e.getMessage());
        }catch(IllegalStateException e){
            throw new TpvException(e.getMessage());
        }
        
        retorno[1]=response.getString(3);
        retorno[2]=response.getString(5);
        
        BinaryFiscalPacketParser binaryFP = new BinaryFiscalPacketParser(response);
        retorno[3]=String.valueOf(binaryFP.isDocumentoFiscalAbierto());
        
        
        try{
           request = getHfp().cmdGetInitData();
           response = getHfp().execute(request);
        }catch(FiscalPrinterIOException e){
            throw new TpvException(e.getMessage());
        }
        retorno[0]=response.getString(7);
        
        return retorno;
    }
    
    private String traducirIVAcmdCustomerData(int condicionIVA){
        switch (condicionIVA){
            case 1:
                return IVA_CONSUMIDOR_FINAL;
            case 2:
                return IVA_RESPONSABLE_INS;
            case 5:
                return IVA_EXENTO;
            case 6:
                return IVA_RESPONSABLE_MONOTRIBUTO;
        }
        return "";
    }
    
    private void printHeaderTrailer(String linea11NroTarj,String linea12Villeco
            ,String linea13DatosFact,ArrayList<String> concursos)
            throws FiscalPrinterIOException, FiscalPrinterStatusError {
                /*líneas SetHeaderTrailer
                11 Numero tarjeta
                12 producto Y orden Y cuenta de villeto
                13 CHECKOUT CAJERO CAJA DIA MES Y HORA PREFIJO - NRO DE COMPROBANTE(ID factura)
                14 concurso
                15 concurso*/          
        FiscalPacket request;
        FiscalPacket response;
        request = getHfp().cmdSetHeaderTrailer(11,linea11NroTarj);
        response = getHfp().execute(request);
        
        request  = getHfp().cmdSetHeaderTrailer(12,linea12Villeco);
        response = getHfp().execute(request);
        
        request = getHfp().cmdSetHeaderTrailer(13,linea13DatosFact);
        response = getHfp().execute(request);
        int linea = 14;
        for(String item : concursos){
            request = getHfp().cmdSetHeaderTrailer(linea,item);
            response = getHfp().execute(request);
            linea++;
            if(linea>17)
                break;
        }
        
    }
    
    public void abrirTicket() throws TpvException{
      
        if(!Connection.getStcp().isConnected()){
            throw new TpvException("La impresora no está conectada");
        }
        
        //HasarFiscalPrinter hfp = new HasarPrinterP715F(Connection.getStcp()); //new HasarPrinterP320F(stcp);
        FiscalPacket request;
        FiscalPacket requestStatus;
        FiscalPacket response;
        FiscalMessages fMsg;
        
        try{
            requestStatus = getHfp().cmdStatusRequest();
            response = getHfp().execute(requestStatus);
            

            
            if(Context.getInstance().currentDMTicket().getCliente()!=null){
                if(Context.getInstance().currentDMTicket().getCliente().getCondicionIva().getId()!=1){
                    //cmdSetCustomerData(String name, String customerDocNumber, String ivaResponsibility, String docType, String location)

                    request = getHfp().cmdSetCustomerData(
                            Context.getInstance().currentDMTicket().getCliente().getRazonSocial()
                            , Context.getInstance().currentDMTicket().getCliente().getCuit()
                            , traducirIVAcmdCustomerData(Context.getInstance().currentDMTicket().getCliente().getCondicionIva().getId())
                            , TIPODOC_CUIT
                            , Context.getInstance().currentDMTicket().getCliente().getDireccion());
                    response = getHfp().execute(request);
                }

                if(Context.getInstance().currentDMTicket().getCliente().getCondicionIva().getId()==2)
                    request = getHfp().cmdOpenFiscalReceipt("A");
                else    
                    request = getHfp().cmdOpenFiscalReceipt("B");
            }else
                request = getHfp().cmdOpenFiscalReceipt("B");
            response = getHfp().execute(request);
        }catch(FiscalPrinterStatusError e){
            fMsg = getHfp().getMessages();
            log.warn("Error fiscal al abrir el ticket",e);
            throw new TpvException(e.getMessage());
        }catch(FiscalPrinterIOException e){
            log.warn("Error de Impresora al abrir el ticket",e);
            throw new TpvException(e.getMessage());
        }
        
        
    }
    
    public void imprimirLineaTicket(String descripcion,BigDecimal cantidad
            ,BigDecimal precio, BigDecimal iva,boolean imprimeNegativo,BigDecimal impuestoInterno) throws TpvException{
        String _2daLineaDetalle = "";
        String _1erLineaDetalle = "";
        
        if(descripcion.length()>20){
            _1erLineaDetalle = descripcion.substring(0,descripcion.length()-21);
            _2daLineaDetalle = descripcion.substring(descripcion.length()-21,descripcion.length()-1);
        }else{
            _1erLineaDetalle = descripcion;
        }
            
        
        
        
        //HasarFiscalPrinter hfp = new HasarPrinterP715F(Connection.getStcp()); //new HasarPrinterP320F(stcp);
        FiscalPacket request2daLineaDetalle,request1eraLineaDetalle,requestEstado;
        FiscalPacket response;
        FiscalMessages fMsg;
        //cmdPrintLineItem(String description, BigDecimal quantity, BigDecimal price, BigDecimal ivaPercent
        //, boolean substract, BigDecimal internalTaxes, boolean basePrice, Integer display) {
        //request = hfp.cmdPrintLineItem("CACAO", new BigDecimal("1"), new BigDecimal("1"), new BigDecimal("21"), false, new BigDecimal("0"), false,0);
        request1eraLineaDetalle = getHfp().cmdPrintFiscalText(_1erLineaDetalle,0);
        request2daLineaDetalle = getHfp().cmdPrintLineItem(_2daLineaDetalle,  cantidad, precio, iva, imprimeNegativo, impuestoInterno, false,0);
        requestEstado = getHfp().cmdStatusRequest();
//        hfp.cmdPrintFiscalText(descripcion, Integer.SIZE)
        try{
            response = getHfp().execute(requestEstado);
            response = getHfp().execute(request1eraLineaDetalle);
            if(_2daLineaDetalle.compareTo("")!=0)
                response = getHfp().execute(request2daLineaDetalle);
        }catch(FiscalPrinterStatusError e){
            fMsg = getHfp().getMessages();
            log.warn("Error fiscal al imprimir linea de ticket",e);
            log.info("Codigos error en PrinterStatusError: "+e.getResponsePacket().getPrinterStatus());
            log.info("Codigos error del estado fiscal: "+e.getResponsePacket().getFiscalStatus());
            throw new TpvException(e.getMessage());
            
        }catch(FiscalPrinterIOException e){
            log.warn("Codigo de Error en PrinterIOException de Impresora al imprimir linea de ticket",e);
            
            throw new TpvException(e.getMessage());
        }
        
        
    }
    
    public void cerrarTicket(Factura factura) throws TpvException{
        //HasarFiscalPrinter hfp = new HasarPrinterP715F(Connection.getStcp()); //new HasarPrinterP320F(stcp);
        log.info("Cierre de factura en capa de servicios. Factura id: "+factura.getId());
        FiscalPacket request;
        FiscalPacket requestStatus;
        FiscalPacket response;
        requestStatus = getHfp().cmdStatusRequest();
        for(Iterator<FacturaDetalleCombo> it = factura.getDetalleCombosAux().iterator();it.hasNext();){
            FacturaDetalleCombo fdc = it.next();
            
            request = getHfp().cmdReturnRecharge(fdc.getCombo().getDescripcion(),
                            fdc.getBonificacion(),
                            BigDecimal.valueOf(21), true,
                            BigDecimal.ZERO, false, 0, "B");
            try{
                response = getHfp().execute(requestStatus);
                response = getHfp().execute(request);
            }catch(FiscalPrinterStatusError e){
                log.warn("Error en estado fiscal de la impresora al imprimir descuentos por combos",e);
                throw new TpvException(e.getMessage());
            
            }catch(FiscalPrinterIOException e){
                log.warn("Error de entrada/salida en la impresora fical al imprimir descuentos por combos",e);
                throw new TpvException(e.getMessage());
            }
          
        }
        

        if(factura.getBonificaTarjeta().compareTo(BigDecimal.ZERO)>0){
            request = getHfp().cmdReturnRecharge(Context.getInstance().currentDMParametroGral().getLeyendaBonifTarjeta(),
                            factura.getBonificaTarjeta(),
                            BigDecimal.valueOf(21), true,
                            BigDecimal.ZERO, false, 0, "B");
            try{
                response = getHfp().execute(requestStatus);
                response = getHfp().execute(request);
            }catch(FiscalPrinterStatusError e){
                log.warn("Error en estado fiscal de la impresora al imprimir bonificación de tarjeta",e);
                throw new TpvException(e.getMessage());
            }catch(FiscalPrinterIOException e){
                log.warn("Error de entrada/salida en la impresora fiscal al imprimir bonificación de tarjeta",e);
            }
        }
        if(factura.getInteresTarjeta().compareTo(BigDecimal.ZERO)>0){
            request = getHfp().cmdReturnRecharge(Context.getInstance().currentDMParametroGral().getLeyendaIntTarjeta(),
                            factura.getInteresTarjeta(),
                            BigDecimal.valueOf(21), false,
                            BigDecimal.ZERO, false, 0, "B");
            try{
                response = getHfp().execute(requestStatus);
                response = getHfp().execute(request);
            }catch(FiscalPrinterStatusError e){
                log.warn("Error en estado fiscal de la impresora al imprimir interés de tarjeta",e);
                throw new TpvException(e.getMessage());
            }catch(FiscalPrinterIOException e){
                log.warn("Error de entrada/salida en la impresora fiscal al imprimir interés de tarjeta",e);
            }
            
        }
        
        if(factura.getRetencion().compareTo(BigDecimal.ZERO)>0 ){
            request = getHfp().cmdPerceptions(Context.getInstance().currentDMParametroGral().getLeyendaRetIngBrutosCliente(), factura.getRetencion(), null);
            try{
                response = getHfp().execute(requestStatus);
                response = getHfp().execute(request);
            }catch(FiscalPrinterStatusError e){
                log.warn("Error en estado fiscal de la impresora al imprimir retención");
                throw new TpvException(e.getMessage());
            }catch(FiscalPrinterIOException e){
                log.warn("Error de entrada/salida en la impresora fiscal al imprimir retención",e);
                throw new TpvException(e.getMessage());
            }
        }   
        ArrayList<String> concursos = new ArrayList();
        if(!factura.getDetalleConcursos().isEmpty()){
            for(Iterator<FacturaDetalleConcurso> it = factura.getDetalleConcursos().iterator();it.hasNext(); ){
                FacturaDetalleConcurso fdet = it.next();
                String linea=
                        String.format("%1$-15.15s",fdet.getConcurso().getTextoCorto())+" "
                        +String.format("%2s",fdet.getCantidadCupones());
                fdet=null;
                if(it.hasNext())
                    fdet = it.next();
                if(fdet!=null)
                    linea= linea + " | "
                            +String.format("%1$-15.15s",fdet.getConcurso().getTextoCorto())+" "
                            +String.format("%2s",fdet.getCantidadCupones());
                concursos.add(linea);
            }
        }
        
        try{        
            SimpleDateFormat sdfDia = new SimpleDateFormat("dd");
            SimpleDateFormat sdfMes = new SimpleDateFormat("MM");
            SimpleDateFormat sdfHora = new SimpleDateFormat("hh:mm");
            
            for(int i = concursos.size();i<=2;i++){
                concursos.add(" ");
            }
            
            printHeaderTrailer(" "
                        ,Context.getInstance().currentDMParametroGral().getSetHeaderTrailerLinea12()
                        ,Context.getInstance().currentDMTicket().getCheckout().getId()+" "
                            +Context.getInstance().currentDMTicket().getUsuario().getIdUsuario()+" "
                            +Context.getInstance().currentDMTicket().getCaja()+" "
                            +sdfDia.format(factura.getFechaHoy())+" "
                            +sdfMes.format(factura.getFechaHoy())+" "
                            +sdfHora.format(factura.getFechaHoy())+" "
                            +factura.getPrefijoFiscal()+" "
                            +factura.getNumeroComprobante()+" "
                            +factura.getId()
                        ,concursos);
        }catch(FiscalPrinterStatusError e){
            log.warn("Error en estado fiscal de la impresora al imprimir retención");
            throw new TpvException(e.getMessage());
        }catch(FiscalPrinterIOException e){
            log.warn("Error de entrada/salida en la impresora fiscal al imprimir retención",e);
            throw new TpvException(e.getMessage());
        }
        
        //comando para extraer la fecha y hora fiscal
        /*request = getHfp().cmdGetDateTime();
        try{
            response = getHfp().execute(request);
        }catch(FiscalPrinterStatusError e){
            log.warn("Error en estado fiscal de la impresora al cerrar el ticket fiscal",e);
            throw new TpvException(e.getMessage());
            
        }catch(FiscalPrinterIOException e){
            log.warn("Error de entrada/salida en la impresora fical",e);
            throw new TpvException(e.getMessage());
        }*/
        
        request = getHfp().cmdCloseFiscalReceipt(null);
        try{
          response = getHfp().execute(requestStatus);
          response = getHfp().execute(request);
        }catch(FiscalPrinterStatusError e){
            log.warn("Error en estado fiscal de la impresora al cerrar el ticket fiscal",e);
            throw new TpvException(e.getMessage());
            
        }catch(FiscalPrinterIOException e){
            log.warn("Error de entrada/salida en la impresora fical",e);
            throw new TpvException(e.getMessage());
        }
        
        
    }
    
    public void cancelarTicket() throws TpvException{
        //HasarFiscalPrinter hfp = new HasarPrinterP715F(Connection.getStcp()); //new HasarPrinterP320F(stcp);
        FiscalPacket request,requestStatus;
        FiscalPacket response;
        FiscalMessages fMsg;
        request = getHfp().cmdCancelDocument();
        requestStatus = getHfp().cmdStatusRequest();
        try{
          response = getHfp().execute(requestStatus);
          response = getHfp().execute(request);
        }catch(FiscalPrinterStatusError e){
            fMsg = getHfp().getMessages();
            log.warn("Error fiscal al cancelar el ticket",e);
            throw new TpvException(e.getMessage());
            
        }catch(FiscalPrinterIOException e){
            log.warn("Error de impresora al cancelar el ticket",e);
            throw new TpvException(e.getMessage());
        }        
    }
    
    public void cierreZ() throws TpvException{
        FiscalPacket request;
        FiscalPacket response;
        FiscalMessages fMsg;
        request = getHfp().cmdDailyClose("Z");
        try{
            response = getHfp().execute(request);
        }catch(FiscalPrinterStatusError e){
            fMsg = getHfp().getMessages();
            log.warn("Error de estado en la impresora al hacer cierre diario Z",e);
            throw new TpvException(e.getMessage());
        }catch(FiscalPrinterIOException e){
            log.warn("Error mecánico de impresora al hacer cierre diario Z",e);
            throw new TpvException(e.getMessage());
        }
        
    }
    
    public void cierreX() throws TpvException{
        FiscalPacket request;
        FiscalPacket response;
        FiscalMessages fMsg;
        request = getHfp().cmdDailyClose("X");
        try{
            response = getHfp().execute(request);
        }catch(FiscalPrinterStatusError e){
            fMsg = getHfp().getMessages();
            log.warn("Error de estado en la impresora al hacer cierre diario X",e);
            throw new TpvException(e.getMessage());
        }catch(FiscalPrinterIOException e){
            log.warn("Error mecánico de impresora al hacer cierre diario X",e);
            throw new TpvException(e.getMessage());
        }
    }
    
    public void imprimirRetiroDinero(RetiroDinero retiroDinero)throws TpvException{
        FiscalMessages fMsg;
        ArrayList<String> concursos = new ArrayList();
        concursos.add(" ");
        concursos.add(" ");
        concursos.add(" ");
        try{
            printHeaderTrailer(" ", " ", " ", concursos);
        }catch(FiscalPrinterStatusError e){
            fMsg = getHfp().getMessages();
            log.warn("Error de estado en la impresora al imprimir retiro de dinero",e);
            throw new TpvException(e.getMessage());
        }catch(FiscalPrinterIOException e){
            log.warn("Error mécanico de impresora al imprimir retiro de dinero",e);
            throw new TpvException(e.getMessage());
        }            
        imprimirRetiroDinero(retiroDinero,"Original ");
        imprimirRetiroDinero(retiroDinero,"Duplicado");
    }
    
    private void imprimirRetiroDinero(RetiroDinero retiroDinero,String titulo) throws TpvException{
        FiscalPacket requestOpen;
        FiscalPacket requestCerrar;
        FiscalPacket requestNonFiscalText;
        FiscalPacket response;
        FiscalMessages fMsg;
        requestOpen = getHfp().cmdOpenNonFiscalReceipt();
        requestCerrar = getHfp().cmdCloseNonFiscalReceipt(Integer.valueOf("1"));
        
        
        try{
            response = getHfp().execute(requestOpen);
            
            requestNonFiscalText = getHfp().cmdPrintNonFiscalText("                "+titulo+"                ", Integer.valueOf("1"));
            response = getHfp().execute(requestNonFiscalText);
            
            requestNonFiscalText = getHfp().cmdPrintNonFiscalText(" ", Integer.valueOf("1"));            
            response = getHfp().execute(requestNonFiscalText);            
            
            requestNonFiscalText = getHfp().cmdPrintNonFiscalText("       RETIRO DE DINERO DE CAJERO       ", Integer.valueOf("1"));
            response = getHfp().execute(requestNonFiscalText);
            
            requestNonFiscalText = getHfp().cmdPrintNonFiscalText(" ", Integer.valueOf("1"));
            response = getHfp().execute(requestNonFiscalText);
            
            SimpleDateFormat dateF = new SimpleDateFormat("dd/MM/yyyy");
            requestNonFiscalText = getHfp()
                    .cmdPrintNonFiscalText("Fecha: "+dateF.format(retiroDinero.getFechaRetiro())
                        + " Cajero: "+retiroDinero.getUsuario().getIdUsuario()
                        + " Caja: "+retiroDinero.getCaja()
                        , Integer.valueOf("1"));
            response = getHfp().execute(requestNonFiscalText);
            
            requestNonFiscalText = getHfp().cmdPrintNonFiscalText("Checkout: "
                    +retiroDinero.getCheckout().getId()
                    +" Retiro Nro: "+retiroDinero.getId()
                    , Integer.valueOf("1"));
            response = getHfp().execute(requestNonFiscalText);
            
            String strColumns = "Billete";
            strColumns+="   ";
            strColumns+="Cantidad";
            strColumns+="   ";
            strColumns+="   SubTotal ";
            requestNonFiscalText = getHfp().cmdPrintNonFiscalText(strColumns,Integer.valueOf("0"));
            response = getHfp().execute(requestNonFiscalText);
            for(Iterator<RetiroDineroDetalle> it = retiroDinero.getDetalle().iterator();it.hasNext(); ){
                RetiroDineroDetalle retDetalle = it.next();
                DecimalFormat dfCantidad = new DecimalFormat("###,##0");
                DecimalFormat dfSubTtal = new DecimalFormat("##,###,##0.00");
                String strLine =  String.format("%6s",retDetalle.getBillete().getDetalleFormulario());
                strLine += "   ";
                strLine += String.format("%8s",dfCantidad.format(retDetalle.getCantidadBilletes()));
                strLine += "   ";
                strLine += String.format("%13s",dfSubTtal.format(retDetalle.getMonto()));
                strLine += "   ";
                requestNonFiscalText = getHfp().cmdPrintNonFiscalText(strLine, Integer.valueOf("0"));
                response = getHfp().execute(requestNonFiscalText);
            }
            
            requestNonFiscalText = getHfp().cmdPrintNonFiscalText("                      ---------------", Integer.valueOf("1"));
            response = getHfp().execute(requestNonFiscalText);
            
            DecimalFormat dfTotalGral = new DecimalFormat("###,###,##0.00");
            String strTotalGral = String.format("%33s",dfTotalGral.format(retiroDinero.getMonto()));
            requestNonFiscalText = getHfp().cmdPrintNonFiscalText(strTotalGral,Integer.valueOf("1"));
            response = getHfp().execute(requestNonFiscalText);
            requestNonFiscalText = getHfp().cmdPrintNonFiscalText(
                        String.format("%40s", "Obs: "+retiroDinero.getObservacion())
                        ,Integer.valueOf("1"));
            response = getHfp().execute(requestNonFiscalText);
            
            requestNonFiscalText = getHfp().cmdPrintNonFiscalText("_ _ _ _ _ _ _ _ _      _ _ _ _ _ _ _ _ _"
                    , Integer.valueOf("1"));
            response = getHfp().execute(requestNonFiscalText);
            
            requestNonFiscalText = getHfp().cmdPrintNonFiscalText("  Firma Cajero          Firma Supervisor"
                    , Integer.valueOf("1"));
            response = getHfp().execute(requestNonFiscalText);
            
            
            response = getHfp().execute(requestCerrar);
        }catch(FiscalPrinterStatusError e){
            fMsg = getHfp().getMessages();
            log.warn("Error de estado en la impresora al imprimir retiro de dinero",e);
            throw new TpvException(e.getMessage());
        }catch(FiscalPrinterIOException e){
            log.warn("Error mécanico de impresora al imprimir retiro de dinero",e);
            throw new TpvException(e.getMessage());
        }
    }
    

    /**
     * @return the hfp
     */
    public HasarFiscalPrinter getHfp() {
        //if (Context.getInstance().getModeloImpresora()==MODELOIMPRESORA_SMH_P_441F)
        //    return hfpP715F;
        if (Context.getInstance().currentDMTicket().getModeloImpresora().compareTo(MODELOIMPRESORA_SMH_PT_1000F)==0)
            return hfp250F;
        return hfpP715F;
    }
    

    /**
     * @param hfp the hfp to set
     */
    //public void setHfp(HasarPrinterP715F hfp) {
    //    this.hfp = hfp;
    //}
    
    public void getPrinterVersion() throws TpvException{
        FiscalPacket request;
        FiscalPacket response;
        FiscalMessages fMsg;
        request = getHfp().cmdGetFiscalDeviceVersion();
        try{
          response = getHfp().execute(request);
          if (response.getString(3).contains(MODELOIMPRESORA_SMH_P_441F))
              Context.getInstance().currentDMTicket().setModeloImpresora(MODELOIMPRESORA_SMH_P_441F);
          if (response.getString(3).contains(MODELOIMPRESORA_SMH_PT_1000F))
              Context.getInstance().currentDMTicket().setModeloImpresora(MODELOIMPRESORA_SMH_PT_1000F);
          log.info("Modelo de Impresora conectada: "+response.getString(3));
        }catch(FiscalPrinterStatusError e){
            fMsg = getHfp().getMessages();
            log.error(fMsg.getErrorsAsString());
            throw new TpvException(e.getMessage());
            
        }catch(FiscalPrinterIOException e){
            log.error(e.getFullMessage());
            throw new TpvException(e.getMessage());
        }        
    }
    
    
}
