/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.modelo;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.annotations.Formula;

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
    
    @Column(name="TIENECUPON")
    private boolean tieneCupon;
    
    @Column(name="TIENEVUELTO")
    private boolean tieneVuelto;
    
    @Formula("(SELECT current_date())")
    private java.util.Date fechaHoy;
    


    @OneToMany(fetch = FetchType.EAGER,mappedBy = "formaPago")
    private List<InteresTarjeta> interesesTarjeta;
    
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

    /**
     * @return the interesesTarjeta
     */
    public List<InteresTarjeta> getInteresesTarjeta() {
        //return interesesTarjeta;
        Collections.sort(interesesTarjeta,(o1,o2)->
            o1.getCuota()-o2.getCuota()
        );
        return interesesTarjeta;
        
    }

    /**
     * @param interesesTarjeta the interesesTarjeta to set
     */
    public void setInteresesTarjeta(List<InteresTarjeta> interesesTarjeta) {
        this.interesesTarjeta = interesesTarjeta;
    }
    
    public BigDecimal getBonificacionEnFormaPago(int cantCuotas,BigDecimal monto){
        BigDecimal bonificaciones = BigDecimal.ZERO;
        BigDecimal bonifAux = BigDecimal.ZERO;
        for(Iterator<InteresTarjeta> it = interesesTarjeta.iterator();it.hasNext();){
            InteresTarjeta intTarj = it.next();
            bonifAux = monto.multiply(intTarj.getPorcentajeBonificacion(cantCuotas))
                    .divide(BigDecimal.valueOf(100));
            bonificaciones=bonificaciones.add(bonifAux);
        }
        bonificaciones = bonificaciones.setScale(2,BigDecimal.ROUND_HALF_UP);
        return bonificaciones;
    }
    
    public BigDecimal getInteresEnFormaPago(int cantCuotas,BigDecimal monto){
        BigDecimal intereses = BigDecimal.ZERO;
        BigDecimal interesAux = BigDecimal.ZERO;
        for(Iterator<InteresTarjeta> it = interesesTarjeta.iterator();it.hasNext();){
            InteresTarjeta intTarj = it.next();
            interesAux = monto.multiply(intTarj.getPorcentajeInteres(cantCuotas))
                        .divide(BigDecimal.valueOf(100));
            intereses=intereses.add(interesAux);
        }
        intereses = intereses.setScale(2,BigDecimal.ROUND_HALF_UP);
        return intereses;
    }

    /**
     * @return the tieneCupon
     */
    public boolean isTieneCupon() {
        return tieneCupon;
    }

    /**
     * @param tieneCupon the tieneCupon to set
     */
    public void setTieneCupon(boolean tieneCupon) {
        this.tieneCupon = tieneCupon;
    }

    /**
     * @return the tieneVuelto
     */
    public boolean isTieneVuelto() {
        return tieneVuelto;
    }

    /**
     * @param tieneVuelto the tieneVuelto to set
     */
    public void setTieneVuelto(boolean tieneVuelto) {
        this.tieneVuelto = tieneVuelto;
    }

    /**
     * @return the fechaHoy
     */
    public java.util.Date getFechaHoy() {
        return fechaHoy;
    }
    
    
    
}
