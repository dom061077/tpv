/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.modelo;

import java.math.BigDecimal;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author COMPUTOS
 */

@Entity
@Table(name="facturasdetallecombosabierto")
public class FacturaDetalleComboAbierto {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="idFACTURASDETALLECOMOSABIERTO")
    private Long id;
    
    @Column(name="cantidad")
    private BigDecimal cantidad;
    

    @Column(name="IMPUESTOINTERNO")
    private BigDecimal impuestoInterno;
    
    @Column(name="NETO")
    private BigDecimal neto;
    
    @Column(name="TOTAL")
    private BigDecimal total;
    
    @Column(name="IVA")
    private BigDecimal iva;
    
    @ManyToOne
    @JoinColumn(name = "idPRODUCTOS", referencedColumnName = "idPRODUCTOS", nullable=false)
    private Producto producto;
            
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "idFACTURADETALLECOMBOS", referencedColumnName = "idFACTURADETALLECOMBOS", nullable=false)
    private FacturaDetalleCombo fdCombo;    
    
}
