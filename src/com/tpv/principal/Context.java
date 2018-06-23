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
    private final static Context instance = new Context();
    
    public static Context getInstance(){
        return instance;
    }
    
    private DataModelTicket dataModelTicket = new DataModelTicket();
    private DataModelParametroGral parametroGral = new DataModelParametroGral();
    
    public DataModelTicket currentDMTicket(){
        return getDataModelTicket();
    }
    
    public DataModelParametroGral currentDMParametroGral(){
        return getDataModelParametroGral();
    }

    public DataModelParametroGral getDataModelParametroGral(){
        return parametroGral;
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
