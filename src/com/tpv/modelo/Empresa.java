/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.modelo;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author daniel
 */

@Entity
@Table(name="empresa")
public class Empresa {
    @Id
    @Column(name = "idEMPRESA")
    private int id;
    
    @Column(name = "RAZONSOCIAL")
    private String razonSocial;
    
    @Column(name = "TOPEDESCUENTO")
    private BigDecimal topeDescuento;
    
    @Column(name = "PORCDESCUENTO")
    private BigDecimal porcentajeDescuento;
    
    @Column(name = "PORCESPECIAL")
    private BigDecimal porcentajeEspecial;
    
    @Column(name = "ESTADO", columnDefinition = "TINYINT(1)") 
    private BigDecimal estado;

    @Column(name = "CONDICION")
    private BigDecimal condicion;

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
     * @return the razonSocial
     */
    public String getRazonSocial() {
        return razonSocial;
    }

    /**
     * @param razonSocial the razonSocial to set
     */
    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    /**
     * @return the topeDescuento
     */
    public BigDecimal getTopeDescuento() {
        return topeDescuento;
    }

    /**
     * @param topeDescuento the topeDescuento to set
     */
    public void setTopeDescuento(BigDecimal topeDescuento) {
        this.topeDescuento = topeDescuento;
    }

    /**
     * @return the porcentajeDescuento
     */
    public BigDecimal getPorcentajeDescuento() {
        return porcentajeDescuento;
    }

    /**
     * @param porcentajeDescuento the porcentajeDescuento to set
     */
    public void setPorcentajeDescuento(BigDecimal porcentajeDescuento) {
        this.porcentajeDescuento = porcentajeDescuento;
    }

    /**
     * @return the porcentajeEspecial
     */
    public BigDecimal getPorcentajeEspecial() {
        return porcentajeEspecial;
    }

    /**
     * @param porcentajeEspecial the porcentajeEspecial to set
     */
    public void setPorcentajeEspecial(BigDecimal porcentajeEspecial) {
        this.porcentajeEspecial = porcentajeEspecial;
    }

    /**
     * @return the estado
     */
    public BigDecimal getEstado() {
        return estado;
    }

    /**
     * @param estado the estado to set
     */
    public void setEstado(BigDecimal estado) {
        this.estado = estado;
    }

    /**
     * @return the condicion
     */
    public BigDecimal getCondicion() {
        return condicion;
    }

    /**
     * @param condicion the condicion to set
     */
    public void setCondicion(BigDecimal condicion) {
        this.condicion = condicion;
    }
    
    
    
    
}
