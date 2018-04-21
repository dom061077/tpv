/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.util;

import org.tpv.print.fiscal.FiscalPacket;

/**
 *
 * @author Daniel
 */
public class BinaryFiscalPacketParser {
    FiscalPacket fiscalPacket;
    private boolean errorComprobacionMemoriaFiscal;
    private boolean errorComprobacionMemoriaTrabajo;
    private boolean pocaBateria;
    private boolean comandoNoReconocido;
    private boolean campoDatosInvalido;
    private boolean comandoFiscalNoValidoEnEstadoFiscal;
    private boolean desbordamientoTotales;
    private boolean memoriaFiscalLlena;
    private boolean memoriaFiscalCasiLlena;
    private boolean impresorFiscalCertificado;
    private boolean impresorFiscalFiscalizado;
    private boolean necesitaCierreJornadaFisOEnvioMaximoItems; //256 maximo items
    private boolean documentoFiscalAbierto;
    private boolean documentoFiscalAbiertoODocNoFiscalAbierto;
    
    
    public BinaryFiscalPacketParser(FiscalPacket fiscalPacket){
        this.fiscalPacket = fiscalPacket;
        translateFiscalStatus();
    }
    
    
    private void translateFiscalStatus(){
        String fiscalStatus = fiscalPacket.getString(2);
        
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
}
