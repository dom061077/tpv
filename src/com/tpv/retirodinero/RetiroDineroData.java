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
    private IntegerProperty IdBillete;
    private StringProperty DescripcionBillete;
    private ObjectProperty<BigDecimal> ValorRetiro;
    private IntegerProperty CantidadBilletes;
    
    public RetiroDineroData(){
        
    }
    
    public RetiroDineroData(int idBillete,String descripcionBillete
        ,BigDecimal valorRetiro, int cantidadBilletes){
        this.IdBillete = new SimpleIntegerProperty(idBillete);
        this.DescripcionBillete = new SimpleStringProperty(descripcionBillete);
        this.ValorRetiro = new SimpleObjectProperty(valorRetiro);
        this.CantidadBilletes = new SimpleIntegerProperty(cantidadBilletes);
                
    }
    
    public IntegerProperty idBilleteProperty(){
        if(IdBillete == null)
            IdBillete = new SimpleIntegerProperty();
        return IdBillete;    
    }
    
    public StringProperty descripcionFormaProperty(){
        if(DescripcionBillete == null)
            DescripcionBillete = new SimpleStringProperty();
        return DescripcionBillete;
    }
    
    public ObjectProperty<BigDecimal> valorRetiroProperty(){
        if(ValorRetiro == null)
            ValorRetiro = new SimpleObjectProperty<BigDecimal>();
        return ValorRetiro;    
    }
    
    public IntegerProperty cantidadBilletesProperty(){
        if(CantidadBilletes == null)
            CantidadBilletes = new SimpleIntegerProperty();
        return CantidadBilletes;
    }
    
    public int getIdBillete(){
        return IdBillete.get();
    }
    
    public String getDescripcionBillete(){
        return DescripcionBillete.get();
    }
    
    public BigDecimal getValorRetiro(){
        return ValorRetiro.get();
    }
    
    public int getCantidadBilletes(){
        return CantidadBilletes.get();
    }
    
    public void setCantidadBilletes(int cantidad){
        this.cantidadBilletesProperty().set(cantidad);
    }
}
