/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.modelo;

import java.math.BigDecimal;
import java.util.Iterator;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author daniel
 */
@Entity
@Table(name="facturasformapagodetalle")
public class FacturaFormaPagoDetalle {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="idFACTURASFORMAPAGODETALLE")
    private Long id;
    
    @Column(name="MONTOPAGO")
    private BigDecimal montoPago;
    
    @Column(name="INTERES")
    private BigDecimal interes;
    
    @Column(name="BONIFICACION")
    private BigDecimal bonificacion;
    
    @Column(name="CUOTA")
    private int cuota;
    
    @ManyToOne
    @JoinColumn(name = "idFORMAPAGO", referencedColumnName = "idFORMAPAGO", nullable=false)
    private FormaPago formaPago;
    
    @ManyToOne
    @JoinColumn(name = "idFACTURAS", referencedColumnName = "idFACTURAS", nullable=false)
    private Factura factura;

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
     * @return the montoPago
     */
    public BigDecimal getMontoPago() {
        return montoPago;
    }

    /**
     * @param montoPago the montoPago to set
     */
    public void setMontoPago(BigDecimal montoPago) {
        this.montoPago = montoPago;
    }

    /**
     * @return the interes
     */
    public BigDecimal getInteres() {
        return interes;
    }

    /**
     * @param interes the interes to set
     */
    public void setInteres(BigDecimal interes) {
        this.interes = interes;
    }

    /**
     * @return the bonificacion
     */
    public BigDecimal getBonificacion() {
        return bonificacion;
    }

    /**
     * @param bonificacion the bonificacion to set
     */
    public void setBonificacion(BigDecimal bonificacion) {
        this.bonificacion = bonificacion;
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

    /**
     * @return the factura
     */
    public Factura getFactura() {
        return factura;
    }

    /**
     * @param factura the factura to set
     */
    public void setFactura(Factura factura) {
        this.factura = factura;
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
    
    
}
