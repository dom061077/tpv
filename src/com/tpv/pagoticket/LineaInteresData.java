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

/**
 *
 * @author COMPUTOS
 */
public class LineaInteresData {
    private IntegerProperty Cuotas;
    private ObjectProperty<BigDecimal> InteresBonif;
    private ObjectProperty<BigDecimal> MontoInteresBonif;
    private ObjectProperty<BigDecimal> TotalPago;
    
    public LineaInteresData(){
        
    }
    
    public LineaInteresData(int cuotas
                ,BigDecimal interesBonif,BigDecimal montoInteresBonif
                ,BigDecimal totalPago){
        this.Cuotas = new SimpleIntegerProperty(cuotas);
        this.InteresBonif = new SimpleObjectProperty(interesBonif);
        this.MontoInteresBonif = new SimpleObjectProperty(montoInteresBonif);
        this.TotalPago = new SimpleObjectProperty(totalPago);
    }
    
    
    public IntegerProperty cuotasProperty(){
        return Cuotas;
    }
    
    public ObjectProperty<BigDecimal> interesBonifProperty(){
        return InteresBonif;
    }
    
    public ObjectProperty<BigDecimal> montoInteresBonifProperty(){
        return MontoInteresBonif;
    }
    
    public ObjectProperty<BigDecimal> totalPagoProperty(){
        return TotalPago;
    }
}
