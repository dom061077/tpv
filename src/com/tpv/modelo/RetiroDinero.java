/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.modelo;

import com.tpv.modelo.enums.RetiroDineroEnum;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.annotations.Formula;

/**
 *
 * @author COMPUTOS
 */

@Entity
@Table(name="retirodinero")
public class RetiroDinero {

    /**
     * @return the estado
     */
    public RetiroDineroEnum getEstado() {
        return estado;
    }

    /**
     * @param estado the estado to set
     */
    public void setEstado(RetiroDineroEnum estado) {
        this.estado = estado;
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "idRETIRODINERO")    
    private Long id;
    
    @Column(name = "FECHAHORAALTA")
    private java.util.Date fechaAlta;
    
    @Column(name = "FECHAHORARETIRO")
    private java.util.Date fechaRetiro;
    
    @Column(name = "ESTADO",nullable = false)
    @Enumerated(EnumType.STRING)
    private RetiroDineroEnum estado;
    
    @Column(name = "MONTOTOTAL")
    private BigDecimal monto;
    
    @Column(name = "OBSERVACION")
    private String observacion;
    
    @Formula("(SELECT NOW())")
    private java.sql.Date fechaHoy;    
    
    @ManyToOne
    @JoinColumn(name = "idUSUARIOS",referencedColumnName="idUSUARIOS", nullable=false)
    private Usuario usuario;
    
    @ManyToOne
    @JoinColumn(name = "idCHECKOUT",referencedColumnName="idCHECKOUT", nullable=false)
    private Checkout checkout;
    
    @OneToMany(cascade = CascadeType.ALL,mappedBy="retiro", fetch = FetchType.EAGER)
    private List<RetiroDineroDetalle> detalle = new ArrayList<RetiroDineroDetalle>();    

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
     * @return the fechaRetiro
     */
    public java.util.Date getFechaRetiro() {
        return fechaRetiro;
    }

    /**
     * @param fechaRetiro the fechaRetiro to set
     */
    public void setFechaRetiro(java.util.Date fechaRetiro) {
        this.fechaRetiro = fechaRetiro;
    }


    /**
     * @return the monto
     */
    public BigDecimal getMonto() {
        return monto;
    }

    /**
     * @param monto the monto to set
     */
    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    /**
     * @return the observacion
     */
    public String getObservacion() {
        return observacion;
    }

    /**
     * @param observacion the observacion to set
     */
    public void setObservacion(String observacion) {
        this.observacion = observacion;
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

    /**
     * @return the checkout
     */
    public Checkout getCheckout() {
        return checkout;
    }

    /**
     * @param checkout the checkout to set
     */
    public void setCheckout(Checkout checkout) {
        this.checkout = checkout;
    }

    /**
     * @return the detalle
     */
    public List<RetiroDineroDetalle> getDetalle() {
        return detalle;
    }

    /**
     * @param detalle the detalle to set
     */
    public void setDetalle(List<RetiroDineroDetalle> detalle) {
        this.detalle = detalle;
    }


    
    
}
