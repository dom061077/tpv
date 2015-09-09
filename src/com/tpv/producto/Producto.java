/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.producto;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 *
 * @author daniel
 */
@Entity
public class Producto {
    @Id
    private int idProducto;
    private String codBarra;
    private int stock;
    private String detalleSucursales;
    private int codigoProducto;

    /**
     * @return the idProducto
     */
    public int getIdProducto() {
        return idProducto;
    }

    /**
     * @param idProducto the idProducto to set
     */
    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    /**
     * @return the codBarra
     */
    public String getCodBarra() {
        return codBarra;
    }

    /**
     * @param codBarra the codBarra to set
     */
    public void setCodBarra(String codBarra) {
        this.codBarra = codBarra;
    }

    /**
     * @return the stock
     */
    public int getStock() {
        return stock;
    }

    /**
     * @param stock the stock to set
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     * @return the detalleSucursales
     */
    public String getDetalleSucursales() {
        return detalleSucursales;
    }

    /**
     * @param detalleSucursales the detalleSucursales to set
     */
    public void setDetalleSucursales(String detalleSucursales) {
        this.detalleSucursales = detalleSucursales;
    }

    /**
     * @return the codigoProducto
     */
    public int getCodigoProducto() {
        return codigoProducto;
    }

    /**
     * @param codigoProducto the codigoProducto to set
     */
    public void setCodigoProducto(int codigoProducto) {
        this.codigoProducto = codigoProducto;
    }
         
}
