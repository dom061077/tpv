/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.principal;

import com.tpv.modelo.Cliente;
import com.tpv.modelo.FormaPago;
import java.math.BigDecimal;
import java.util.Iterator;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
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
    private ListProperty<LineaTicketData> tickets;
    private ListProperty<PagoTicketData> pagos;
    private Cliente cliente = null;
    private FormaPago formaPago = null;
    private int nroTicket;
    private int puntoVenta;//checkout
    private boolean clienteSelecciondo = false;
    
    
    public ListProperty<LineaTicketData> getTickets() {
        if (tickets == null) {
            ObservableList<LineaTicketData> innerList = FXCollections.observableArrayList();
            tickets = new SimpleListProperty<>(innerList);
        }
        return tickets;
    }
    
    public ListProperty<PagoTicketData> getPagos(){
        if(pagos == null){
            ObservableList<PagoTicketData> innerList = FXCollections.observableArrayList();
            pagos = new SimpleListProperty<>(innerList);            
        }
        return pagos;
    }
    
    public BigDecimal getTotalTicket(){
        ListProperty<LineaTicketData> innerList = getTickets();
        
        Double total = new Double(0);
        for(Iterator iter=innerList.iterator();iter.hasNext();){
            LineaTicketData ticket= (LineaTicketData)iter.next();
            total = total + ticket.getSubTotal().doubleValue();
        }
        
        return BigDecimal.valueOf(total);
    }

    public BigDecimal getTotalPagos(){
        ListProperty<PagoTicketData> innerList = getPagos();
        Double total = new Double(0);
        for(Iterator iter=innerList.iterator();iter.hasNext();){
            PagoTicketData pago = (PagoTicketData)iter.next();
            total = total + pago.getMontoPago().doubleValue();
        }
        
        return BigDecimal.valueOf(total);
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
    
}
