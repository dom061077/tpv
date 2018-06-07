/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.principal;

import java.math.BigDecimal;

/**
 *
 * @author daniel
 */
public class Context {

    /**
     * @return the formatNumeroDinero
     */
    public String getFormatNumeroDinero() {
        return formatNumeroDinero;
    }

    /**
     * @param formatNumeroDinero the formatNumeroDinero to set
     */
    public void setFormatNumeroDinero(String formatNumeroDinero) {
        this.formatNumeroDinero = formatNumeroDinero;
    }
    private final static Context instance = new Context();
    
    public static Context getInstance(){
        return instance;
    }
    
    private DataModelTicket dataModelTicket = new DataModelTicket();
    private String leyendaRetIngBrutosCliente;
    private BigDecimal montoMinRetIngBrutos;
    private BigDecimal porcentajeIvaIntTarjeta;
    private String leyendaIntTarjeta;
    private BigDecimal porcentajeIvaBonifTarjeta;
    private String leyendaBonifTarjeta;
    private String formatNumeroDinero;
    
    public DataModelTicket currentDMTicket(){
        return getDataModelTicket();
    }

    /**
     * @return the leyendaRetIngBrutosCliente
     */
    public String getLeyendaRetIngBrutosCliente() {
        return leyendaRetIngBrutosCliente;
    }

    /**
     * @param leyendaRetIngBrutosCliente the leyendaRetIngBrutosCliente to set
     */
    public void setLeyendaRetIngBrutosCliente(String leyendaRetIngBrutosCliente) {
        this.leyendaRetIngBrutosCliente = leyendaRetIngBrutosCliente;
    }

    /**
     * @return the montoMinRetIngBrutos
     */
    public BigDecimal getMontoMinRetIngBrutos() {
        return montoMinRetIngBrutos;
    }

    /**
     * @param montoMinRetIngBrutos the montoMinRetIngBrutos to set
     */
    public void setMontoMinRetIngBrutos(BigDecimal montoMinRetIngBrutos) {
        this.montoMinRetIngBrutos = montoMinRetIngBrutos;
    }

    /**
     * @return the porcentajeIvaIntTarjeta
     */
    public BigDecimal getPorcentajeIvaIntTarjeta() {
        return porcentajeIvaIntTarjeta;
    }

    /**
     * @param porcentajeIvaIntTarjeta the porcentajeIvaIntTarjeta to set
     */
    public void setPorcentajeIvaIntTarjeta(BigDecimal porcentajeIvaIntTarjeta) {
        this.porcentajeIvaIntTarjeta = porcentajeIvaIntTarjeta;
    }

    /**
     * @return the porcentajeIvaBonifTarjeta
     */
    public BigDecimal getPorcentajeIvaBonifTarjeta() {
        return porcentajeIvaBonifTarjeta;
    }

    /**
     * @param porcentajeIvaBonifTarjeta the porcentajeIvaBonifTarjeta to set
     */
    public void setPorcentajeIvaBonifTarjeta(BigDecimal porcentajeIvaBonifTarjeta) {
        this.porcentajeIvaBonifTarjeta = porcentajeIvaBonifTarjeta;
    }

    /**
     * @return the leyendaIntTarjeta
     */
    public String getLeyendaIntTarjeta() {
        return leyendaIntTarjeta;
    }

    /**
     * @param leyendaIntTarjeta the leyendaIntTarjeta to set
     */
    public void setLeyendaIntTarjeta(String leyendaIntTarjeta) {
        this.leyendaIntTarjeta = leyendaIntTarjeta;
    }

    /**
     * @return the leyendaBonifTarjeta
     */
    public String getLeyendaBonifTarjeta() {
        return leyendaBonifTarjeta;
    }

    /**
     * @param leyendaBonifTarjeta the leyendaBonifTarjeta to set
     */
    public void setLeyendaBonifTarjeta(String leyendaBonifTarjeta) {
        this.leyendaBonifTarjeta = leyendaBonifTarjeta;
    }

    /**
     * @return the dataModelTicket
     */
    public DataModelTicket getDataModelTicket() {
        return dataModelTicket;
    }

    /**
     * @param dataModelTicket the dataModelTicket to set
     */
    public void setDataModelTicket(DataModelTicket dataModelTicket) {
        this.dataModelTicket = dataModelTicket;
    }


    

    
    
    
    
}
