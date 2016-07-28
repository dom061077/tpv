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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.annotations.Formula;

/**
 *
 * @author daniel
 */
@Entity
@Table(name="interestarjeta")
public class InteresTarjeta {
    @Id
    @Column(name="idINTERESTARJETA")
    private int id;
    
    @Column(name="CUOTA")
    private int cuota;
    
    @Column(name="INTERESTARJETA")
    private BigDecimal interesTarjeta;
    
    @Column(name="DESDEINTTARJETA")
    private java.sql.Date desdeIntTarjeta;
    
    @Column(name="HASTAINTTARJETA")
    private java.sql.Date hastaIntTarjeta;
    
    @Column(name="BONIFICATARJETA")
    private BigDecimal bonificaTarjeta;
    
    @Column(name="DESDEBONTARJETA")
    private java.sql.Date desdeBonTarjeta;

    @Column(name="HASTABONTARJETA")
    private java.sql.Date hastaBonTarjeta;
    
    @ManyToOne
    @JoinColumn(name = "idFORMAPAGO"
            ,referencedColumnName="idFORMAPAGO",nullable=false)
    private FormaPago formaPago;

    @Formula("(SELECT current_date())")
    private java.sql.Date fechaHoy;
    
    

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
     * @return the cuota
     */
    public int getCuota() {
        return cuota;
    }

    /**
     * @param cuota the cuota to set
     */
    public void setCuota(int cuota) {
        this.cuota = cuota;
    }

    /**
     * @return the interesTarjeta
     */
    public BigDecimal getInteresTarjeta() {
        return interesTarjeta;
    }

    /**
     * @param interesTarjeta the interesTarjeta to set
     */
    public void setInteresTarjeta(BigDecimal interesTarjeta) {
        this.interesTarjeta = interesTarjeta;
    }

    /**
     * @return the desdeIntTarjeta
     */
    public java.sql.Date getDesdeIntTarjeta() {
        return desdeIntTarjeta;
    }

    /**
     * @param desdeIntTarjeta the desdeIntTarjeta to set
     */
    public void setDesdeIntTarjeta(java.sql.Date desdeIntTarjeta) {
        this.desdeIntTarjeta = desdeIntTarjeta;
    }

    /**
     * @return the hastaIntTarjeta
     */
    public java.sql.Date getHastaIntTarjeta() {
        return hastaIntTarjeta;
    }

    /**
     * @param hastaIntTarjeta the hastaIntTarjeta to set
     */
    public void setHastaIntTarjeta(java.sql.Date hastaIntTarjeta) {
        this.hastaIntTarjeta = hastaIntTarjeta;
    }

    /**
     * @return the bonificaTarjeta
     */
    public BigDecimal getBonificaTarjeta() {
        return bonificaTarjeta;
    }

    /**
     * @param bonificaTarjeta the bonificaTarjeta to set
     */
    public void setBonificaTarjeta(BigDecimal bonificaTarjeta) {
        this.bonificaTarjeta = bonificaTarjeta;
    }

    /**
     * @return the desdeBonTarjeta
     */
    public java.sql.Date getDesdeBonTarjeta() {
        return desdeBonTarjeta;
    }

    /**
     * @param desdeBonTarjeta the desdeBonTarjeta to set
     */
    public void setDesdeBonTarjeta(java.sql.Date desdeBonTarjeta) {
        this.desdeBonTarjeta = desdeBonTarjeta;
    }

    /**
     * @return the hastaBonTarjeta
     */
    public java.sql.Date getHastaBonTarjeta() {
        return hastaBonTarjeta;
    }

    /**
     * @param hastaBonTarjeta the hastaBonTarjeta to set
     */
    public void setHastaBonTarjeta(java.sql.Date hastaBonTarjeta) {
        this.hastaBonTarjeta = hastaBonTarjeta;
    }

    /**
     * @return the formaPago
     */
    public FormaPago getFormaPago() {
        return formaPago;
    }

    /**
     * @param formaPago the formaPago to set
     */
    public void setFormaPago(FormaPago formaPago) {
        this.formaPago = formaPago;
    }
    
    
    @Transient
    public BigDecimal getInteres(int cantCuotas){
        BigDecimal interes=BigDecimal.ZERO;
        if(desdeIntTarjeta.compareTo(fechaHoy)<=0 &&
            hastaIntTarjeta.compareTo(fechaHoy)>=0 &&
            cuota == cantCuotas){
            interes = interesTarjeta;
        }
        return interes;
    } 
    
    @Transient
    public BigDecimal getBonificacion(int cantCuotas){
        BigDecimal bonificacion=BigDecimal.ZERO;
        if(desdeBonTarjeta.compareTo(fechaHoy)<=0 &&
            hastaBonTarjeta.compareTo(fechaHoy)>=0 &&
            cuota == cantCuotas){
            bonificacion = bonificaTarjeta;
        }
        return bonificacion;
    }
    
    
    
}
