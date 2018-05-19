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
    private final static Context instance = new Context();
    
    public static Context getInstance(){
        return instance;
    }
    
    private DataModelTicket dataModelTicket = new DataModelTicket();
    private String leyendaRetIngBrutosCliente;
    private BigDecimal montoMinRetIngBrutos;
    private BigDecimal porcentajeIvaTarjeta;
    
    public DataModelTicket currentDMTicket(){
        return dataModelTicket;
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
     * @return the porcentajeIvaTarjeta
     */
    public BigDecimal getPorcentajeIvaTarjeta() {
        return porcentajeIvaTarjeta;
    }

    /**
     * @param porcentajeIvaTarjeta the porcentajeIvaTarjeta to set
     */
    public void setPorcentajeIvaTarjeta(BigDecimal porcentajeIvaTarjeta) {
        this.porcentajeIvaTarjeta = porcentajeIvaTarjeta;
    }
    
    
    
    
    
}
