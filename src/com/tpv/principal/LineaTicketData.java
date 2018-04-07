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
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author daniel
 */
public class LineaTicketData {
    private IntegerProperty CodigoProducto;
    private StringProperty Descripcion;
    private ObjectProperty<BigDecimal> Cantidad;
    private ObjectProperty<BigDecimal> PrecioUnitario;
    private ObjectProperty<BigDecimal> SubTotal;
    private ObjectProperty<BigDecimal> neto;//sin IVA 
    private ObjectProperty<BigDecimal> iva;
    private ObjectProperty<BigDecimal> impuestoInterno;
    private ObjectProperty<BigDecimal> descuento;
    private ObjectProperty<BigDecimal> retencion; //ingresos brutos
    
    private BooleanProperty Devuelto;
    
    public LineaTicketData(){
        
    }
    
    public LineaTicketData(int codigoProducto,String descripcion,BigDecimal cantidad,BigDecimal precioUnitario
            ,BigDecimal neto,BigDecimal impuestoInterno,BigDecimal descuento
            ,BigDecimal retencion ,boolean devuelto){
        this.CodigoProducto = new SimpleIntegerProperty(codigoProducto);
        this.Descripcion = new SimpleStringProperty(descripcion);
        this.Cantidad = new SimpleObjectProperty(cantidad);
        this.PrecioUnitario = new SimpleObjectProperty(precioUnitario);
        this.neto = new SimpleObjectProperty(neto);
        this.impuestoInterno = new SimpleObjectProperty(impuestoInterno);
        this.descuento = new SimpleObjectProperty(descuento);
        this.retencion = new SimpleObjectProperty(retencion);
        this.Devuelto = new SimpleBooleanProperty(devuelto);
        if(devuelto)
            cantidad = cantidad.multiply(new BigDecimal(-1));
        //this.SubTotal = new SimpleObjectProperty(new BigDecimal(precioUnitario.doubleValue()*cantidad));
        this.SubTotal = new SimpleObjectProperty(precioUnitario.multiply(cantidad));
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
    
    public void setDevuelto(boolean dev){
        this.devueltoProperty().set(dev);
    }
    
    public BooleanProperty devueltoProperty(){
        if(Devuelto == null){
            Devuelto = new SimpleBooleanProperty();
        }
        return Devuelto;
    }
    
    
}
