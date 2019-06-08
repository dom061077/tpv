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

    public static void clearCurrentDMTicket(){
        Context.getInstance().currentDMTicket().setIdDocumento(null);
        Context.getInstance().currentDMTicket().setCliente(null);
        Context.getInstance().currentDMTicket().setClienteSeleccionado(false);
        Context.getInstance().currentDMTicket().getDetalle().clear();
        Context.getInstance().currentDMTicket().getPagos().clear();
        Context.getInstance().currentDMTicket().setRetencion(BigDecimal.ZERO);
        Context.getInstance().currentDMTicket().setBonificaciones(BigDecimal.ZERO);
        Context.getInstance().currentDMTicket().setTotalIva(BigDecimal.ZERO);
        Context.getInstance().currentDMTicket().setTotalImpuestoInterno(BigDecimal.ZERO);
        Context.getInstance().currentDMTicket().setImprimeComoNegativo(false);
    }
    

    
    
    
    
}
