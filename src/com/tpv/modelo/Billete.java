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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author COMPUTOS
 */
@Entity
@Table(name="suctes_billete")
public class Billete {
    @Id
    @Column(name = "idsuctes_BILLETE")
    private int id;
    
    @Column(name = "VALOR")
    private BigDecimal valor;
    @Column(name = "DETALLE_FORMULARIO")
    private String detalleFormulario;
    @Column(name = "ESTADO")
    private boolean estado;
    @Column(name = "RETIRO_HABILITADO")
    private boolean retiroHabilitado;
    
    @ManyToOne
    @JoinColumn(name = "idFORMAPAGO", referencedColumnName = "idFORMAPAGO",nullable = false)
    private FormaPago formaPago;

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
     * @return the valor
     */
    public BigDecimal getValor() {
        return valor;
    }

    /**
     * @param valor the valor to set
     */
    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    /**
     * @return the detalleFormulario
     */
    public String getDetalleFormulario() {
        return detalleFormulario;
    }

    /**
     * @param detalleFormulario the detalleFormulario to set
     */
    public void setDetalleFormulario(String detalleFormulario) {
        this.detalleFormulario = detalleFormulario;
    }

    /**
     * @return the estado
     */
    public boolean isEstado() {
        return estado;
    }

    /**
     * @param estado the estado to set
     */
    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    /**
     * @return the retiroHabilitado
     */
    public boolean isRetiroHabilitado() {
        return retiroHabilitado;
    }

    /**
     * @param retiroHabilitado the retiroHabilitado to set
     */
    public void setRetiroHabilitado(boolean retiroHabilitado) {
        this.retiroHabilitado = retiroHabilitado;
    }

    /**
     * @return the formaPago
     */
    public FormaPago getFormaPago() {
        return formaPago;
    }

    /**
     * @param formaPago the formaPago to set
     */
    public void setFormaPago(FormaPago formaPago) {
        this.formaPago = formaPago;
    }
}
