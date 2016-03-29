/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.modelo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author daniel
 */

@Entity
@Table(name="facturas")
public class Factura {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "idFACTURAS")
    private Long id;
    
    @Column(name = "FECHAALTA")
    private java.util.Date fechaAlta;
    
    @Column(name = "TIPOCOMPROBANTE")
    private String tipoComprobante;
    
    @Column(name = "NUMEROCOMPROBANTE")
    private String numeroComprobante;
    
    @Column(name = "TOTAL")
    private BigDecimal total;
    
    @Column(name = "NETO")
    private BigDecimal neto;
    
    @Column(name = "IVA")
    private BigDecimal iva;
    
    @Column(name = "IMPUESTOINTERNO")
    private BigDecimal impuestoInterno;
            
    @Column(name = "RETENCION")
    private BigDecimal retencion;
    
    @Column(name = "INTERESTARJETA")
    private BigDecimal interesTarjeta;
    
    @Column(name = "BONIFICATARJETA")
    private BigDecimal bonificaTarjeta;
    
    @Column(name = "IVABONIFTARJETA")
    private BigDecimal ivaBonificaTarjeta;
    
    @Column(name = "ANULADO", nullable = false, columnDefinition = "TINYINT(1)") 
    private boolean anulada;
    
    @OneToMany(cascade = CascadeType.ALL,mappedBy="factura")
    //@org.hibernate.annotations.IndexColumn(name = "BID_POSITION")
    private List<FacturaDetalle> detalle = new ArrayList<FacturaDetalle>();

    
    @ManyToOne
    @JoinColumn(name = "idClientes", referencedColumnName = "idClientes", nullable=true)
    private Cliente cliente;
    
    @ManyToOne
    @JoinColumn(name = "idUSUARIOALTA", referencedColumnName = "idUSUARIOS",nullable=false)
    private Usuario usuario;
    
    
    
    
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
     * @return the fechaAlta
     */
    public java.util.Date getFechaAlta() {
        return fechaAlta;
    }

    /**
     * @param fechaAlta the fechaAlta to set
     */
    public void setFechaAlta(java.util.Date fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    /**
     * @return the tipoComprobante
     */
    public String getTipoComprobante() {
        return tipoComprobante;
    }

    /**
     * @param tipoComprobante the tipoComprobante to set
     */
    public void setTipoComprobante(String tipoComprobante) {
        this.tipoComprobante = tipoComprobante;
    }

    /**
     * @return the numeroComprobante
     */
    public String getNumeroComprobante() {
        return numeroComprobante;
    }

    /**
     * @param numeroComprobante the numeroComprobante to set
     */
    public void setNumeroComprobante(String numeroComprobante) {
        this.numeroComprobante = numeroComprobante;
    }

    /**
     * @return the total
     */
    public BigDecimal getTotal() {
        return total;
    }

    /**
     * @param total the total to set
     */
    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    /**
     * @return the detalle
     */
    public List<FacturaDetalle> getDetalle() {
        return detalle;
    }

    /**
     * @return the cliente
     */
    public Cliente getCliente() {
        return cliente;
    }

    /**
     * @param cliente the cliente to set
     */
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
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

    
}
