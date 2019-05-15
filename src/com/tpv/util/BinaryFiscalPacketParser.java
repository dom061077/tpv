/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.util;

import com.tpv.principal.FXMLMainController;
import java.math.BigDecimal;
import java.math.BigInteger;
import org.apache.log4j.Logger;
import org.tpv.print.fiscal.FiscalPacket;


/**
 *
 * @author Daniel
 */
public class BinaryFiscalPacketParser {
    Logger log = Logger.getLogger(FXMLMainController.class);
    
    FiscalPacket fiscalPacket;
    private boolean errorComprobacionMemoriaFiscal=false;
    private boolean errorComprobacionMemoriaTrabajo=false;
    private boolean pocaBateria=false;
    private boolean comandoNoReconocido=false;
    private boolean campoDatosInvalido=false;
    private boolean comandoFiscalNoValidoEnEstadoFiscal=false;
    private boolean desbordamientoTotales=false;
    private boolean memoriaFiscalLlena=false;
    private boolean memoriaFiscalCasiLlena=false;
    private boolean impresorFiscalCertificado=false;
    private boolean impresorFiscalFiscalizado=false;
    private boolean necesitaCierreJornadaFisOEnvioMaximoItems=false; //256 maximo items
    private boolean documentoFiscalAbierto=false;
    private boolean documentoFiscalAbiertoODocNoFiscalAbierto=false;
    
    
    public BinaryFiscalPacketParser(FiscalPacket fiscalPacket){
        this.fiscalPacket = fiscalPacket;
        translateFiscalStatus();
    }
    
    
    private void translateFiscalStatus(){
        String fiscalStatus = fiscalPacket.getString(2);
        log.debug("Fiscal status en string: "+fiscalStatus);
        //int num = Integer.parseInt(fiscalStatus,16);
        //String binaryStr = Integer.toBinaryString(num);
        String binaryStr = new BigInteger(fiscalStatus,16).toString(2);
        StringBuffer sb = new StringBuffer(16);
        for(int i=1;i<=16-binaryStr.length();i++){
            sb.append("0");
        }
        sb.append(binaryStr);
        binaryStr = sb.toString();
        //tomo el string en modo invertido
        if(binaryStr.substring(2,3).compareTo("1")==0){
            documentoFiscalAbiertoODocNoFiscalAbierto=true;
        }
        if(binaryStr.substring(3,4).compareTo("1")==0){
            documentoFiscalAbierto=true;
        }
        if(binaryStr.substring(4,5).compareTo("1")==0){
            necesitaCierreJornadaFisOEnvioMaximoItems=true;
        }
        if(binaryStr.substring(5,6).compareTo("1")==0){
            impresorFiscalFiscalizado=true;
        }
        if(binaryStr.substring(6,7).compareTo("1")==0){
            impresorFiscalCertificado=true;
        }
        if(binaryStr.substring(7,8).compareTo("1")==0){
            memoriaFiscalCasiLlena=true;
        }
        if(binaryStr.substring(8,9).compareTo("1")==0){
            memoriaFiscalLlena=true;
        }
        if(binaryStr.substring(9,10).compareTo("1")==0){
            desbordamientoTotales=true;
        }
        if(binaryStr.substring(10,11).compareTo("1")==0){
            comandoFiscalNoValidoEnEstadoFiscal=true;
        }
        if(binaryStr.substring(11,12).compareTo("1")==0){
            campoDatosInvalido=true;
        }
        if(binaryStr.substring(12,13).compareTo("1")==0){
            comandoNoReconocido=true;
        }
        if(binaryStr.substring(13,14).compareTo("1")==0){
            pocaBateria=true;
        }
        if(binaryStr.substring(14,15).compareTo("1")==0){
            errorComprobacionMemoriaTrabajo=true;
        }
        if(binaryStr.substring(15,16).compareTo("1")==0){
            errorComprobacionMemoriaFiscal=true;
        }
        
        
        
        
    }
    
    private void translatePrinterStatus(){
        
    }
    
    
    /**
     * @return the errorComprobacionMemoriaFiscal
     */
    public boolean isErrorComprobacionMemoriaFiscal() {
        return errorComprobacionMemoriaFiscal;
    }

    /**
     * @return the errorComprobacionMemoriaTrabajo
     */
    public boolean isErrorComprobacionMemoriaTrabajo() {
        return errorComprobacionMemoriaTrabajo;
    }

    /**
     * @return the pocaBateria
     */
    public boolean isPocaBateria() {
        return pocaBateria;
    }

    /**
     * @return the comandoNoReconocido
     */
    public boolean isComandoNoReconocido() {
        return comandoNoReconocido;
    }

    /**
     * @return the campoDatosInvalido
     */
    public boolean isCampoDatosInvalido() {
        return campoDatosInvalido;
    }

    /**
     * @return the comandoFiscalNoValidoEnEstadoFiscal
     */
    public boolean isComandoFiscalNoValidoEnEstadoFiscal() {
        return comandoFiscalNoValidoEnEstadoFiscal;
    }

    /**
     * @return the desbordamientoTotales
     */
    public boolean isDesbordamientoTotales() {
        return desbordamientoTotales;
    }

    /**
     * @return the memoriaFiscalLlena
     */
    public boolean isMemoriaFiscalLlena() {
        return memoriaFiscalLlena;
    }

    /**
     * @return the memoriaFiscalCasiLlena
     */
    public boolean isMemoriaFiscalCasiLlena() {
        return memoriaFiscalCasiLlena;
    }

    /**
     * @return the impresorFiscalCertificado
     */
    public boolean isImpresorFiscalCertificado() {
        return impresorFiscalCertificado;
    }

    /**
     * @return the impresorFiscalFiscalizado
     */
    public boolean isImpresorFiscalFiscalizado() {
        return impresorFiscalFiscalizado;
    }

    /**
     * @return the necesitaCierreJornadaFisOEnvioMaximoItems
     */
    public boolean isNecesitaCierreJornadaFisOEnvioMaximoItems() {
        return necesitaCierreJornadaFisOEnvioMaximoItems;
    }

    /**
     * @return the documentoFiscalAbierto
     */
    public boolean isDocumentoFiscalAbierto() {
        return documentoFiscalAbierto;
    }

    /**
     * @return the documentoFiscalAbiertoODocNoFiscalAbierto
     */
    public boolean isDocumentoFiscalAbiertoODocNoFiscalAbierto() {
        return documentoFiscalAbiertoODocNoFiscalAbierto;
    }
    
    public static void main(String[] args){
        /*String cadena="1234567890123";
        StringBuffer sb = new StringBuffer(cadena);
        StringBuffer sb2 = new StringBuffer(cadena);
        for(int i = 7;i<sb.length();i++){
            String str = String.valueOf(Integer.parseInt(String.valueOf(sb.charAt(i)))+7);
            if(str.length()>1)
                sb2.replace(i, i+1,str.substring(1));
            else
                sb2.replace(i, i+1, str);
            
        }
        System.out.println(sb2.substring(0, 13) .toString());*/
        
        /*DecimalFormat df = new DecimalFormat("##,##0");
        System.out.println(df.format(99999));
        System.out.println(String.format("%8.2f",99999999.2));
        System.out.println(String.format("%8.2f",9999.2));
        System.out.println(String.format("%3d",19));
        System.out.println(String.format("%6s","$1.000"));
        System.out.println(String.format("%6s","$500"));*/
        //BigDecimal valor = new BigDecimal(1.3);
        //System.out.println(valor.remainder(BigDecimal.ONE));
        String fecha = "10/10/2019";
        for(char c: fecha.toCharArray()){
            System.out.println("Char: "+c);
            
        }
        
        
        
    }
}
