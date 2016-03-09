/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.cliente;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author daniel
 */
public class ClienteData {
    private IntegerProperty CodigoCliente;
    private StringProperty NombreCliente;
    private IntegerProperty Dni;
    private StringProperty Cuit;
    
    public ClienteData(int codigoCliente,String NombreCliente,
                    int dni,String cuit){
        this.CodigoCliente = new SimpleIntegerProperty(codigoCliente);
        this.NombreCliente = new SimpleStringProperty(NombreCliente);
        this.Dni = new SimpleIntegerProperty(dni);
        this.Cuit = new SimpleStringProperty(cuit);
    }
    
    
    public IntegerProperty CodigoClienteProperty(){
        return this.CodigoCliente;
    }
    
    public StringProperty NombreClienteProperty(){
        return this.NombreCliente;
    }
    
    public IntegerProperty DniProperty(){
        return this.Dni;
    }
    
    
    public StringProperty CuitProperty(){
        return this.Cuit;
    }
    
    public int getCodigoCliente(){
        return CodigoClienteProperty().get();
    }
    
    public int getDni(){
        return DniProperty().get();
    }
    
    public String getNombreCliente(){
        return NombreClienteProperty().get();
    }
    
    public String getCuit(){
        return CuitProperty().get();
    }
    
}
