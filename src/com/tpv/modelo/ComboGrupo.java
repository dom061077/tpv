/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.modelo;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
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
    
    

    @ManyToOne
    @JoinColumn(name = "idCOMBOS", referencedColumnName = "idCOMBOS", nullable=true)
    private Combo combo;  
    
    @OneToMany(cascade = CascadeType.ALL,mappedBy="comboGrupo")
    private List<ComboGrupoDetalle> gruposDetalle = new ArrayList<ComboGrupoDetalle>();
    
    @Transient
    private List<ComboGrupoDetallePrecioProducto> detallePreciosProductos = new ArrayList<ComboGrupoDetallePrecioProducto>();
    

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
     * @return the detallePreciosProductos
     */
    public List<ComboGrupoDetallePrecioProducto> getDetallePreciosProductos() {
        return detallePreciosProductos;
    }
    
    public void addDetallePrecioProducto(int cantidad,BigDecimal precioProducto,Producto producto
        ,FacturaDetalle factDetalle){
        boolean agregar = true;
        if(factDetalle == null){
            for(Iterator<ComboGrupoDetallePrecioProducto> it = detallePreciosProductos.iterator();it.hasNext();){
                ComboGrupoDetallePrecioProducto gpp = it.next();
                if(gpp.getPrecioProducto().equals(producto)){
                   gpp.setCantidad(gpp.getCantidad()+cantidad);
                   agregar = false;
                }
            }
        }
        
        if(agregar){
            ComboGrupoDetallePrecioProducto gdProd = new ComboGrupoDetallePrecioProducto();
            gdProd.setCantidad(cantidad);
            gdProd.setPrecioProducto(precioProducto);
            gdProd.setProducto(producto);
            gdProd.setFactDetalle(factDetalle);
            gdProd.setComboGrupo(this);
            detallePreciosProductos.add(gdProd);
        }
        
    }
    
    @Transient
    public int getCantidadAcumulada(){
        int cantAcumulada = 0;
        for(Iterator<ComboGrupoDetallePrecioProducto> iterator = getDetallePreciosProductos().iterator();
                iterator.hasNext();){
            ComboGrupoDetallePrecioProducto cGrupoDet = iterator.next();
            cantAcumulada+=cGrupoDet.getCantidad();
        }
        return cantAcumulada;
    }
    
    @Transient
    public int getCantidadGrupos(){
        if(getCantidadAcumulada()<cantidad)
            return 0;
        return getCantidadAcumulada()/cantidad;
    }    
    
    @Transient
    public BigDecimal getBonificacion(){
        BigDecimal bonificacion = new BigDecimal(BigInteger.ZERO);
        int cantidadCombos = combo.getCantidadCombosArmados();
        for(Iterator<ComboGrupoDetallePrecioProducto> iterator = getDetallePreciosProductos().iterator()
                ;iterator.hasNext();){
            ComboGrupoDetallePrecioProducto cGDetPrecioProducto = iterator.next();
            //bonificacion = bonificacion.add(cGDetPrecioProducto.getSubTotal().multiply(this.getPorcentaje()).divide(new BigDecimal(100)));
            bonificacion = cGDetPrecioProducto.getBonificacionPorPrecioProducto();
            
        }
        return bonificacion;
    }
    

}
