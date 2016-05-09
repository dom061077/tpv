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
@Table(name="combosgrupo")
public class ComboGrupo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "idCOMBOSGRUPO")
    private Long id;
    
    @Column(name = "CANTIDADPRODUCTO")
    private int cantidad;
            
    @Column(name = "PORCENTAJE")
    private BigDecimal porcentaje;

    @Column(name = "MONTO")
    private BigDecimal monto;
    
    @Transient
    private int cantidadAux;
    
    @Transient
    private BigDecimal totalDescuento;

    @ManyToOne
    @JoinColumn(name = "idCOMBOS", referencedColumnName = "idCOMBOS", nullable=true)
    private Combo combo;  
    
    @OneToMany(cascade = CascadeType.ALL,mappedBy="comboGrupo")
    private List<ComboGrupoDetalle> gruposDetalle = new ArrayList<ComboGrupoDetalle>();
    

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
     * @return the gruposDetalle
     */
    public List<ComboGrupoDetalle> getGruposDetalle() {
        return gruposDetalle;
    }

    /**
     * @param gruposDetalle the gruposDetalle to set
     */
    public void setGruposDetalle(List<ComboGrupoDetalle> gruposDetalle) {
        this.gruposDetalle = gruposDetalle;
    }

    /**
     * @return the cantidadAux
     */
    public int getCantidadAux() {
        return cantidadAux;
    }

    /**
     * @param cantidadAux the cantidadAux to set
     */
    public void setCantidadAux(int cantidadAux) {
        this.cantidadAux = cantidadAux;
    }

    public void incCantidadAux(int inc){
        this.cantidadAux += inc;
    }


    @Transient
    public int getCantidadGrupos(){
        if(cantidadAux<cantidad)
            return 0;
        return cantidadAux/cantidad;
    }


    /**
     * @return the totalDescuento
     */
    public BigDecimal getTotalDescuento() {
        return totalDescuento;
    }

    /**
     * @param totalDescuento the totalDescuento to set
     */
    public void setTotalDescuento(BigDecimal totalDescuento) {
        this.totalDescuento = totalDescuento;
    }
    
    public void incTotalDescuento(BigDecimal precioUnitario,int cantidad){
        totalDescuento = totalDescuento.add(precioUnitario.multiply(new BigDecimal(cantidad)));
    }
    

}
