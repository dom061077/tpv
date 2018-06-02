/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.pagoticket;

import java.math.BigDecimal;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author COMPUTOS
 */
public class LineaInteresTarjetaData {
    private IntegerProperty Cuotas;
    private StringProperty Descripcion;
    private ObjectProperty<BigDecimal> Porcentaje;
    private ObjectProperty<BigDecimal> TotalPago;
    
    public LineaInteresTarjetaData(){
        
    }
    
    public LineaInteresTarjetaData(int cuotas
                ,String Descripcion
                ,BigDecimal porcentaje
                ,BigDecimal totalPago){
        this.Cuotas = new SimpleIntegerProperty(cuotas);
        this.Descripcion = new SimpleStringProperty(Descripcion);
        this.Porcentaje = new SimpleObjectProperty(porcentaje);
        this.TotalPago = new SimpleObjectProperty(totalPago);
    }
    
    
    public IntegerProperty cuotasProperty(){
        return Cuotas;
    }
    
    public int getCuotas(){
        return Cuotas.get();
    }
    
    public ObjectProperty<BigDecimal> porcentajeProperty(){
        return Porcentaje;
    }
    
    public BigDecimal getPorcentaje(){
        return Porcentaje.get();
    }
    
    public ObjectProperty<BigDecimal> totalPagoProperty(){
        return TotalPago;
    }
    
    public BigDecimal getTotalPago(){
        return TotalPago.get();
    }
    
    public StringProperty descripcionProperty(){
        return Descripcion;
    }
    
    public String getDescripcion(){
        return Descripcion.get();
    }
            
    
}
