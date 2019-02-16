/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.modelo;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.Formula;

/**
 *
 * @author daniel
 */

@Entity
@Table(name="bonificacioncliente")
public class BonificacionCliente {

    /**
     * @return the montoAcumuladoTotal
     */
    public BigDecimal getMontoAcumuladoTotal() {
        return montoAcumuladoTotal;
    }

    /**
     * @param montoAcumuladoTotal the montoAcumuladoTotal to set
     */
    public void setMontoAcumuladoTotal(BigDecimal montoAcumuladoTotal) {
        this.montoAcumuladoTotal = montoAcumuladoTotal;
    }

    /**
     * @return the mesAnioCalc
     */
    public String getMesAnioCalc() {
        return mesAnioCalc;
    }

    /**
     * @param cliente the cliente to set
     */
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
    @Id
    @Column(name = "idBONIFICACIONCLIENTE")
    private int id;    
    
    @Column(name = "MESANO")
    private String mesAnio;
    
    @Column(name = "MONTOACUMULADO")
    private BigDecimal montoAcumulado;
    
    @Column(name = "MONTOACUMULADOTOTAL")
    private BigDecimal montoAcumuladoTotal;
    
    @Formula("(SELECT (DATE_FORMAT(NOW(),'%m%Y')))")
    private String mesAnioCalc;
    
    
    @ManyToOne
    @JoinColumn(name = "idCLIENTES", referencedColumnName = "idCLIENTES", nullable=true)
    private Cliente cliente;    

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the mesAnio
     */
    public String getMesAnio() {
        return mesAnio;
    }

    /**
     * @param mesAnio the mesAnio to set
     */
    public void setMesAnio(String mesAnio) {
        this.mesAnio = mesAnio;
    }

    /**
     * @return the montoAcumulado
     */
    public BigDecimal getMontoAcumulado() {
        return montoAcumulado;
    }

    /**
     * @param montoAcumulado the montoAcumulado to set
     */
    public void setMontoAcumulado(BigDecimal montoAcumulado) {
        this.montoAcumulado = montoAcumulado;
    }

    /**
     * @return the cliente
     */
    public Cliente getCliente() {
        return cliente;
    }
    
    public void addMontoAcumulado(BigDecimal incremento){
        montoAcumulado = montoAcumulado.add(incremento);
    }
    
    public void addMontoAcumuladoTotal(BigDecimal incremento){
        montoAcumuladoTotal = montoAcumuladoTotal.add(incremento);
    }
    
}
