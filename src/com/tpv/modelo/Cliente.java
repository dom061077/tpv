/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.modelo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author daniel
 */

@Entity
@Table(name="clientes")
public class Cliente { 
    @Id
    @Column(name = "idCLIENTES")
    private int id;
    @Column(name = "RAZONSOCIAL")
    private String razonSocial;
    
    @Column(name = "DNI")
    private int dni;
    
    @Column(name = "CUIT")
    private String cuit;
    
    @ManyToOne
    @JoinColumn(name = "idCONDICIONESIVA", referencedColumnName = "idCONDICIONESIVA", nullable=true)
    private CondicionIva condicionIva;
    
    
    
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
     * @return the razonSocial
     */
    public String getRazonSocial() {
        return razonSocial;
    }

    /**
     * @param razonSocial the razonSocial to set
     */
    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    /**
     * @return the dni
     */
    public int getDni() {
        return dni;
    }

    /**
     * @param dni the dni to set
     */
    public void setDni(int dni) {
        this.dni = dni;
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
     * @return the condicionIva
     */
    public CondicionIva getCondicionIva() {
        return condicionIva;
    }

    /**
     * @param condicionIva the condicionIva to set
     */
    public void setCondicionIva(CondicionIva condicionIva) {
        this.condicionIva = condicionIva;
    }
    
    
    
}
