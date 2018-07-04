/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.retirodinero;

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
public class RetiroDineroData {
    private IntegerProperty CodigoForma;
    private StringProperty DescripcionForma;
    private ObjectProperty<BigDecimal> ValorRetiro;
    private ObjectProperty<BigDecimal> CantidadBilletes;
    
    public RetiroDineroData(){
        
    }
    
    public RetiroDineroData(int codigoForma,String descripcionForma
        ,BigDecimal valorRetiro, BigDecimal cantidadBilletes){
        this.CodigoForma = new SimpleIntegerProperty(codigoForma);
        this.DescripcionForma = new SimpleStringProperty(descripcionForma);
        this.ValorRetiro = new SimpleObjectProperty(valorRetiro);
        this.CantidadBilletes = new SimpleObjectProperty<BigDecimal>(cantidadBilletes);
                
    }
    
    public IntegerProperty codigoFormaProperty(){
        if(CodigoForma == null)
            CodigoForma = new SimpleIntegerProperty();
        return CodigoForma;    
    }
    
    public StringProperty descripcionFormaProperty(){
        if(DescripcionForma == null)
            DescripcionForma = new SimpleStringProperty();
        return DescripcionForma;
    }
    
    public ObjectProperty<BigDecimal> valorRetiroProperty(){
        if(ValorRetiro == null)
            ValorRetiro = new SimpleObjectProperty<BigDecimal>();
        return ValorRetiro;    
    }
    
    public ObjectProperty<BigDecimal> cantidadBilletesProperty(){
        if(CantidadBilletes == null)
            CantidadBilletes = new SimpleObjectProperty<BigDecimal>();
        return CantidadBilletes;
    }
    
    public int getCodigoForma(){
        return CodigoForma.get();
    }
    
    public String getDescripcionForma(){
        return DescripcionForma.get();
    }
    
    public BigDecimal getValorRetiro(){
        return ValorRetiro.get();
    }
    
    public BigDecimal getCantidadBilletes(){
        return CantidadBilletes.get();
    }
    
    public void setCantidadBilletes(BigDecimal cantidad){
        this.cantidadBilletesProperty().set(cantidad);
    }
}
