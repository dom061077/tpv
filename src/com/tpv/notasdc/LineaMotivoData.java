/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.notasdc;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author COMPUTOS
 */
public class LineaMotivoData {
    private IntegerProperty CodigoMotivo;
    private StringProperty Descripcion;
    
    public LineaMotivoData(){
        
    }
    
    public LineaMotivoData(int codigo,String descripcion){
        this.CodigoMotivo = new SimpleIntegerProperty(codigo);
        this.Descripcion = new SimpleStringProperty(descripcion);
        
    }
    
    public IntegerProperty codigoMotivoProperty(){
        return CodigoMotivo;
    }
    
    public StringProperty descripcionProperty(){
        return Descripcion;
    }
    
    public int getCodigoMotivo(){
        return CodigoMotivo.get();
    }
    
    public String getDescripcion(){
        return Descripcion.get();
    }
    
}
