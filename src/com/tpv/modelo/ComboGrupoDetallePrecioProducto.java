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
    private BigDecimal cantidad;
    
    private BigDecimal cantidadAux;
    
    private ProductoAgrupadoEnFactura paf;
    
    private ComboGrupo comboGrupo;

    /**
     * @return the cantidad
     */
    public BigDecimal getCantidad() {
        return cantidad;
    }

    /**
     * @param cantidad the cantidad to set
     */
    public void setCantidad(BigDecimal cantidad) {
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
        BigDecimal cantReferencia = cantidad.divide(comboGrupo.getCantidad());
        cantReferencia = cantReferencia.multiply(comboGrupo.getCantidad());
        bonif = bonif.multiply(cantReferencia);
        bonif = bonif.multiply(comboGrupo.getPorcentaje()).divide(BigDecimal.valueOf(100));
        return bonif;
    }
    
    public void initCantidadAux(){
        setCantidadAux(cantidad);
    }
    
    public void decrementarCantAux(BigDecimal decremento){
        this.setCantidadAux(getCantidadAux().subtract(decremento));
    }

    /**
     * @return the cantidadAux
     */
    public BigDecimal getCantidadAux() {
        return cantidadAux;
    }

    /**
     * @param cantidadAux the cantidadAux to set
     */
    public void setCantidadAux(BigDecimal cantidadAux) {
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
