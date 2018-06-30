/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.modelo;

import java.math.BigDecimal;
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
    
    @Column(name="IVAINTERES")
    private BigDecimal ivaInteres;
    
    @Column(name="BONIFICACION")
    private BigDecimal bonificacion;
    
    @Column(name="IVABONIFICACION")
    private BigDecimal ivaBonificacion;
    
    @Column(name="NUMERO_TARJETA")
    private String numeroTarejta;
    
    @Column(name="NUMERO_CUPON")
    private String numeroCupon;
    
    @Column(name="TERMINAL")
    private String terminal;
    
    @Column(name="NUMERO_LOTE")
    private String numeroLote;
    
    @Column(name="DNI_CLIENTE")
    private String dniCliente;
    
    @Column(name="PORCENTAJE")
    private BigDecimal porcentaje;
    
    
    
    
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

    /**
     * @return the ivaInteres
     */
    public BigDecimal getIvaInteres() {
        return ivaInteres;
    }

    /**
     * @param ivaInteres the ivaInteres to set
     */
    public void setIvaInteres(BigDecimal ivaInteres) {
        this.ivaInteres = ivaInteres;
    }

    /**
     * @return the ivaBonificacion
     */
    public BigDecimal getIvaBonificacion() {
        return ivaBonificacion;
    }

    /**
     * @param ivaBonificacion the ivaBonificacion to set
     */
    public void setIvaBonificacion(BigDecimal ivaBonificacion) {
        this.ivaBonificacion = ivaBonificacion;
    }

    /**
     * @return the numeroTarejta
     */
    public String getNumeroTarejta() {
        return numeroTarejta;
    }

    /**
     * @param numeroTarejta the numeroTarejta to set
     */
    public void setNumeroTarejta(String numeroTarejta) {
        this.numeroTarejta = numeroTarejta;
    }

    /**
     * @return the numeroCupon
     */
    public String getNumeroCupon() {
        return numeroCupon;
    }

    /**
     * @param numeroCupon the numeroCupon to set
     */
    public void setNumeroCupon(String numeroCupon) {
        this.numeroCupon = numeroCupon;
    }

    /**
     * @return the terminal
     */
    public String getTerminal() {
        return terminal;
    }

    /**
     * @param terminal the terminal to set
     */
    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }

    /**
     * @return the dniCliente
     */
    public String getDniCliente() {
        return dniCliente;
    }

    /**
     * @param dniCliente the dniCliente to set
     */
    public void setDniCliente(String dniCliente) {
        this.dniCliente = dniCliente;
    }

    /**
     * @return the numeroLote
     */
    public String getNumeroLote() {
        return numeroLote;
    }

    /**
     * @param numeroLote the numeroLote to set
     */
    public void setNumeroLote(String numeroLote) {
        this.numeroLote = numeroLote;
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
