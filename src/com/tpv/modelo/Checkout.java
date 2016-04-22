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
@Table(name="checkout")
public class Checkout {
    @Id
    @Column(name = "idCHECKOUT")
    private int id;
    
    @Column(name = "DETALLE")
    private String detalle;
    
    @Column(name = "PLACA")
    private String placa;
    
    @Column(name = "POSNET")
    private int posnet;
    
    @Column(name = "HABILITADO", columnDefinition = "TINYINT(1)")
    private boolean habitado;

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
     * @return the placa
     */
    public String getPlaca() {
        return placa;
    }

    /**
     * @param placa the placa to set
     */
    public void setPlaca(String placa) {
        this.placa = placa;
    }

    /**
     * @return the posnet
     */
    public int getPosnet() {
        return posnet;
    }

    /**
     * @param posnet the posnet to set
     */
    public void setPosnet(int posnet) {
        this.posnet = posnet;
    }

    /**
     * @return the habitado
     */
    public boolean isHabitado() {
        return habitado;
    }

    /**
     * @param habitado the habitado to set
     */
    public void setHabitado(boolean habitado) {
        this.habitado = habitado;
    }
    
}
