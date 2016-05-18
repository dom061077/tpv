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
public enum ComboPrioridadEnum {
    PRIORIDAD_ALTA("Prioridad Alta"),
    PRIORIDAD_MEDIA("Prioridad Media"),
    PRIORIDAD_BAJA("Prioridad Baja");
    
    private String prioridadStr;
    
    private ComboPrioridadEnum(String prioridadStr){
        this.prioridadStr = prioridadStr;
    }

    /**
     * @return the prioridadStr
     */
    public String getPrioridadStr() {
        return prioridadStr;
    }

    /**
     * @param prioridadStr the prioridadStr to set
     */
    public void setPrioridadStr(String prioridadStr) {
        this.prioridadStr = prioridadStr;
    }

}
