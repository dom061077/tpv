/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.modelo;

import java.math.BigDecimal;

/**
 *
 * @author daniel
 */
public class ProductoAgrupadoEnFactura {
    private Producto producto;
    private BigDecimal precioUnitario;
    private BigDecimal precioUnitarioBase;
    private int cantidad;
    private int cantidadAux;

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

    /**
     * @return the precioUnitario
     */
    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    /**
     * @param precioUnitario the precioUnitario to set
     */
    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }
    
    public void incCantidad(int inc){
        cantidad+=inc;
    }
    
    public void decCantidad(int dec){
        cantidad-=dec;
    }
    

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
     * @return the cantidadAux
     */
    public int getCantidadAux() {
        return cantidadAux;
    }
    
    public void resetCantidadAux(){
        cantidadAux = cantidad;
    }
    
    public void decCantidadAux(int dec){
        cantidadAux-=dec;
    }

    /**
     * @return the precioUnitarioBase
     */
    public BigDecimal getPrecioUnitarioBase() {
        return precioUnitarioBase;
    }

    /**
     * @param precioUnitarioBase the precioUnitarioBase to set
     */
    public void setPrecioUnitarioBase(BigDecimal precioUnitarioBase) {
        this.precioUnitarioBase = precioUnitarioBase;
    }
    
}
