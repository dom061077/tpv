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
@Table(name="empresa")
public class Empresa {
    @Id
    @Column(name = "idEMPRESA")
    private int id;
    
    @Column(name = "RAZONSOCIAL")
    private String razonSocial;
    
}
