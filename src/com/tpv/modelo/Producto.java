/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.modelo;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name="productos")
public class Producto {
    @Id
    @Column(name = "idPRODUCTOS")
    private Long idProducto;
    @Column(name = "CODBARRA")
    private String codBarra;
    @Column(name = "STOCK")
    private int stock;
    @Column(name = "DETALLESUCURSALES")
    private String descripcion;
    @Column(name = "CODIGOPRODUCTO")
    private int codigoProducto;
    @Column(name = "DISCONTINUADO")
    private int discontinuado;
    @Column(name = "IMPUESTOINTERNO")
    private BigDecimal impuestoInterno;
    
    
    @ManyToOne
    @JoinColumn(name = "idVALORESIMPOSITIVOS"
            ,referencedColumnName="idVALORESIMPOSITIVOS",nullable=false)
    private ValorImpositivo valorImpositivo;
    
    @ManyToOne
    @JoinColumn(name = "idGRUPOPRODUCTOS",
            referencedColumnName="idGRUPOPRODUCTOS",nullable=false)
    private GrupoProducto grupoProducto;
    
    @OneToMany(mappedBy="producto")
    private List<ProveedorProducto> proveedores;
    

    /**
     * @return the idProducto
     */
    public Long getIdProducto() {
        return idProducto;
    }

    /**
     * @param idProducto the idProducto to set
     */
    public void setIdProducto(Long idProducto) {
        this.idProducto = idProducto;
    }

    /**
     * @return the codBarra
     */
    public String getCodBarra() {
        return codBarra;
    }

    /**
     * @param codBarra the codBarra to set
     */
    public void setCodBarra(String codBarra) {
        this.codBarra = codBarra;
    }

    /**
     * @return the stock
     */
    public int getStock() {
        return stock;
    }

    /**
     * @param stock the stock to set
     */
    public void setStock(int stock) {
        this.stock = stock;
    }


    /**
     * @return the codigoProducto
     */
    public int getCodigoProducto() {
        return codigoProducto;
    }

    /**
     * @param codigoProducto the codigoProducto to set
     */
    public void setCodigoProducto(int codigoProducto) {
        this.codigoProducto = codigoProducto;
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
     * @return the discontinuado
     */
    public int getDiscontinuado() {
        return discontinuado;
    }

    /**
     * @param discontinuado the discontinuado to set
     */
    public void setDiscontinuado(int discontinuado) {
        this.discontinuado = discontinuado;
    }

    /**
     * @return the valorImpositivo
     */
    public ValorImpositivo getValorImpositivo() {
        return valorImpositivo;
    }

    /**
     * @param valorImpositivo the valorImpositivo to set
     */
    public void setValorImpositivo(ValorImpositivo valorImpositivo) {
        this.valorImpositivo = valorImpositivo;
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
     * @return the proveedores
     */
//    public List<ProveedorProducto> getProveedores() {
//        return proveedores;
//    }
//
//    /**
//     * @param proveedores the proveedores to set
//     */
//    public void setProveedores(List<ProveedorProducto> proveedores) {
//        this.proveedores = proveedores;
//    }

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
    
    public boolean tieneEsteProveedor(Proveedor prov){
        for(Iterator<ProveedorProducto> it = getProveedores().iterator();it.hasNext();){
            ProveedorProducto pp = it.next();
            if(pp.getProveedor().equals(prov)){
                return true;
            }
                
        }
        return false;
    }
    
    public boolean tieneEsteGrupo(GrupoProducto gp){
        if(gp==null)
            return false;
        if(gp.equals(grupoProducto))
            return true;
        if(gp.equals(grupoProducto.getGrupoPadre()))
            return true;
        return false;
    }

    /**
     * @return the proveedores
     */
    public List<ProveedorProducto> getProveedores() {
        return proveedores;
    }

    /**
     * @param proveedores the proveedores to set
     */
    public void setProveedores(List<ProveedorProducto> proveedores) {
        this.proveedores = proveedores;
    }
         
}
