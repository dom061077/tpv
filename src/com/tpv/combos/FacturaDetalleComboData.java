/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.combos;

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

public class FacturaDetalleComboData {
    private IntegerProperty codigo;
    private StringProperty descripcion;
    private IntegerProperty cantidad;
    private ObjectProperty<BigDecimal> subTotal;
    private StringProperty observacion;
    
    public FacturaDetalleComboData(int codigo,String descripcion,int cantidad
        ,BigDecimal subTotal,String observacion){
        this.codigo = new SimpleIntegerProperty(codigo);
        this.descripcion = new SimpleStringProperty(descripcion);
        this.cantidad = new SimpleIntegerProperty(cantidad);
        this.subTotal = new SimpleObjectProperty<BigDecimal>(subTotal);
        this.observacion = new SimpleStringProperty(observacion);
    }

    /**
     * @return the codigo
     */
    public IntegerProperty codigoProperty() {
        return getCodigo();
    }

    /**
     * @return the descripcion
     */
    public StringProperty descripcionProperty() {
        return getDescripcion();
    }

    /**
     * @return the cantidad
     */
    public IntegerProperty cantidadProperty() {
        return getCantidad();
    }

    /**
     * @return the subTotal
     */
    public ObjectProperty<BigDecimal> subTotalProperty() {
        return getSubTotal();
    }
    
    
    public StringProperty observacionProperty(){
        return observacion;
    }

    /**
     * @return the codigo
     */
    public IntegerProperty getCodigo() {
        return codigo;
    }

    /**
     * @return the descripcion
     */
    public StringProperty getDescripcion() {
        return descripcion;
    }

    /**
     * @return the cantidad
     */
    public IntegerProperty getCantidad() {
        return cantidad;
    }

    /**
     * @return the subTotal
     */
    public ObjectProperty<BigDecimal> getSubTotal() {
        return subTotal;
    }
    
    
    
}
