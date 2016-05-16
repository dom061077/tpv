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
    
    private BigDecimal precioProducto;
    
    private Producto producto;
    
    private FacturaDetalle factDetalle;
    
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
     * @return the precioProducto
     */
    public BigDecimal getPrecioProducto() {
        return precioProducto;
    }

    /**
     * @param precioProducto the precioProducto to set
     */
    public void setPrecioProducto(BigDecimal precioProducto) {
        this.precioProducto = precioProducto;
    }

    /**
     * @return the producto
     */
    public Producto getProducto() {
        return producto;
    }

    /**
     * @param producto the producto to set
     */
    public void setProducto(Producto producto) {
        this.producto = producto;
    }
    
    public BigDecimal getSubTotal(){
        BigDecimal value = new BigDecimal(getComboGrupo().getCombo().getCantidadCombosArmados());
        return getPrecioProducto().multiply(new BigDecimal(cantidad));
    }
    
    public BigDecimal getBonificacionPorPrecioProducto(){
        return getPrecioProducto().multiply(getComboGrupo().getPorcentaje()).divide(new BigDecimal(100));
    }
    
    
    public void recuperarCantFacDetlleSinCombo(int cantRecuperada){
        factDetalle.decrementarCantidadAuxCombo(cantidad);
    }

    /**
     * @return the factDetalle
     */
    public FacturaDetalle getFactDetalle() {
        return factDetalle;
    }

    /**
     * @param factDetalle the factDetalle to set
     */
    public void setFactDetalle(FacturaDetalle factDetalle) {
        this.factDetalle = factDetalle;
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
        BigDecimal bonif = precioProducto;
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
    
}
