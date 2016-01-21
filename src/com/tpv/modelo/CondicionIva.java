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
@Table(name="condicionesiva")
public class CondicionIva {
    @Id
    @Column(name="idCONDICIONESIVA")
    private int id;
    
    @Column(name="DESCRIPCION")
    private String descripcion;
    @Column(name="ALICUOTA")
    private BigDecimal alicuota;
    @Column(name="ALICUOTARECARGADA")
    private BigDecimal alicuotaRecargada;

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
     * @return the descripcion
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * @param descripcion the descripcion to set
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * @return the alicuota
     */
    public BigDecimal getAlicuota() {
        return alicuota;
    }

    /**
     * @param alicuota the alicuota to set
     */
    public void setAlicuota(BigDecimal alicuota) {
        this.alicuota = alicuota;
    }

    /**
     * @return the alicuotaRecargada
     */
    public BigDecimal getAlicuotaRecargada() {
        return alicuotaRecargada;
    }

    /**
     * @param alicuotaRecargada the alicuotaRecargada to set
     */
    public void setAlicuotaRecargada(BigDecimal alicuotaRecargada) {
        this.alicuotaRecargada = alicuotaRecargada;
    }
    
    
}
