/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.modelo;

import java.util.Set;
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
@Table(name="grupoproductos")
public class GrupoProducto {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "idGRUPOPRODUCTOS")
    private Long id;
    
    @Column(name = "DESCRIPCION")
    private String descripcion;
    
    @ManyToOne
    @JoinColumn(name = "PadreID", referencedColumnName = "idGRUPOPRODUCTOS", nullable=false)
    private GrupoProducto grupoPadre;
    
    @OneToMany(mappedBy="grupoPadre")
    private Set<GrupoProducto> grupoHijos;

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
     * @return the grupoPadre
     */
    public GrupoProducto getGrupoPadre() {
        return grupoPadre;
    }

    /**
     * @param grupoPadre the grupoPadre to set
     */
    public void setGrupoPadre(GrupoProducto grupoPadre) {
        this.grupoPadre = grupoPadre;
    }

    /**
     * @return the grupoHijos
     */
    public Set<GrupoProducto> getGrupoHijos() {
        return grupoHijos;
    }

    /**
     * @param grupoHijos the grupoHijos to set
     */
    public void setGrupoHijos(Set<GrupoProducto> grupoHijos) {
        this.grupoHijos = grupoHijos;
    }
    
    
}
