/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.modelo.enums;

/**
 *
 * @author daniel
 */
public enum FacturaEstadoEnum {
    ANULADA("Factura Anulada",1),
    ABIERTA("Factura Abierta",2),
    CERRADA("Factura Cerrada",3);
    
    private String estadoStr;
    private int estadoInt;
    
    private FacturaEstadoEnum(String estadoStr,int estadoInt){
        this.estadoInt = estadoInt;
        this.estadoStr = estadoStr;
    }

    /**
     * @return the estadoStr
     */
    public String getEstadoStr() {
        return estadoStr;
    }

    /**
     * @param estadoStr the estadoStr to set
     */
    public void setEstadoStr(String estadoStr) {
        this.estadoStr = estadoStr;
    }

    /**
     * @return the estadoInt
     */
    public int getEstadoInt() {
        return estadoInt;
    }

    /**
     * @param estadoInt the estadoInt to set
     */
    public void setEstadoInt(int estadoInt) {
        this.estadoInt = estadoInt;
    }
    
}
