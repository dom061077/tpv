/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.modelo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.Formula;

/**
 *
 * @author daniel
 */

@Entity
@Table(name="combos")
public class Combo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "idCOMBOS")
    private Long id;

    @Column(name = "DESCRIPCION")
    private String descripcion;
    
    @Column(name = "FECHADESDE")
    private java.sql.Date fechaDesde;
            
    @Column(name = "FECHAHASTA")
    private java.sql.Date fechaHasta;
            
    @Formula("(SELECT current_date())")
    private java.sql.Date fechaHoy;

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
     * @return the descripcion
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * @param descripcion the descripcion to set
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * @return the fechaDesde
     */
    public java.sql.Date getFechaDesde() {
        return fechaDesde;
    }

    /**
     * @param fechaDesde the fechaDesde to set
     */
    public void setFechaDesde(java.sql.Date fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    /**
     * @return the fechaHasta
     */
    public java.sql.Date getFechaHasta() {
        return fechaHasta;
    }

    /**
     * @param fechaHasta the fechaHasta to set
     */
    public void setFechaHasta(java.sql.Date fechaHasta) {
        this.fechaHasta = fechaHasta;
    }

    /**
     * @return the fechaHoy
     */
    public java.sql.Date getFechaHoy() {
        return fechaHoy;
    }

    /**
     * @param fechaHoy the fechaHoy to set
     */
    public void setFechaHoy(java.sql.Date fechaHoy) {
        this.fechaHoy = fechaHoy;
    }
    
    
    
    
}
