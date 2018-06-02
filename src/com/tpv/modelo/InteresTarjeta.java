/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.modelo;

import com.tpv.modelo.enums.InteresBonifTarjetaEnum;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
    
    @Column(name="PORCENTAJE")
    private BigDecimal porcentaje;
    
    @Column(name = "TIPO",nullable = false)
    @Enumerated(EnumType.STRING)
    private InteresBonifTarjetaEnum tipo;
    
    @Column(name = "VIGENCIADESDE", nullable = false)
    private java.sql.Date vigenciaDesde;
    
    @Column(name = "VIGENCIAHASTA", nullable = false)
    private java.sql.Date vigenciaHasta;


    
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
    public BigDecimal getPorcentajeInteres(int cantCuotas){
        BigDecimal interes=BigDecimal.ZERO;
        if(tipo == InteresBonifTarjetaEnum.INTERES){
            if(vigenciaDesde.compareTo(fechaHoy)<=0 &&
                vigenciaHasta.compareTo(fechaHoy)>=0 &&
                cuota == cantCuotas){
                interes = getPorcentaje();
            }
        }
        return interes;
    } 
    
    public BigDecimal getMontoInteres(BigDecimal monto){
        BigDecimal interes = BigDecimal.ZERO;
        if(tipo == InteresBonifTarjetaEnum.INTERES){
            if(vigenciaDesde.compareTo(fechaHoy)<=0 &&
                vigenciaHasta.compareTo(fechaHoy)>=0){
                interes = interes.multiply(getPorcentaje()).divide(BigDecimal.valueOf(100));
            }
        }
        return interes;
    }
    
    @Transient
    public BigDecimal getPorcentajeBonificacion(int cantCuotas){
        BigDecimal bonificacion=BigDecimal.ZERO;
        if(tipo == InteresBonifTarjetaEnum.BONIFICACION){
            if(vigenciaDesde.compareTo(fechaHoy)<=0 &&
                vigenciaHasta.compareTo(fechaHoy)>=0 &&
                cuota == cantCuotas){
                bonificacion = getPorcentaje();
            }
        }
        return bonificacion;
    }
    
    public BigDecimal getMontoBonificacion(BigDecimal monto){
        BigDecimal bonificacion = BigDecimal.ZERO;
                if(tipo == InteresBonifTarjetaEnum.BONIFICACION){
            if(vigenciaDesde.compareTo(fechaHoy)<=0 &&
                vigenciaHasta.compareTo(fechaHoy)>=0){
                bonificacion = monto.multiply(getPorcentaje()).divide(BigDecimal.valueOf(100));
            }
        }

        return bonificacion;
    }
    
    public BigDecimal getMonto(BigDecimal monto){
        BigDecimal montoResultante = BigDecimal.ZERO;
            if(vigenciaDesde.compareTo(fechaHoy)<=0 &&
                vigenciaHasta.compareTo(fechaHoy)>=0){
                montoResultante = monto.multiply(getPorcentaje()).divide(BigDecimal.valueOf(100));
        }

        return montoResultante.setScale(2,BigDecimal.ROUND_HALF_EVEN);
    }

    /**
     * @return the tipo
     */
    public InteresBonifTarjetaEnum getTipo() {
        return tipo;
    }

    /**
     * @param tipo the tipo to set
     */
    public void setTipo(InteresBonifTarjetaEnum tipo) {
        this.tipo = tipo;
    }

    /**
     * @return the vigenciaDesde
     */
    public java.sql.Date getVigenciaDesde() {
        return vigenciaDesde;
    }

    /**
     * @param vigenciaDesde the vigenciaDesde to set
     */
    public void setVigenciaDesde(java.sql.Date vigenciaDesde) {
        this.vigenciaDesde = vigenciaDesde;
    }

    /**
     * @return the vigenciaHasta
     */
    public java.sql.Date getVigenciaHasta() {
        return vigenciaHasta;
    }

    /**
     * @param vigenciaHasta the vigenciaHasta to set
     */
    public void setVigenciaHasta(java.sql.Date vigenciaHasta) {
        this.vigenciaHasta = vigenciaHasta;
    }

    /**
     * @return the porcentaje
     */
    public BigDecimal getPorcentaje() {
        return porcentaje;
    }

    /**
     * @param porcentaje the porcentaje to set
     */
    public void setPorcentaje(BigDecimal porcentaje) {
        this.porcentaje = porcentaje;
    }
    
    
    
}
