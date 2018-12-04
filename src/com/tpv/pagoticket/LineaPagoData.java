/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.pagoticket;

import com.mchange.v2.codegen.bean.SimpleProperty;
import java.math.BigDecimal;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
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
    private ObjectProperty<BigDecimal> CambioCliente;
    private IntegerProperty CantidadCuotas;
    private StringProperty CodigoCupon;
    private StringProperty NroTarjeta;
    private StringProperty Terminal;
    private StringProperty NumeroLote;
    private StringProperty DniCliente;
    private ObjectProperty<BigDecimal> Interes;
    private ObjectProperty<BigDecimal> Bonificacion;
    private ObjectProperty<BigDecimal> IvaInteres;
    private ObjectProperty<BigDecimal> IvaBonificacion;
    private ObjectProperty<BigDecimal> Porcentaje;
    
    
    public LineaPagoData(){
        
    }
    
    public LineaPagoData(int codigoPago,String descripcion,BigDecimal monto
            ,BigDecimal cambioCliente
            ,int cantidadCuotas,String nroTarjeta, String codigoCupon
            ,String terminal,String numeroLote, String dniCliente
            ,BigDecimal porcentaje
            ,BigDecimal interesTarjeta,BigDecimal bonificacionTarjeta
            ,BigDecimal ivaInteres,BigDecimal ivaBonificacion){
        this.CodigoPago = new SimpleIntegerProperty(codigoPago);
        this.Descripcion = new SimpleStringProperty(descripcion);
        this.Monto = new SimpleObjectProperty(monto);
        this.CambioCliente = new SimpleObjectProperty(cambioCliente);
        this.CantidadCuotas = new SimpleIntegerProperty(cantidadCuotas);
        this.CodigoCupon = new SimpleStringProperty(codigoCupon);
        this.NroTarjeta = new SimpleStringProperty(nroTarjeta);
        this.Terminal = new SimpleStringProperty(terminal);
        this.NumeroLote = new SimpleStringProperty();
        this.Porcentaje = new SimpleObjectProperty(porcentaje);
        this.Interes =new SimpleObjectProperty(interesTarjeta);
        this.Bonificacion = new SimpleObjectProperty(bonificacionTarjeta);
        this.IvaBonificacion = new SimpleObjectProperty(ivaBonificacion);
        this.IvaInteres = new SimpleObjectProperty(ivaInteres);
        this.DniCliente = new SimpleStringProperty(dniCliente);
        
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
    
    public ObjectProperty<BigDecimal> cambioCliente(){
        return CambioCliente;
    }
            
    
    public ObjectProperty<BigDecimal> interesProperty(){
        return Interes;
    }
    
    public ObjectProperty<BigDecimal> bonificacionProperty(){
        return Bonificacion;
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
    public StringProperty codigoCuponProperty() {
        return CodigoCupon;
    }
    
    /**
     * 
     */
    public StringProperty nroTarjetaProperty(){
        return NroTarjeta;
    }
    
    public StringProperty terminalProperty(){
        return Terminal;
    }
    
    public StringProperty numeroLoteProperty(){
        return NumeroLote;
    }
    
    public StringProperty dniClienteProperty(){
        return DniCliente;
    }
            
    public ObjectProperty<BigDecimal> porcentajeProperty(){
        return Porcentaje;
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
    
    public BigDecimal getCambioCliente(){
        return cambioCliente().get();
    }
    
    
    public BigDecimal getInteres(){
        return interesProperty().get();
    }
    
    public BigDecimal getBonificacion(){
        return bonificacionProperty().get();
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
    public String getCodigoCupon() {
        return codigoCuponProperty().get();
    }

    public String getNroTarjeta(){
        return nroTarjetaProperty().get();
    }
    
    public String getTerminal(){
        return terminalProperty().get();
    }
    
    public String getNroLote(){
        return numeroLoteProperty().get();
    }
    
    public String getDniCliente(){
        return dniClienteProperty().get();
    }

    /**
     * @return the IvaInteres
     */
    public BigDecimal getIvaInteres() {
        return IvaInteres.get();
    }


    /**
     * @return the IvaBoficacion
     */
    public BigDecimal getIvaBonficacion() {
        return IvaBonificacion.get();
    }

    public BigDecimal getPorcentaje() {
        return Porcentaje.get();
    }
    
    
}
