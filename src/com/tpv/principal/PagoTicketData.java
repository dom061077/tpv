/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.principal;

import java.math.BigDecimal;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author daniel
 */
public class PagoTicketData {
    private IntegerProperty CodigoPago;
    private StringProperty DescripcionPago;
    private IntegerProperty CantidadCuotas;
    private ObjectProperty<BigDecimal> MontoPago;
    
    public PagoTicketData(){
    }
    
    public int getCodigoPago(){
        return codigoPagoProperty().get();
    }
    
    public IntegerProperty codigoPagoProperty(){
        if(CodigoPago == null){
            CodigoPago = new SimpleIntegerProperty();
        }
        return CodigoPago;
    }
    public int getCantidadCuotas(){
        return cantidadCuotasProperty().get();
    }
    
    public IntegerProperty cantidadCuotasProperty(){
        if(CantidadCuotas == null){
            CantidadCuotas = new SimpleIntegerProperty();
        }
        return CantidadCuotas;
    }
    
    public String getDescripcion() {
        return descripcionPagoProperty().get();
    }

    public StringProperty descripcionPagoProperty() {
        if (DescripcionPago == null) {
            DescripcionPago = new SimpleStringProperty();
        }
        return DescripcionPago;
    }    
        
    public BigDecimal getMontoPago(){
        return montoPagoProperty().get();
    }
    
    public ObjectProperty<BigDecimal> montoPagoProperty(){
        if(MontoPago == null){
            MontoPago = new SimpleObjectProperty();
        }
        return MontoPago;
    }    
    
    
}
