/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.modelo;

import java.math.BigDecimal;
import javax.persistence.Transient;

/**
 *
 * @author daniel
 */
public class ComboGrupoDetallePrecioProducto {
    private int cantidad;
    
    private int cantidadAux;
    
    private ProductoAgrupadoEnFactura paf;
    
    private ComboGrupo comboGrupo;

    /**
     * @return the cantidad
     */
    public int getCantidad() {
        return cantidad;
    }

    /**
     * @param cantidad the cantidad to set
     */
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }


    
    
    


    /**
     * @return the comboGrupo
     */
    public ComboGrupo getComboGrupo() {
        return comboGrupo;
    }

    /**
     * @param comboGrupo the comboGrupo to set
     */
    public void setComboGrupo(ComboGrupo comboGrupo) {
        this.comboGrupo = comboGrupo;
    }

    public BigDecimal getBonificacion(){
        BigDecimal bonif =  paf.getPrecioUnitario();
        int cantReferencia = cantidad / comboGrupo.getCantidad();
        cantReferencia = cantReferencia * comboGrupo.getCantidad();
        bonif = bonif.multiply(BigDecimal.valueOf(cantReferencia));
        bonif = bonif.multiply(comboGrupo.getPorcentaje()).divide(BigDecimal.valueOf(100));
        return bonif;
    }
    
    public void initCantidadAux(){
        setCantidadAux(cantidad);
    }
    
    public void decrementarCantAux(int decremento){
        this.setCantidadAux(getCantidadAux() - decremento);
    }

    /**
     * @return the cantidadAux
     */
    public int getCantidadAux() {
        return cantidadAux;
    }

    /**
     * @param cantidadAux the cantidadAux to set
     */
    public void setCantidadAux(int cantidadAux) {
        this.cantidadAux = cantidadAux;
    }

    /**
     * @return the paf
     */
    public ProductoAgrupadoEnFactura getPaf() {
        return paf;
    }

    /**
     * @param paf the paf to set
     */
    public void setPaf(ProductoAgrupadoEnFactura paf) {
        this.paf = paf;
    }
    
}
