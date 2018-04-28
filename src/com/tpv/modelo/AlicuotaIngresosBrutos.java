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
import javax.persistence.Table;

/**
 *
 * @author COMPUTOS
 */

@Entity
@Table(name="alicuotaretibtuc")
public class AlicuotaIngresosBrutos {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "idALICUOTARETIBTUC")
    private Long id;  
    
    @Column(name = "CUIT")
    private String cuit;
    
    @Column(name = "EXENTO")    
    private String exento;

    @Column(name = "CONVENIO")    
    private String convenio;
    
    @Column(name = "PORCENTAJE")    
    private BigDecimal porcentaje;

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
     * @return the cuit
     */
    public String getCuit() {
        return cuit;
    }

    /**
     * @param cuit the cuit to set
     */
    public void setCuit(String cuit) {
        this.cuit = cuit;
    }

    /**
     * @return the exento
     */
    public String getExento() {
        return exento;
    }

    /**
     * @param exento the exento to set
     */
    public void setExento(String exento) {
        this.exento = exento;
    }

    /**
     * @return the convenio
     */
    public String getConvenio() {
        return convenio;
    }

    /**
     * @param convenio the convenio to set
     */
    public void setConvenio(String convenio) {
        this.convenio = convenio;
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
