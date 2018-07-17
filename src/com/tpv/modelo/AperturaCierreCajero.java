/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.modelo;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author COMPUTOS
 */
@Entity
@Table(name="suctes_aperturacierre")
public class AperturaCierreCajero {
    @Id
    @Column(name="idsuctes_APERTURACIERRE")
    private Long id;
    
    @Column(name="FECHACAJA")
    private java.sql.Date fecha;
    
    @Column(name="APERTURA")
    private boolean abierta;
    
    @Column(name="CIERRE")
    private boolean cerrada;
    
    @Column(name="ESTADO")
    private int estado;
    
    @OneToMany(mappedBy="aperturaCierreCab", fetch = FetchType.EAGER)
    private List<AperturaCierreCajeroDetalle> detalle;

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
     * @return the fecha
     */
    public java.sql.Date getFecha() {
        return fecha;
    }

    /**
     * @param fecha the fecha to set
     */
    public void setFecha(java.sql.Date fecha) {
        this.fecha = fecha;
    }

    /**
     * @return the abierta
     */
    public boolean isAbierta() {
        return abierta;
    }

    /**
     * @param abierta the abierta to set
     */
    public void setAbierta(boolean abierta) {
        this.abierta = abierta;
    }

    /**
     * @return the cerrada
     */
    public boolean isCerrada() {
        return cerrada;
    }

    /**
     * @param cerrada the cerrada to set
     */
    public void setCerrada(boolean cerrada) {
        this.cerrada = cerrada;
    }

    /**
     * @return the estado
     */
    public int getEstado() {
        return estado;
    }

    /**
     * @param estado the estado to set
     */
    public void setEstado(int estado) {
        this.estado = estado;
    }

    /**
     * @return the detalle
     */
    public List<AperturaCierreCajeroDetalle> getDetalle() {
        return detalle;
    }

    /**
     * @param detalle the detalle to set
     */
    public void setDetalle(List<AperturaCierreCajeroDetalle> detalle) {
        this.detalle = detalle;
    }
    
}
