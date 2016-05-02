/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.modelo;

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
@Table(name="combosgrupodetalle")
public class ComboGrupoDetalle {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "idCOMBOSGRUPODETALLE")
    private Long id;

    
    @ManyToOne
    @JoinColumn(name = "idCOMBOSGRUPO", referencedColumnName = "idCOMBOSGRUPO", nullable=true)
    private ComboGrupo comboGrupo;  
    
    
    @ManyToOne
    @JoinColumn(name = "idPRODUCTOS", referencedColumnName = "idPRODUCTOS", nullable=true)
    private Producto producto;  

    @ManyToOne
    @JoinColumn(name = "idGRUPOPRODUCTOS", referencedColumnName = "idGRUPOPRODUCTOS", nullable=true)
    private GrupoProducto grupoProducto;  

    @ManyToOne
    @JoinColumn(name = "idProveedor", referencedColumnName = "idProveedor", nullable=true)
    private Proveedor proveedor;  

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
     * @return the producto
     */
    public Producto getProducto() {
        return producto;
    }

    /**
     * @param producto the producto to set
     */
    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    /**
     * @return the grupoProducto
     */
    public GrupoProducto getGrupoProducto() {
        return grupoProducto;
    }

    /**
     * @param grupoProducto the grupoProducto to set
     */
    public void setGrupoProducto(GrupoProducto grupoProducto) {
        this.grupoProducto = grupoProducto;
    }

    /**
     * @return the proveedor
     */
    public Proveedor getProveedor() {
        return proveedor;
    }

    /**
     * @param proveedor the proveedor to set
     */
    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }
    
    
}
