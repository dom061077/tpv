/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.principal;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author daniel
 */
public class LineaTicket {
    private IntegerProperty codigoProducto;
    private StringProperty descripcion;
    private IntegerProperty cantidad;
    private DoubleProperty precioUnitario;
    private DoubleProperty subTotal;
    
    public LineaTicket(){
        
    }
    
    public LineaTicket(int codigoProducto,String descripcion,int cantidad){
        
    }
    
    public int getCodigoProducto(){
        return codigoProductoProperty().get();
    }
    
    public IntegerProperty codigoProductoProperty(){
        if(codigoProducto == null){
            codigoProducto = new SimpleIntegerProperty();
        }
        return codigoProducto;
    }
    
    
    public String getDescripcion(){
        return descripcionProperty().get();
    }
    
    public StringProperty descripcionProperty(){
        if(descripcion == null){
            descripcion = new SimpleStringProperty();
        }
        return descripcion;
    }
    
    public int getCantidad(){
        return cantidadProperty().get();
    }
    
    public IntegerProperty cantidadProperty(){
        if(cantidad == null){
            cantidad = new SimpleIntegerProperty();
        }
        return cantidad;
    }
    
    public double getPrecioUnitario(){
        return precioUnitarioProperty().get();
    }
    
    public DoubleProperty precioUnitarioProperty(){
        if(precioUnitario == null){
            precioUnitario = new SimpleDoubleProperty();
        }
        return precioUnitario;
    }
    
    public double getSubTotal(){
        return subTotalProperty().get();
    }
    
    public DoubleProperty subTotalProperty(){
        if(subTotal == null){
            subTotal = new SimpleDoubleProperty();
        }
        return subTotal;
    }
    
    
}
