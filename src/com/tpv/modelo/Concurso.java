/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.modelo;

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
@Table(name="concursos")
public class Concurso {
   @Id
   @Column(name="idCONCURSOS")
   private Long id;
   
   
   
   @Column(name="CANTIDADPRODUCTOS")
   private int cantidadProductos;
   
   @Column(name="CANTIDADCUPONES")
   private int cantidadCupones;
   
   @Column(name="VIGENCIADESDE")
   private java.sql.Date vigenciaDesde;
   
   @Column(name="VIGENCIAHASTA")
   private java.sql.Date vigenciaHasta;
   
   @Column(name="TEXTOCORTO")
   private String textoCorto;
   
   @Column(name="IMPRIMETEXTO")
   private boolean imprimeTexto;
   
   @ManyToOne
   @JoinColumn(name="idProveedor",referencedColumnName="idProveedor",nullable = true)
   private Proveedor proveedor;
   
   @ManyToOne
   @JoinColumn(name = "idGRUPOPRODUCTOS", referencedColumnName = "idGRUPOPRODUCTOS",nullable = true)
   private GrupoProducto grupoProducto;
   
   @ManyToOne
   @JoinColumn(name = "idSUBGRUPO", referencedColumnName = "idGRUPOPRODUCTOS", nullable = true)
   private GrupoProducto grupoProductoHijo;
   
   @ManyToOne
   @JoinColumn(name = "idPRODUCTOS", referencedColumnName = "idPRODUCTOS", nullable = true)
   private Producto producto;

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
     * @return the cantidadProductos
     */
    public int getCantidadProductos() {
        return cantidadProductos;
    }

    /**
     * @param cantidadProductos the cantidadProductos to set
     */
    public void setCantidadProductos(int cantidadProductos) {
        this.cantidadProductos = cantidadProductos;
    }

    /**
     * @return the cantidadCupones
     */
    public int getCantidadCupones() {
        return cantidadCupones;
    }

    /**
     * @param cantidadCupones the cantidadCupones to set
     */
    public void setCantidadCupones(int cantidadCupones) {
        this.cantidadCupones = cantidadCupones;
    }

    /**
     * @return the vigenciaDesde
     */
    public java.sql.Date getVigenciaDesde() {
        return vigenciaDesde;
    }

    /**
     * @param vigenciaDesde the vigenciaDesde to set
     */
    public void setVigenciaDesde(java.sql.Date vigenciaDesde) {
        this.vigenciaDesde = vigenciaDesde;
    }

    /**
     * @return the vigenciaHasta
     */
    public java.sql.Date getVigenciaHasta() {
        return vigenciaHasta;
    }

    /**
     * @param vigenciaHasta the vigenciaHasta to set
     */
    public void setVigenciaHasta(java.sql.Date vigenciaHasta) {
        this.vigenciaHasta = vigenciaHasta;
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
     * @return the grupoProductoHijo
     */
    public GrupoProducto getGrupoProductoHijo() {
        return grupoProductoHijo;
    }

    /**
     * @param grupoProductoHijo the grupoProductoHijo to set
     */
    public void setGrupoProductoHijo(GrupoProducto grupoProductoHijo) {
        this.grupoProductoHijo = grupoProductoHijo;
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
     * @return the textoCorto
     */
    public String getTextoCorto() {
        return textoCorto;
    }

    /**
     * @param textoCorto the textoCorto to set
     */
    public void setTextoCorto(String textoCorto) {
        this.textoCorto = textoCorto;
    }

    /**
     * @return the imprimeTexto
     */
    public boolean isImprimeTexto() {
        return imprimeTexto;
    }

    /**
     * @param imprimeTexto the imprimeTexto to set
     */
    public void setImprimeTexto(boolean imprimeTexto) {
        this.imprimeTexto = imprimeTexto;
    }
   
   
}
