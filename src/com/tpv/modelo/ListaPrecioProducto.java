/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.modelo;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Date;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import org.hibernate.annotations.Formula;

/**
 *
 * @author daniel
 */
@Entity
@Table(name="listaprecios_productos")
public class ListaPrecioProducto {

    /**
     * @return the precioLista
     */
    public BigDecimal getPrecioLista() {
        return precioLista;
    }

    /**
     * @param precioLista the precioLista to set
     */
    public void setPrecioLista(BigDecimal precioLista) {
        this.precioLista = precioLista;
    }

    /**
     * @return the precioEspecial
     */
    public BigDecimal getPrecioEspecial() {
        return precioEspecial;
    }

    /**
     * @param precioEspecial the precioEspecial to set
     */
    public void setPrecioEspecial(BigDecimal precioEspecial) {
        this.precioEspecial = precioEspecial;
    }

    /**
     * @return the precioPublico
     */
    public BigDecimal getPrecioPublico() {
        return precioPublico;
    }

    /**
     * @param precioPublico the precioPublico to set
     */
    public void setPrecioPublico(BigDecimal precioPublico) {
        this.precioPublico = precioPublico;
    }

    /**
     * @return the precioOferta
     */
    public BigDecimal getPrecioOferta() {
        return precioOferta;
    }

    /**
     * @param precioOferta the precioOferta to set
     */
    public void setPrecioOferta(BigDecimal precioOferta) {
        this.precioOferta = precioOferta;
    }

    /**
     * @return the fechaInicioOferta
     */
    public Date getFechaInicioOferta() {
        return fechaInicioOferta;
    }

    /**
     * @param fechaInicioOferta the fechaInicioOferta to set
     */
    public void setFechaInicioOferta(Date fechaInicioOferta) {
        this.fechaInicioOferta = fechaInicioOferta;
    }

    /**
     * @return the fechaFinOferta
     */
    public Date getFechaFinOferta() {
        return fechaFinOferta;
    }

    /**
     * @param fechaFinOferta the fechaFinOferta to set
     */
    public void setFechaFinOferta(Date fechaFinOferta) {
        this.fechaFinOferta = fechaFinOferta;
    }

    /**
     * @return the fechaInicioEspecial
     */
    public Date getFechaInicioEspecial() {
        return fechaInicioEspecial;
    }

    /**
     * @param fechaInicioEspecial the fechaInicioEspecial to set
     */
    public void setFechaInicioEspecial(Date fechaInicioEspecial) {
        this.fechaInicioEspecial = fechaInicioEspecial;
    }

    /**
     * @return the fechaFinEspecial
     */
    public Date getFechaFinEspecial() {
        return fechaFinEspecial;
    }

    /**
     * @param fechaFinEspecial the fechaFinEspecial to set
     */
    public void setFechaFinEspecial(Date fechaFinEspecial) {
        this.fechaFinEspecial = fechaFinEspecial;
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
     * @return the listaPrecio
     */
    public ListaPrecio getListaPrecio() {
        return listaPrecio;
    }

    /**
     * @param listaPrecio the listaPrecio to set
     */
    public void setListaPrecio(ListaPrecio listaPrecio) {
        this.listaPrecio = listaPrecio;
    }
    
    public java.sql.Date getFechaHoy(){
        return this.fechaHoy;
    }
    
    public void setFechaHoy(java.sql.Date fechaHoy){
        this.fechaHoy = fechaHoy;
    }
    
    @Embeddable
    public static class Id implements Serializable{
        @Column(name = "idPRODUCTOS")
        private Long productoId;
        
        @Column(name = "idLISTAPRECIOS")
        private int listaId;
        
        public Id (){
            
        }
        
        public Id(Long productoId, int listaId){
            this.productoId = productoId;
            this.listaId = listaId;
        }
        
        public boolean equals(Object o){
            if (o != null && o instanceof Id){
                Id that = (Id)o;
                return this.productoId == productoId &&
                        this.listaId == that.listaId;
            }else{
                return false;
            }
        }
        
        public int hashCode(){
            return Long.hashCode(productoId)+Long.hashCode(listaId);
        }
    }
        @EmbeddedId
        private Id id = new Id();
        
        @Column(name = "PRECIOLISTA")
        private BigDecimal precioLista;
        
        @Column(name = "PRECIOESPECIAL")
        private BigDecimal precioEspecial;
                
        @Column(name = "PRECIOPUBLICO")
        private BigDecimal precioPublico;

        @Column(name = "PRECIOOFERTA")
        private BigDecimal precioOferta;
        
        @Column(name = "FECHAINIOFERTA")
        private Date fechaInicioOferta;
        
        @Column(name = "FECHAFINOFERTA")
        private Date fechaFinOferta;
        
        @Column(name = "FECHAINIESPECIAL")
        private Date fechaInicioEspecial;
        
        @Column(name = "FECHAFINESPECIAL")
        private Date fechaFinEspecial;
        
        @Formula("(SELECT current_date())")
        private java.sql.Date fechaHoy;
        
      
        
        @ManyToOne
        @JoinColumn(name = "idPRODUCTOS",insertable=false ,updatable = false)
        private Producto producto;
        
        @ManyToOne
        @JoinColumn(name = "idLISTAPRECIOS",insertable=false ,updatable = false)
        private ListaPrecio listaPrecio;
        
        public ListaPrecioProducto(){
            
        }

        public ListaPrecioProducto(BigDecimal precioLista,BigDecimal precioPublico
                ,BigDecimal precioOferta,BigDecimal precioEspecial
                ,Producto producto, ListaPrecio listaPrecio){
            this.precioLista = precioLista;
            this.precioPublico = precioPublico;
            this.precioOferta = precioOferta;
            this.precioEspecial = precioEspecial;
            this.producto = producto;
            this.listaPrecio = listaPrecio;
            this.id.listaId = listaPrecio.getId();
            this.id.productoId = producto.getIdProducto();
        }
        
        @Transient
        public BigDecimal getPrecioBase(){
               BigDecimal precioAux = new BigDecimal(0);
               if(fechaInicioEspecial.compareTo(fechaHoy)<=0 &&
                       fechaFinEspecial.compareTo(fechaHoy)>=0){
                   precioAux =  precioEspecial;
               }else{
                   if(fechaInicioOferta.compareTo(fechaHoy)<=0 &&
                       fechaFinOferta.compareTo(fechaHoy)>=0)
                       precioAux = precioOferta;
                   else
                       precioAux = precioPublico;
               }
               return precioAux; 
        }
        
        @Transient
        public BigDecimal getPrecioFinal(){
               BigDecimal precioAux = new BigDecimal(0);
               if(fechaInicioEspecial.compareTo(fechaHoy)<=0 &&
                       fechaFinEspecial.compareTo(fechaHoy)>=0){
                   precioAux =  precioEspecial;
               }else{
                   if(fechaInicioOferta.compareTo(fechaHoy)<=0 &&
                       fechaFinOferta.compareTo(fechaHoy)>=0)
                       precioAux = precioOferta;
                   else
                       precioAux = precioPublico;
               }
               BigDecimal valorImpositivo = new BigDecimal(0);
               valorImpositivo = precioAux.multiply(producto.getValorImpositivo().getValor());
               valorImpositivo = valorImpositivo.divide(BigDecimal.valueOf(100));
               precioAux = precioAux.add(valorImpositivo);
               precioAux = precioAux.add(producto.getImpuestoInterno());
               
               return precioAux;
        }
        
        @Transient
        public BigDecimal getPrecioUnitario(){
               BigDecimal precioAux = new BigDecimal(0);
               if(fechaInicioEspecial.compareTo(fechaHoy)<=0 &&
                       fechaFinEspecial.compareTo(fechaHoy)>=0){
                   precioAux =  precioEspecial;
               }else{
                   if(fechaInicioOferta.compareTo(fechaHoy)<=0 &&
                       fechaFinOferta.compareTo(fechaHoy)>=0)
                       precioAux = precioOferta;
                   else
                       precioAux = precioPublico;
               }
               return precioAux;
        }
        
        @Transient
        public BigDecimal getIva(){
            BigDecimal valorImpositivo = null;
            valorImpositivo = getPrecioUnitario().multiply(producto.getValorImpositivo().getValor());
            valorImpositivo = valorImpositivo.divide(BigDecimal.valueOf(100));
            return valorImpositivo;
        }

                
    
}
