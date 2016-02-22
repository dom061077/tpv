/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.modelo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author daniel
 */
@Entity
@Table(name="formapago")
public class FormaPago {
    @Id
    @Column(name = "idFORMAPAGO")
    private int id;
    
    @Column(name = "DETALLE")
    private String detalle;
    
    @Column(name="MAXIMOCUOTAS")
    private int maxiCuotas;

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the detalle
     */
    public String getDetalle() {
        return detalle;
    }

    /**
     * @param detalle the detalle to set
     */
    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    /**
     * @return the maxiCuotas
     */
    public int getMaxiCuotas() {
        return maxiCuotas;
    }

    /**
     * @param maxiCuotas the maxiCuotas to set
     */
    public void setMaxiCuotas(int maxiCuotas) {
        this.maxiCuotas = maxiCuotas;
    }
    
}
