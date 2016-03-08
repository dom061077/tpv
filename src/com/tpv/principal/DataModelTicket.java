/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.principal;

import com.tpv.exceptions.TpvException;
import com.tpv.modelo.Cliente;
import com.tpv.modelo.FormaPago;
import com.tpv.pagoticket.LineaPagoData;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Iterator;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.datafx.controller.flow.injection.FlowScoped;
//import org.datafx.samples.app.Person;

/**
 *
 * @author daniel
 */

@FlowScoped
public class DataModelTicket {
    private ListProperty<LineaTicketData> detalle;
    private ListProperty<LineaPagoData> pagos;
    private Cliente cliente = null;
    private FormaPago formaPago = null;
    private TpvException exception;
    private int nroTicket;
    private int puntoVenta;//checkout
    private boolean clienteSelecciondo = false;
    private int codigoProdSelecEnBuscarPorDesc;
    
    public DataModelTicket(){
    }
    
    public ListProperty<LineaTicketData> getDetalle() {
        if (detalle == null) {
            ObservableList<LineaTicketData> innerList = FXCollections.observableArrayList();
            detalle = new SimpleListProperty<>(innerList);
        }
        return detalle;
    }
    
    public ListProperty<LineaPagoData> getPagos(){
        if(pagos == null){
            ObservableList<LineaPagoData> innerList = FXCollections.observableArrayList();
            pagos = new SimpleListProperty<>(innerList);            
        }
        return pagos;
    }
    
    public BigDecimal getTotalTicket(){
        ListProperty<LineaTicketData> innerList = getDetalle();
        
        BigDecimal total = new BigDecimal(0);
        for(Iterator iter=innerList.iterator();iter.hasNext();){
            LineaTicketData ticket= (LineaTicketData)iter.next();
            //total = total + ticket.getSubTotal().doubleValue();
            total = total.add(ticket.getSubTotal());
        }
        
        return total;
    }

    public BigDecimal getTotalPagos(){
        ListProperty<LineaPagoData> innerList = getPagos();
        Double total = new Double(0);
        for(Iterator iter=innerList.iterator();iter.hasNext();){
            LineaPagoData pago = (LineaPagoData)iter.next();
            total = total + pago.getMonto().doubleValue();
        }
        
        return BigDecimal.valueOf(total);
    }
    
    public BigDecimal getSaldo(){
        BigDecimal saldo = getTotalTicket().subtract(getTotalPagos());
        saldo = saldo.setScale(2,BigDecimal.ROUND_HALF_EVEN);
        return saldo;
    }
    

    public String getFormatSaldo(){
        DecimalFormat df = new DecimalFormat("###,###,###,##0.00");
        return df.format(getSaldo());
    }
    
    /**
     * @return the cliente
     */
    public Cliente getCliente() {
        return cliente;
    }

    /**
     * @param cliente the cliente to set
     */
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
    
    public void setClienteSeleccionado(boolean seleccionado){
        this.clienteSelecciondo = seleccionado;
    }
    
    public boolean isClienteSeleccionado(){
        return this.clienteSelecciondo;
    }

    /**
     * @return the nroTicket
     */
    public int getNroTicket() {
        return nroTicket;
    }

    /**
     * @param nroTicket the nroTicket to set
     */
    public void setNroTicket(int nroTicket) {
        this.nroTicket = nroTicket;
    }

    /**
     * @return the puntoVenta
     */
    public int getPuntoVenta() {
        return puntoVenta;
    }

    /**
     * @param puntoVenta the puntoVenta to set
     */
    public void setPuntoVenta(int puntoVenta) {
        this.puntoVenta = puntoVenta;
    }
    
    
    public TpvException getTpvException(){
        return this.exception;
    }
    
    public void setException(TpvException e){
        this.exception = e;
    }
    
    public int getCodigoProdSelecEnBuscarPorDesc(){
        return this.codigoProdSelecEnBuscarPorDesc;
    }
    
    public void setCodigoProdSelecEnBuscarPorDesc(int codigoProducto){
        this.codigoProdSelecEnBuscarPorDesc = codigoProducto;
    }
    
}
