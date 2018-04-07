/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.modelo;
//http://vladmihalcea.com/2015/03/05/a-beginners-guide-to-jpa-and-hibernate-cascade-types/

import java.math.BigDecimal;
import java.math.MathContext;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PostLoad;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 *
 * @author daniel
 */

@Entity
@Table(name="facturasdetalle")
public class FacturaDetalle {
    @Id 
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "idFACTURASDETALLE")
    private Long id;
    
    @Column(name = "CANTIDAD")
    private BigDecimal cantidad;
    
    @Column(name = "VALORUNITARIO")
    private BigDecimal precioUnitario;
    
    @Column(name = "IVA")
    private BigDecimal iva;
    
    @Column(name = "DESCUENTO")
    private BigDecimal descuento;
    
    @Column(name = "IMPUESTOINTERNO")
    private BigDecimal impuestoInterno;
    
    @Column(name = "NETO")
    private BigDecimal neto;
    
    @Column(name = "TOTAL")
    private BigDecimal subTotal;
    
    @Transient
    private BigDecimal ivaReducido;
    
    @Transient
    private int cantidadAuxCombo;
    
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "idFACTURAS", referencedColumnName = "idFACTURAS", nullable=false)
    private Factura factura;
    
    
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "idPRODUCTOS", referencedColumnName = "idPRODUCTOS", nullable=false)
    private Producto producto;
    


    @PostLoad
    protected void intCantidadAuxCombo(){
        setCantidadAuxCombo(cantidad.intValue());
    }
    
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
     * @return the subTotal
     */
    public BigDecimal getSubTotal() {
        return subTotal;
    }

    /**
     * @param subTotal the subTotal to set
     */
    public void setSubTotal(BigDecimal subTotal) {
        this.subTotal = subTotal;
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
     * @return the precioUnitario
     */
    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    /**
     * @param precioUnitario the precioUnitario to set
     */
    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    /**
     * @return the iva
     */
    public BigDecimal getIva() {
        return iva;
    }

    /**
     * @param iva the iva to set
     */
    public void setIva(BigDecimal iva) {
        this.iva = iva;
    }

    /**
     * @return the descuento
     */
    public BigDecimal getDescuento() {
        return descuento;
    }

    /**
     * @param descuento the descuento to set
     */
    public void setDescuento(BigDecimal descuento) {
        this.descuento = descuento;
    }

    /**
     * @return the impuestoInterno
     */
    public BigDecimal getImpuestoInterno() {
        return impuestoInterno;
    }

    /**
     * @param impuestoInterno the impuestoInterno to set
     */
    public void setImpuestoInterno(BigDecimal impuestoInterno) {
        this.impuestoInterno = impuestoInterno;
    }

    /**
     * @return the neto
     */
    public BigDecimal getNeto() {
        return neto;
    }

    /**
     * @param neto the neto to set
     */
    public void setNeto(BigDecimal neto) {
        this.neto = neto;
    }
    
    public void decrementarCantidadAuxCombo(int cantidad){
        this.setCantidadAuxCombo(this.getCantidadAuxCombo() - cantidad);
    }
    
    public void incrementarCantidadAuxCombo(int cantidad){
        this.setCantidadAuxCombo(this.getCantidadAuxCombo()+ cantidad);
    }
    

    @Transient
    public BigDecimal getSubTotal(int cantidad){
        return this.precioUnitario.multiply(new BigDecimal(cantidad));
    }

    /**
     * @return the cantidadAuxCombo
     */
    public int getCantidadAuxCombo() {
        return cantidadAuxCombo;
    }

    /**
     * @param cantidadAuxCombo the cantidadAuxCombo to set
     */
    public void setCantidadAuxCombo(int cantidadAuxCombo) {
        this.cantidadAuxCombo = cantidadAuxCombo;
    }
    
    
}
