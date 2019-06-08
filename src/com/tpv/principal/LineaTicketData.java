/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.principal;

import java.math.BigDecimal;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author daniel
 */
public class LineaTicketData {
    private ObjectProperty<Long> idProducto;//id del PRODUCTO!!
    private IntegerProperty CodigoProducto;
    private StringProperty CodBarra;
    private StringProperty Descripcion;
    private ObjectProperty<BigDecimal> Cantidad;
    private ObjectProperty<BigDecimal> PrecioUnitario;
    private ObjectProperty<BigDecimal> PrecioUnitarioBase;//sin sin iva, impuestointerno pero si con posible descuento de cliente
    private ObjectProperty<BigDecimal> SubTotal;
    
    
    
    /**
     * 
     *  neto de la línea de detalle sin el iva del 21 %
    */
    private ObjectProperty<BigDecimal> neto;
    /**
     * neto de la linea de detalle sin el iva del 10.5 %
     */
    
    /**
     * se guarda el porcentaje del iva
     */
    private ObjectProperty<BigDecimal> porcentajeIva;
    
    
    private ObjectProperty<BigDecimal> netoReducido;
    /**
     *  iva del 21% de la línea de detalle
     */
    private ObjectProperty<BigDecimal> iva;
    /**
     * iva reducido del 10.5 % de la línea de detalle
     */
    private ObjectProperty<BigDecimal> ivaReducido;
    
    /**
     *  exento de la línea de datalle
     */
    private ObjectProperty<BigDecimal> exento;
    
    private ObjectProperty<BigDecimal> impuestoInterno;
    private ObjectProperty<BigDecimal> descuentoCliente;
    private ObjectProperty<BigDecimal> retencion; //ingresos brutos
    private ObjectProperty<BigDecimal> costo;
    
    private BooleanProperty Devuelto;
    
    public LineaTicketData(){
        
    }
    
    public LineaTicketData(Long idProducto, int codigoProducto,String codBarra,String descripcion,BigDecimal cantidad,BigDecimal precioUnitario
            ,BigDecimal precioUnitarioBase,BigDecimal neto,BigDecimal netoReducido,BigDecimal exento
            ,BigDecimal descuentoCliente,BigDecimal iva ,BigDecimal ivaReducido
            ,BigDecimal impuestoInterno,BigDecimal retencion
            ,BigDecimal porcentajeIva
            ,BigDecimal costo
            ,boolean devuelto){
        
        BigDecimal montoSigno = BigDecimal.valueOf(1);
        if(devuelto)
            montoSigno = montoSigno.multiply(BigDecimal.valueOf(-1));
        this.idProducto = new SimpleObjectProperty(idProducto);
        this.CodigoProducto = new SimpleIntegerProperty(codigoProducto);
        this.CodBarra = new SimpleStringProperty(codBarra);
        this.Descripcion = new SimpleStringProperty(descripcion);
        this.Cantidad = new SimpleObjectProperty(cantidad);
        this.PrecioUnitario = new SimpleObjectProperty(precioUnitario);
        this.PrecioUnitarioBase = new SimpleObjectProperty(precioUnitarioBase.multiply(montoSigno));
        this.neto = new SimpleObjectProperty(neto.multiply(montoSigno));
        this.netoReducido = new SimpleObjectProperty(netoReducido.multiply(montoSigno));
        this.exento = new SimpleObjectProperty(exento.multiply(montoSigno));
        this.iva = new SimpleObjectProperty(iva.multiply(montoSigno));
        this.ivaReducido = new SimpleObjectProperty(ivaReducido.multiply(montoSigno));
        this.impuestoInterno = new SimpleObjectProperty(impuestoInterno.multiply(montoSigno));
        this.descuentoCliente = new SimpleObjectProperty(descuentoCliente.multiply(montoSigno));
        this.retencion = new SimpleObjectProperty(retencion.multiply(montoSigno));
        this.Devuelto = new SimpleBooleanProperty(devuelto);
        cantidad = cantidad.multiply(montoSigno);
        //this.SubTotal = new SimpleObjectProperty(new BigDecimal(precioUnitario.doubleValue()*cantidad));
        this.SubTotal = new SimpleObjectProperty(precioUnitario.multiply(cantidad));
        this.porcentajeIva = new SimpleObjectProperty(porcentajeIva);
        this.costo = new SimpleObjectProperty(costo.multiply(montoSigno));
    }
    
    public int getCodigoProducto(){
        return codigoProductoProperty().get();
    }
    
    public IntegerProperty codigoProductoProperty(){
        if(CodigoProducto == null){
            CodigoProducto = new SimpleIntegerProperty();
        }
        return CodigoProducto;
    }

    public StringProperty codBarraProperty(){
        if(CodBarra == null){
            CodBarra = new SimpleStringProperty();
        }
        return CodBarra;
    }
    
    public String getCodBarra(){
        return codBarraProperty().get();
    }
            
    public void setCodBarra(String codBarra){
        this.CodBarra.set(codBarra);
        
    }
            
    
    public String getDescripcion(){
        return descripcionProperty().get();
    }
    
    public StringProperty descripcionProperty(){
        if(Descripcion == null){
            Descripcion = new SimpleStringProperty();
        }
        return Descripcion;
    }
    
    public BigDecimal getCantidad(){
        return cantidadProperty().get();
    }
    
    public ObjectProperty<BigDecimal> cantidadProperty(){
        if(Cantidad == null){
            Cantidad = new SimpleObjectProperty<BigDecimal>();
        }
        return Cantidad;
    }
    
    public BigDecimal getPrecioUnitario(){
        return precioUnitarioProperty().get();
    }
    
    public BigDecimal getPrecioUnitarioBase(){
        return PrecioUnitarioBase.get();
    }
    
    public ObjectProperty<BigDecimal> precioUnitarioProperty(){
        if(PrecioUnitario == null){
            PrecioUnitario = new SimpleObjectProperty();
        }
        return PrecioUnitario;
    }
    
    public BigDecimal getSubTotal(){
        return subTotalProperty().get();
    }
    
    public ObjectProperty<BigDecimal> subTotalProperty(){
        if(SubTotal == null){
            SubTotal = new SimpleObjectProperty<BigDecimal>();
        }
        return SubTotal;
    }
    
    public boolean getDevuelto(){
        return devueltoProperty().get();
    }
    
    
    public BooleanProperty devueltoProperty(){
        if(Devuelto == null){
            Devuelto = new SimpleBooleanProperty();
        }
        return Devuelto;
    }
    
    
    public void setDevuelto(boolean devuelto){
        this.devueltoProperty().set(devuelto);
    }
    
    public ObjectProperty<Long> idProperty(){
        if(idProducto == null){
            idProducto = new SimpleObjectProperty<Long>();
        }
        return idProducto;
    }
    
    public Long getId(){
        return idProperty().get();
    }
    
    public void setId(Long id){
        this.idProperty().set(id);
    }
    
    public ObjectProperty<BigDecimal> costoProperty(){
        if(costo == null){
            costo = new SimpleObjectProperty<BigDecimal>();
        }
        return costo;
    }
    
    

    
    public BigDecimal getCosto(){
        return costoProperty().get();
    }

    public void setCosto(BigDecimal costo){
        this.costoProperty().set(costo);
    }

    /**
     * @return the netoReducido
     */
    public BigDecimal getNetoReducido() {
        return netoReducido.get();
    }

    
    
    /**
     * @param netoReducido the netoReducido to set
     */
    public void setNetoReducido(ObjectProperty<BigDecimal> netoReducido) {
        this.netoReducido = netoReducido;
    }

    /**
     * @return the iva
     */
    public BigDecimal getIva() {
        return iva.get();
    }

    /**
     * @param iva the iva to set
     */
    public void setIva(ObjectProperty<BigDecimal> iva) {
        this.iva = iva;
    }

    /**
     * @return the ivaReducido
     */
    public BigDecimal getIvaReducido() {
        return ivaReducido.get();
    }

    /**
     * @param ivaReducido the ivaReducido to set
     */
    public void setIvaReducido(ObjectProperty<BigDecimal> ivaReducido) {
        this.ivaReducido = ivaReducido;
    }

    /**
     * @return the exento
     */
    public BigDecimal getExento() {
        return exento.get();
    }

    /**
     * @param exento the exento to set
     */
    public void setExento(ObjectProperty<BigDecimal> exento) {
        this.exento = exento;
    }

    /**
     * @return the impuestoInterno
     */
    public BigDecimal getImpuestoInterno() {
        return impuestoInterno.get();
    }

    /**
     * @param impuestoInterno the impuestoInterno to set
     */
    public void setImpuestoInterno(ObjectProperty<BigDecimal> impuestoInterno) {
        this.impuestoInterno = impuestoInterno;
    }

    /**
     * @return the descuentoCliente
     */
    public BigDecimal getDescuentoCliente() {
        return descuentoCliente.get();
    }

    /**
     * @param descuentoCliente the descuentoCliente to set
     */
    public void setDescuentoCliente(ObjectProperty<BigDecimal> descuentoCliente) {
        this.descuentoCliente = descuentoCliente;
    }

    /**
     * @return the retencion
     */
    public BigDecimal getRetencion() {
        return retencion.get();
    }

    /**
     * @param retencion the retencion to set
     */
    public void setRetencion(ObjectProperty<BigDecimal> retencion) {
        this.retencion = retencion;
    }

    /**
     * @return the neto
     */
    public BigDecimal getNeto() {
        return neto.get();
    }

    /**
     * @param neto the neto to set
     */
    public void setNeto(ObjectProperty<BigDecimal> neto) {
        this.neto = neto;
    }

    /**
     * @return the porcentajeIva
     */
    public BigDecimal getPorcentajeIva() {
        return porcentajeIva.get();
    }

    /**
     * @param porcentajeIva the porcentajeIva to set
     */
    public void setPorcentajeIva(ObjectProperty<BigDecimal> porcentajeIva) {
        this.porcentajeIva = porcentajeIva;
    }
    
    
    
}
