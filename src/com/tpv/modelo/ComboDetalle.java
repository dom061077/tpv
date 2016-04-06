/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.modelo;

import java.math.BigDecimal;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 *
 * @author daniel
 */
public class ComboDetalle {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "idCOMBOS")
    private Long id;
    
    @Column(name = "CANTIDADPRODUCTOS")
    BigDecimal cantidad;
            
    @Column(name = "PORCENTAJE")
    BigDecimal porcentaje;
        
    
    @ManyToOne
    @JoinColumn(name = "idPRODUCTOS", referencedColumnName = "idPRODUCTOS", nullable=true)
    private Producto producto;  
    
    @ManyToOne
    @JoinColumn(name = "idGRUPRODUCTOS")
    private GrupoProducto grupoProducto;
    
    
    
}
