/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.pagoticket;

import com.mchange.v2.codegen.bean.SimpleProperty;
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
public class LineaPagoData {
    private IntegerProperty CodigoPago;
    private StringProperty Descripcion;
    private ObjectProperty<BigDecimal> Monto;
    private IntegerProperty CantidadCuotas;
    private IntegerProperty CodigoCupon;
    private IntegerProperty NroTarjeta;
    
    public LineaPagoData(){
        
    }
    
    public LineaPagoData(int codigoPago,String descripcion,BigDecimal monto
            ,int cantidadCuotas,int nroTarjeta, int codigoCupon){
        this.CodigoPago = new SimpleIntegerProperty(codigoPago);
        this.Descripcion = new SimpleStringProperty(descripcion);
        this.Monto = new SimpleObjectProperty(monto);
        this.CantidadCuotas = new SimpleIntegerProperty(cantidadCuotas);
        this.CodigoCupon = new SimpleIntegerProperty(codigoCupon);
        this.NroTarjeta = new SimpleIntegerProperty(nroTarjeta);
    }

    /**
     * @return the CodigoPago
     */
    public IntegerProperty codigoPagoProperty() {
        return CodigoPago;
    }


    /**
     * @return the Descripcion
     */
    public StringProperty descripcionProperty() {
        return Descripcion;
    }


    /**
     * @return the Monto
     */
    public ObjectProperty<BigDecimal> montoProperty() {
        return Monto;
    }


    /**
     * @return the CantidadCuotas
     */
    public IntegerProperty cantidadCuotasProperty() {
        return CantidadCuotas;
    }


    /**
     * @return the CodigoCupon
     */
    public IntegerProperty codigoCuponProperty() {
        return CodigoCupon;
    }
    
    /**
     * 
     */
    public IntegerProperty nroTarjetaProperty(){
        return NroTarjeta;
    }

    /**
     * @return the CodigoPago
     */
    public int getCodigoPago() {
        return codigoPagoProperty().get();
    }

    /**
     * @return the Descripcion
     */
    public String getDescripcion() {
        return descripcionProperty().get();
    }

    /**
     * @return the Monto
     */
    public BigDecimal getMonto() {
        return montoProperty().get();
    }

    /**
     * @return the CantidadCuotas
     */
    public int getCantidadCuotas() {
        return cantidadCuotasProperty().get();
    }

    /**
     * @return the CodigoCupon
     */
    public int getCodigoCupon() {
        return codigoCuponProperty().get();
    }

    public int getNroTarjeta(){
        return nroTarjetaProperty().get();
    }
    
    
}
