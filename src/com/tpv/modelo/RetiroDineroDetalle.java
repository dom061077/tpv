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
 * @author COMPUTOS
 */

@Entity
@Table(name="retirodinerodetalle")
public class RetiroDineroDetalle {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "idRETIRODINERODETALLE")    
    private Long id;
    
    @Column(name = "MONTO")
    private BigDecimal monto;
    
    @Column(name="CANTIDADBILLETES")
    private int cantidadBilletes;
    
    @ManyToOne
    @JoinColumn(name = "idsuctes_BILLETE",referencedColumnName="idsuctes_BILLETE", nullable=false)
    private Billete billete;
    
    @ManyToOne
    @JoinColumn(name = "idRETIRODINERO", referencedColumnName="idRETIRODINERO", nullable=false)
    private RetiroDinero retiro;

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
     * @return the cantidadBilletes
     */
    public int getCantidadBilletes() {
        return cantidadBilletes;
    }

    /**
     * @param cantidadBilletes the cantidadBilletes to set
     */
    public void setCantidadBilletes(int cantidadBilletes) {
        this.cantidadBilletes = cantidadBilletes;
    }

    /**
     * @return the billete
     */
    public Billete getBillete() {
        return billete;
    }

    /**
     * @param billete the billete to set
     */
    public void setBillete(Billete billete) {
        this.billete = billete;
    }

    /**
     * @return the retiro
     */
    public RetiroDinero getRetiro() {
        return retiro;
    }

    /**
     * @param retiro the retiro to set
     */
    public void setRetiro(RetiroDinero retiro) {
        this.retiro = retiro;
    }
    
    
    
}
