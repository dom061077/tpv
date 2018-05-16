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
 * @author COMPUTOS
 */

@Entity
@Table(name="facturasdetallecombosabierto")
public class FacturaDetalleComboAbierto {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="idFACTURASDETALLECOMOSABIERTO")
    private Long id;
    
    @Column(name="cantidad")
    private BigDecimal cantidad;
    

    @Column(name="IMPUESTOINTERNO")
    private BigDecimal impuestoInterno;
    
    @Column(name="NETO")
    private BigDecimal neto;
    
    @Column(name="TOTAL")
    private BigDecimal total;
    
    @Column(name="TOTALSINBONIFICACION")
    private BigDecimal totalSinBonif;
    
    @Column(name="IVA")
    private BigDecimal iva;
    
    @Column(name="PORCENTAJEIVA")
    private BigDecimal porcentajeIva;
    
    @ManyToOne
    @JoinColumn(name = "idPRODUCTOS", referencedColumnName = "idPRODUCTOS", nullable=false)
    private Producto producto;
            
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "idFACTURASDETALLECOMBOS", referencedColumnName = "idFACTURASDETALLECOMBOS", nullable=false)
    private FacturaDetalleCombo fdCombo;    
    

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

    /**
     * @return the total
     */
    public BigDecimal getTotal() {
        return total;
    }

    /**
     * @param total the total to set
     */
    public void setTotal(BigDecimal total) {
        this.total = total;
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
     * @return the porcentjeIva
     */
    public BigDecimal getPorcentjeIva() {
        return getPorcentajeIva();
    }

    /**
     * @param porcentjeIva the porcentjeIva to set
     */
    public void setPorcentjeIva(BigDecimal porcentjeIva) {
        this.porcentajeIva = porcentjeIva;
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
     * @return the fdCombo
     */
    public FacturaDetalleCombo getFdCombo() {
        return fdCombo;
    }

    /**
     * @param fdCombo the fdCombo to set
     */
    public void setFdCombo(FacturaDetalleCombo fdCombo) {
        this.fdCombo = fdCombo;
    }

    /**
     * @return the totalSinBonif
     */
    public BigDecimal getTotalSinBonif() {
        return totalSinBonif;
    }

    /**
     * @param totalSinBonif the totalSinBonif to set
     */
    public void setTotalSinBonif(BigDecimal totalSinBonif) {
        this.totalSinBonif = totalSinBonif;
    }

    /**
     * @return the porcentajeIva
     */
    public BigDecimal getPorcentajeIva() {
        return porcentajeIva;
    }
    
    /**
     * transient que trae el neto reducido
     */
    public BigDecimal getNetoReducido(){
        BigDecimal netoCalculado = BigDecimal.ZERO;
        if (producto.getValorImpositivo().getId()==2)
            netoCalculado = getNeto();
        return netoCalculado;
    }
    
    public BigDecimal getNetoCompleto(){
        BigDecimal netoCalculado = BigDecimal.ZERO;
        if (producto.getValorImpositivo().getId()==0)
            netoCalculado = getNeto();
        return netoCalculado;    
    }
    
    public BigDecimal getExento(){
        BigDecimal exento = BigDecimal.ZERO;
        if (producto.getValorImpositivo().getId()==1)
            exento = getNeto();
        return exento;    
    }
    
    
}
