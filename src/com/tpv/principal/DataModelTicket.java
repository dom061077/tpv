/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.principal;

import com.tpv.enums.OrigenPantallaErrorEnum;
import com.tpv.enums.TipoTituloSupervisorEnum;
import com.tpv.exceptions.TpvException;
import com.tpv.modelo.Checkout;
import com.tpv.modelo.Cliente;
import com.tpv.modelo.FormaPago;
import com.tpv.modelo.Usuario;
import com.tpv.pagoticket.LineaPagoData;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Iterator;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
//import org.datafx.samples.app.Person;

/**
 *
 * @author daniel
 */


public class DataModelTicket {

    /**
     * @param totalIva the totalIva to set
     */
    public void setTotalIva(BigDecimal totalIva) {
        this.totalIva = totalIva;
    }

    /**
     * @param totalNeto the totalNeto to set
     */
    public void setTotalNeto(BigDecimal totalNeto) {
        this.totalNeto = totalNeto;
    }
    private ListProperty<LineaTicketData> detalle;
    private ListProperty<LineaPagoData> pagos;
    private Usuario usuario = null;
    private Usuario usuarioSupervisor = null;
    private Cliente cliente = null;
    private FormaPago formaPago = null;
    private TpvException exception;
    private int nroTicket;
    private Long puntoVenta;//checkout
    private Checkout checkout;
    private boolean clienteSelecciondo = false;
    private int codigoProdSelecEnBuscarPorDesc;
    private int codigoClienteSelecEnBuscarPorDesc;
    private boolean imprimeComoNegativo = false;
    private TipoTituloSupervisorEnum tipoTituloSupervisor;
    private Long idFactura;
    private OrigenPantallaErrorEnum origenPantalla;
    private boolean reinicioVerificado=false;
    private BigDecimal bonificaciones = BigDecimal.ZERO;
    private boolean ticketAbierto = false;
    private String modeloImpresora = "";
    private BigDecimal totalNeto;
    private BigDecimal totalIva;
    private BigDecimal totalImpuestoInterno;
    private BigDecimal totalExento;
    private BigDecimal retencion = BigDecimal.ZERO;
    
    
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
        total = total.add(getRetencion());
        
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
    
    public BigDecimal getTotalIva(){
        ListProperty<LineaTicketData> innerList = getDetalle();
        
        BigDecimal totalIva = new BigDecimal(0);
        for(Iterator iter = innerList.iterator();iter.hasNext();){
            LineaTicketData ticket=(LineaTicketData)iter.next();
            totalIva = totalIva.add(ticket.getIva());
            totalIva = totalIva.add(ticket.getIvaReducido());
        }
        return totalIva;
    }
    
    public BigDecimal getTotalInterno(){
        ListProperty<LineaTicketData> innerList = getDetalle();
        BigDecimal totalInterno = BigDecimal.valueOf(0);
        for(Iterator iter = innerList.iterator();iter.hasNext();){
            LineaTicketData ticket=(LineaTicketData)iter.next();
            totalInterno = totalInterno.add(ticket.getImpuestoInterno());
        }
        
        return totalInterno;
    }
    
    public BigDecimal getTotalExento(){
        ListProperty<LineaTicketData> innerList = getDetalle();
        BigDecimal totalExento = BigDecimal.valueOf(0);
        for(Iterator iter = innerList.iterator();iter.hasNext();){
            LineaTicketData ticket = (LineaTicketData)iter.next();
            totalExento = totalExento.add(ticket.getExento());
        }
        return totalExento;
    }
    
    
    /**
     * Total Base imponible
     */
    public BigDecimal getTotalNeto(){
        ListProperty<LineaTicketData> innerList = getDetalle();
        BigDecimal totalNeto = BigDecimal.valueOf(0);
        for(Iterator iter = innerList.iterator();iter.hasNext();){
            LineaTicketData ticket = (LineaTicketData) iter.next();
            totalNeto = totalNeto.add(ticket.getNeto());
            totalNeto = totalNeto.add(ticket.getNetoReducido());
        }
        return totalNeto;
    }
    
    public BigDecimal getSaldo(){
        BigDecimal saldo = getTotalTicket().subtract(getTotalPagos());
        saldo = saldo.subtract(getBonificaciones());
        saldo = saldo.setScale(2,BigDecimal.ROUND_HALF_EVEN);
        return saldo;
    }
    
    public BigDecimal getBonificacionPorPagoTotal(){
        BigDecimal bonifTotal = BigDecimal.ZERO;
        ListProperty<LineaPagoData> innerList = getPagos();
        for(Iterator iter=innerList.iterator();iter.hasNext();){
            LineaPagoData pago = (LineaPagoData)iter.next();
            bonifTotal = bonifTotal.add(pago.getBonificacion());
        }
        return bonifTotal;
    }
    
    public BigDecimal getInteresPorPagoTotal(){
        BigDecimal interesTotal = BigDecimal.ZERO;
        ListProperty<LineaPagoData> innerList = getPagos();
        for(Iterator iter=innerList.iterator();iter.hasNext();){
            LineaPagoData pago = (LineaPagoData)iter.next();
            interesTotal = interesTotal.add(pago.getInteres());
        }
        
        return interesTotal;
    }
    
    public String getFormatBonificacionPorPagoTotal(){
        DecimalFormat df = new DecimalFormat("###,###,###,##0.00");
        return df.format(getBonificacionPorPagoTotal().doubleValue());
    }
    
    public String getFormatInteresPorPagoTotal(){
        DecimalFormat df = new DecimalFormat("###,###,###,##0.00");
        return df.format(getInteresPorPagoTotal().doubleValue());
    }
    
    public String getFormatSaldo(){
        DecimalFormat df = new DecimalFormat("###,###,###,##0.00");
        return df.format(getSaldo().abs());
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
    public Long getPuntoVenta() {
        return puntoVenta;
    }

    /**
     * @param puntoVenta the puntoVenta to set
     */
    public void setPuntoVenta(Long puntoVenta) {
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

    /**
     * @return the imprimeComoNegativo
     */
    public boolean isImprimeComoNegativo() {
        return imprimeComoNegativo;
    }

    /**
     * @param imprimeComoNegativo the imprimeComoNegativo to set
     */
    public void setImprimeComoNegativo(boolean imprimeComoNegativo) {
        this.imprimeComoNegativo = imprimeComoNegativo;
    }

    /**
     * @return the tipoTituloSupervisor
     */
    public TipoTituloSupervisorEnum getTipoTituloSupervisor() {
        return tipoTituloSupervisor;
    }

    /**
     * @param tipoTituloSupervisor the tipoTituloSupervisor to set
     */
    public void setTipoTituloSupervisor(TipoTituloSupervisorEnum tipoTituloSupervisor) {
        this.tipoTituloSupervisor = tipoTituloSupervisor;
    }

    /**
     * @return the codigoClienteSelecEnBuscarPorDesc
     */
    public int getCodigoClienteSelecEnBuscarPorDesc() {
        return codigoClienteSelecEnBuscarPorDesc;
    }

    /**
     * @param codigoClienteSelecEnBuscarPorDesc the codigoClienteSelecEnBuscarPorDesc to set
     */
    public void setCodigoClienteSelecEnBuscarPorDesc(int codigoClienteSelecEnBuscarPorDesc) {
        this.codigoClienteSelecEnBuscarPorDesc = codigoClienteSelecEnBuscarPorDesc;
    }

    /**
     * @return the usuario
     */
    public Usuario getUsuario() {
        return usuario;
    }

    /**
     * @param usuario the usuario to set
     */
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    
    public void limpiarModelTicket(){
        this.cliente=null;
        this.clienteSelecciondo=false;
        this.codigoClienteSelecEnBuscarPorDesc=0;
        this.codigoProdSelecEnBuscarPorDesc=0;
        
                
                
    }

    /**
     * @return the idFactura
     */
    public Long getIdFactura() {
        return idFactura;
    }

    /**
     * @param idFactura the idFactura to set
     */
    public void setIdFactura(Long idFactura) {
        this.idFactura = idFactura;
    }

    /**
     * @return the orgienPantalla
     */
    public OrigenPantallaErrorEnum getOrigenPantalla() {
        return origenPantalla;
    }

    /**
     * @param orgienPantalla the orgienPantalla to set
     */
    public void setOrigenPantalla(OrigenPantallaErrorEnum orgienPantalla) {
        this.origenPantalla = orgienPantalla;
    }

    /**
     * @return the checkout
     */
    public Checkout getCheckout() {
        return checkout;
    }

    /**
     * @param checkout the checkout to set
     */
    public void setCheckout(Checkout checkout) {
        this.checkout = checkout;
    }

    /**
     * @return the reinicioVerificado
     */
    public boolean isReinicioVerificado() {
        return reinicioVerificado;
    }

    /**
     * @param reinicioVerificado the reinicioVerificado to set
     */
    public void setReinicioVerificado(boolean reinicioVerificado) {
        this.reinicioVerificado = reinicioVerificado;
    }

    /**
     * @return the bonificaciones
     */
    public BigDecimal getBonificaciones() {
        return bonificaciones;
    }

    /**
     * @param bonificaciones the bonificaciones to set
     */
    public void setBonificaciones(BigDecimal bonificaciones) {
        this.bonificaciones = bonificaciones;
    }

    /**
     * @return the ticketAbierto
     */
    public boolean isTicketAbierto() {
        return ticketAbierto;
    }

    /**
     * @param ticketAbierto the ticketAbierto to set
     */
    public void setTicketAbierto(boolean ticketAbierto) {
        this.ticketAbierto = ticketAbierto;
    }
    
    public void setModeloImpresora(String modelo){
        this.modeloImpresora = modelo;
    }
    
    public String getModeloImpresora(){
        return this.modeloImpresora;
    }

    /**
     * @return the totalImpuestoInterno
     */
    public BigDecimal getTotalImpuestoInterno() {
        return totalImpuestoInterno;
    }

    /**
     * @param totalImpuestoInterno the totalImpuestoInterno to set
     */
    public void setTotalImpuestoInterno(BigDecimal totalImpuestoInterno) {
        this.totalImpuestoInterno = totalImpuestoInterno;
    }

    /**
     * @return the totalGral con cálculo local
     */
    public BigDecimal getTotalGral() {
        BigDecimal totalGral = getTotalTicket();
        
        totalGral = totalGral.subtract(getBonificaciones());
        totalGral = totalGral.subtract(getBonificacionPorPagoTotal());
        totalGral = totalGral.add(getInteresPorPagoTotal());
        totalGral = totalGral.setScale(2,BigDecimal.ROUND_HALF_EVEN);
        return totalGral;
    }

    public String getFormatTotalGral(){
        DecimalFormat df = new DecimalFormat("###,###,###,##0.00");
        BigDecimal totalGralAFormatear = getTotalGral();
        return df.format(totalGralAFormatear.doubleValue());
    }

    /**
     * @return the retencion
     */
    public BigDecimal getRetencion() {
        return retencion;
    }

    /**
     * @param retencion the retencion to set
     */
    public void setRetencion(BigDecimal retencion) {
        this.retencion = retencion;
    }

    /**
     * @return the usuarioSupervisor
     */
    public Usuario getUsuarioSupervisor() {
        return usuarioSupervisor;
    }

    /**
     * @param usuarioSupervisor the usuarioSupervisor to set
     */
    public void setUsuarioSupervisor(Usuario usuarioSupervisor) {
        this.usuarioSupervisor = usuarioSupervisor;
    }
    
    
    
}
