/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.modelo;

import java.math.BigDecimal;
import javax.persistence.CascadeType;
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
@Table(name="combosgrupo")
public class ComboGrupo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "idCOMBOSGRUPO")
    private Long id;
    
    @Column(name = "CANTIDADPRODUCTOS")
    private BigDecimal cantidad;
            
    @Column(name = "PORCENTAJE")
    private BigDecimal porcentaje;

    @Column(name = "monto")
    private BigDecimal monto;
    

    @ManyToOne
    @JoinColumn(name = "idCOMBOS", referencedColumnName = "idCOMBOS", nullable=true)
    private Combo combo;  
    
    
    @ManyToOne
    @JoinColumn(name = "idPRODUCTOS", referencedColumnName = "idPRODUCTOS", nullable=true)
    private Producto producto;  
    
    @ManyToOne
    @JoinColumn(name = "idGRUPRODUCTOS", referencedColumnName = "idGRUPOPRODUCTOS", nullable=true)
    private GrupoProducto grupoProducto;

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
     * @return the cantidad
     */
    public BigDecimal getCantidad() {
        return cantidad;
    }

    /**
     * @param cantidad the cantidad to set
     */
    public void setCantidad(BigDecimal cantidad) {
        this.cantidad = cantidad;
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
     * @return the combo
     */
    public Combo getCombo() {
        return combo;
    }

    /**
     * @param combo the combo to set
     */
    public void setCombo(Combo combo) {
        this.combo = combo;
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
    
    
    
}
