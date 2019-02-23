/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.modelo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
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
    
    @OneToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL,mappedBy="fdCombo")
    private List<FacturaDetalleComboAbierto> detalleAbierto = new ArrayList<FacturaDetalleComboAbierto>();    
   

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
        for(Iterator<FacturaDetalleComboAbierto> it = getDetalleAbierto().iterator();it.hasNext();){
            ivaBonificado = ivaBonificado.add(it.next().getIva());
        }
        return ivaBonificado;
    }
    
    @Transient
    public BigDecimal getIvaCompletoBonif(){
        BigDecimal ivaCompleto = BigDecimal.ZERO;
        for(Iterator<FacturaDetalleComboAbierto> it = getDetalleAbierto().iterator();it.hasNext();){
            FacturaDetalleComboAbierto fdca = it.next();
            if(fdca.getProducto().getValorImpositivo().getId()==0)
                ivaCompleto = ivaCompleto.add(fdca.getIva());
        }
        return ivaCompleto;
    }
    
    @Transient
    public BigDecimal getIvaReducidoBonif(){
        BigDecimal ivaReducido = BigDecimal.ZERO;
        for(Iterator<FacturaDetalleComboAbierto> it = getDetalleAbierto().iterator();it.hasNext();){
            FacturaDetalleComboAbierto fdca = it.next();
            if(fdca.getProducto().getValorImpositivo().getId()==2)
                ivaReducido = ivaReducido.add(fdca.getIva());
        }
        return ivaReducido;
    }
    
    @Transient
    public BigDecimal getNetoCompletoBonif(){
        BigDecimal neto = BigDecimal.ZERO;
        for(Iterator<FacturaDetalleComboAbierto> it = getDetalleAbierto().iterator();it.hasNext();){
            FacturaDetalleComboAbierto fdca = it.next();
            neto = neto.add(fdca.getNetoCompleto());
        }
        return neto;    
    }
    
    @Transient
    public BigDecimal getExentoBonif(){
        BigDecimal exento = BigDecimal.ZERO;
        for(Iterator<FacturaDetalleComboAbierto> it = getDetalleAbierto().iterator();it.hasNext();){
            FacturaDetalleComboAbierto fdca = it.next();
            if(fdca.getProducto().getValorImpositivo().getId()==1)
                exento = exento.add(fdca.getNeto());
        }
        return exento;
    }
    
    
    @Transient
    public BigDecimal getNetoReducidoBonif(){
        BigDecimal neto = BigDecimal.ZERO;
        for(Iterator<FacturaDetalleComboAbierto> it = getDetalleAbierto().iterator();it.hasNext();){
            FacturaDetalleComboAbierto fdca = it.next();
            neto = neto.add(fdca.getNetoReducido());
        }
        return neto;
    }
    
    
    @Transient
    public BigDecimal getImpuestoInterno(){
        BigDecimal impuestoInterno=BigDecimal.ZERO;
        for(Iterator<FacturaDetalleComboAbierto> it = getDetalleAbierto().iterator();it.hasNext();){
            FacturaDetalleComboAbierto fdca = it.next();
            impuestoInterno = impuestoInterno.add(fdca.getImpuestoInterno());
        }
        return impuestoInterno;
    }
    
    public BigDecimal getCostoPiso(){
        BigDecimal totalCostoPiso = BigDecimal.ZERO;
        for(Iterator<FacturaDetalleComboAbierto> it = getDetalleAbierto().iterator();it.hasNext();){
            FacturaDetalleComboAbierto fdca = it.next();
            totalCostoPiso = totalCostoPiso.add(fdca.getCostoPiso());
        }
        return totalCostoPiso;
    }
    
    public BigDecimal getImpuestoInternoParaCoeficienteK(){
        BigDecimal totalii = BigDecimal.ZERO;
        for(Iterator<FacturaDetalleComboAbierto> it = getDetalleAbierto().iterator();it.hasNext();){
            FacturaDetalleComboAbierto fdca = it.next();
            totalii = totalii.add(fdca.getImpuestoInterno()
                    .divide(fdca.getCantidad()));
        }
        return totalii;
    }
    
    public BigDecimal getPrecioUnitarioBaseParaCoeficienteK(){
        BigDecimal totalizado = BigDecimal.ZERO;
        for(Iterator<FacturaDetalleComboAbierto> it = getDetalleAbierto().iterator();it.hasNext();){
            FacturaDetalleComboAbierto fdca = it.next();
            totalizado = totalizado.add(fdca.getPrecioUnitarioBase());
        }
        return totalizado;
    }

    /**
     * @return the detalleAbierto
     */
    public List<FacturaDetalleComboAbierto> getDetalleAbierto() {
        return detalleAbierto;
    }
    
}
