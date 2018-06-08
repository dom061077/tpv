/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.pagoticket;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author COMPUTOS
 */
public class LineaFormaPagoData {
    private IntegerProperty CodigoForma;
    private StringProperty DescripcionForma;
    
    public LineaFormaPagoData(){
        
    }
    
    public LineaFormaPagoData(int codigoForma, String descripcionForma){
        this.CodigoForma = new SimpleIntegerProperty(codigoForma);
        this.DescripcionForma = new SimpleStringProperty(descripcionForma);
    }
            
    
    public IntegerProperty codigoFormaProperty(){
        return CodigoForma;
    }
    
    public StringProperty descripcionProperty(){
        return DescripcionForma;
    }
    
    public int getCodigoForma(){
        return CodigoForma.getValue();
    }
    
    public String getDescripcionForma(){
        return DescripcionForma.getValue();
    }
}
