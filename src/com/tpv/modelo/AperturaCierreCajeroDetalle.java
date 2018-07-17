/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.modelo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author daniel
 */

@Entity
@Table(name="suctes_aperturacierrecajero")
public class AperturaCierreCajeroDetalle {
    @Id
    @Column(name ="idsuctes_APERTURACIERRECAJERO" )
    private Long id;
    
    @Column(name = "ESTADO")
    private boolean estado;
    
    @Column(name = "TURNO")   
    private int caja;
    
    @ManyToOne
    @JoinColumn(name = "idCHECKOUT", referencedColumnName = "idCHECKOUT")
    private Checkout checkout;
    
    @ManyToOne
    @JoinColumn(name = "idsuctes_CAJERO", referencedColumnName="idUSUARIOS")
    private Usuario usuario;
    
    @ManyToOne
    @JoinColumn(name = "idsuctes_APERTURACIERRE", referencedColumnName="")
    private AperturaCierreCajero aperturaCierreCab;

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the estado
     */
    public boolean isEstado() {
        return estado;
    }

    /**
     * @param estado the estado to set
     */
    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    /**
     * @return the caja
     */
    public int getCaja() {
        return caja;
    }

    /**
     * @param caja the caja to set
     */
    public void setCaja(int caja) {
        this.caja = caja;
    }

    /**
     * @return the checkout
     */
    public Checkout getCheckout() {
        return checkout;
    }

    /**
     * @param checkout the checkout to set
     */
    public void setCheckout(Checkout checkout) {
        this.checkout = checkout;
    }

    /**
     * @return the usuario
     */
    public Usuario getUsuario() {
        return usuario;
    }

    /**
     * @param usuario the usuario to set
     */
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    /**
     * @return the aperturaCierreCab
     */
    public AperturaCierreCajero getAperturaCierreCab() {
        return aperturaCierreCab;
    }

    /**
     * @param aperturaCierreCab the aperturaCierreCab to set
     */
    public void setAperturaCierreCab(AperturaCierreCajero aperturaCierreCab) {
        this.aperturaCierreCab = aperturaCierreCab;
    }
    
}
