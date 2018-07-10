/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.retirodinero;

import com.tpv.modelo.enums.RetiroDineroEnum;
import java.math.BigDecimal;
import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author COMPUTOS
 */
public class RetiroDineroConfirmacionData {
    private LongProperty IdRetiro;
    private ObjectProperty<java.util.Date> FechaAlta;
    private StringProperty DescripcionBillete;
    private ObjectProperty<RetiroDineroEnum> Estado;
    private ObjectProperty<BigDecimal>  MontoTotal;
    
    public RetiroDineroConfirmacionData(){
    }
    
    public RetiroDineroConfirmacionData(Long idRetiro,java.sql.Date fechaAlta
        ,String descripcionBillete,RetiroDineroEnum estado,BigDecimal montoTotal){
        this.IdRetiro = new SimpleLongProperty(idRetiro);
        this.DescripcionBillete = new SimpleStringProperty(descripcionBillete);
        this.Estado = new SimpleObjectProperty(estado);
        this.MontoTotal = new SimpleObjectProperty(montoTotal);
    }
    
    public LongProperty idRetiroProperty(){
        if(IdRetiro == null)
            IdRetiro = new SimpleLongProperty();
        return IdRetiro;
    }
    
    public StringProperty descripcionBilleteProperty(){
        if(DescripcionBillete == null)
            DescripcionBillete = new SimpleStringProperty();
        return DescripcionBillete;    
    }
    
    public ObjectProperty<RetiroDineroEnum> estadoProperty(){
        if(Estado == null)
            Estado = new SimpleObjectProperty();
        return Estado;
    }
    
    public Long getIdRetiro(){
        return IdRetiro.get();
    }
    
    public void setIdRetiro(Long idRetiro){
        this.IdRetiro.set(idRetiro);
    }
    
    public String getDescripcionBillete(){
        return DescripcionBillete.get();
    }
    
    public void setDescripcionBillete(String descripcionBillete){
        this.DescripcionBillete.set(descripcionBillete);
    }
    
    public RetiroDineroEnum getEstado(){
        return Estado.get();
    }
    
    public void setEstado(RetiroDineroEnum estado){
        this.Estado.set(estado);
    }
    
}
