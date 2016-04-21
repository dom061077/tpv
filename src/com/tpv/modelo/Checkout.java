/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.modelo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author daniel
 */
@Entity
@Table(name="checkout")
public class Checkout {
    @Id
    @Column(name = "idCHECKOUT")
    private int id;
    
    @Column(name = "DETALLE")
    private String detalle;
    
    @Column(name = "PLACA")
    private String placa;
    
    @Column(name = "POSNET")
    private int posnet;
    
    @Column(name = "HABILITADO", columnDefinition = "TINYINT(1)")
    private boolean habitado;
    
}
