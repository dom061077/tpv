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
    
    public LineaPagoData(){
        
    }
    
    public LineaPagoData(int codigoPago,String descripcion,BigDecimal monto
            ,int cantidadCuotas, int codigoCupon){
        this.CodigoPago = new SimpleIntegerProperty(codigoPago);
        this.Descripcion = new SimpleStringProperty(descripcion);
        this.Monto = new SimpleObjectProperty(monto);
        this.CantidadCuotas = new SimpleIntegerProperty(cantidadCuotas);
        this.CodigoCupon = new SimpleIntegerProperty(codigoCupon);
    }

    /**
     * @return the CodigoPago
     */
    public IntegerProperty getCodigoPago() {
        return CodigoPago;
    }

    /**
     * @param CodigoPago the CodigoPago to set
     */
    public void setCodigoPago(IntegerProperty CodigoPago) {
        this.CodigoPago = CodigoPago;
    }

    /**
     * @return the Descripcion
     */
    public StringProperty getDescripcion() {
        return Descripcion;
    }

    /**
     * @param Descripcion the Descripcion to set
     */
    public void setDescripcion(StringProperty Descripcion) {
        this.Descripcion = Descripcion;
    }

    /**
     * @return the Monto
     */
    public ObjectProperty<BigDecimal> getMonto() {
        return Monto;
    }

    /**
     * @param Monto the Monto to set
     */
    public void setMonto(ObjectProperty<BigDecimal> Monto) {
        this.Monto = Monto;
    }

    /**
     * @return the CantidadCuotas
     */
    public IntegerProperty getCantidadCuotas() {
        return CantidadCuotas;
    }

    /**
     * @param CantidadCuotas the CantidadCuotas to set
     */
    public void setCantidadCuotas(IntegerProperty CantidadCuotas) {
        this.CantidadCuotas = CantidadCuotas;
    }

    /**
     * @return the CodigoCupon
     */
    public IntegerProperty getCodigoCupon() {
        return CodigoCupon;
    }

    /**
     * @param CodigoCupon the CodigoCupon to set
     */
    public void setCodigoCupon(IntegerProperty CodigoCupon) {
        this.CodigoCupon = CodigoCupon;
    }
    
    
}
