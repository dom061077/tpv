/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.modelo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 *
 * @author daniel
 */

@Entity
@Table(name="facturasdetallecombos")
public class FacturaDetalleCombo {
    @Id 
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "idFACTURASDETALLECOMBOS")
    private Long id;    
    
    @Column(name = "CANTIDADCOMBOS")
    private int cantidad;
    
    @Column(name = "MONTOBONIFICADO")
    private BigDecimal bonificacion;
    
    @ManyToOne
    @JoinColumn(name = "idFACTURAS", referencedColumnName = "idFACTURAS", nullable=false)
    private Factura factura;
    
    @ManyToOne
    @JoinColumn(name = "idCOMBOS", referencedColumnName = "idCOMBOS", nullable=false)
    private Combo combo;

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
    public int getCantidad() {
        return cantidad;
    }

    /**
     * @param cantidad the cantidad to set
     */
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    /**
     * @return the bonificacion
     */
    public BigDecimal getBonificacion() {
        return bonificacion;
    }

    /**
     * @param bonificacion the bonificacion to set
     */
    public void setBonificacion(BigDecimal bonificacion) {
        this.bonificacion = bonificacion;
    }

    /**
     * @return the factura
     */
    public Factura getFactura() {
        return factura;
    }

    /**
     * @param factura the factura to set
     */
    public void setFactura(Factura factura) {
        this.factura = factura;
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
    
    /*@Transient
    public BigDecimal getBonificacionCalculada(){
        BigDecimal bonificacionCalculada = BigDecimal.ZERO;
        return bonificacionCalculada;
    }*/
    
    
    @Transient
    public BigDecimal getIVABonificacion(){
        BigDecimal ivaBonificado = BigDecimal.ZERO;
        BigDecimal porcien = combo.getValorImpositivo().getValor();
        ivaBonificado = bonificacion.multiply(porcien).divide(BigDecimal.valueOf(100));
                
        return ivaBonificado;
    }
    
}
