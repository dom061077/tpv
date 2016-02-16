/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.modelo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
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
    @Column(name = "idFACTURAS")
    private int id;
    
    @Column(name = "FECHAALTA")
    private java.util.Date fechaAlta;
    
    @Column(name = "TIPOCOMPROBANTE")
    private String tipoComprobante;
    
    @Column(name = "NUMEROCOMPROBANTE")
    private String numeroComprobante;
    
    @Column(name = "TOTAL")
    private BigDecimal total;
    
    @OneToMany
    @JoinColumn(name = "idFACTURAS", nullable = false)
    @org.hibernate.annotations.IndexColumn(name = "BID_POSITION")
    private List<FacturaDetalle> detalle = new ArrayList<>();

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

    
}
