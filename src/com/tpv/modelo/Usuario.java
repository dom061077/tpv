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
import org.hibernate.annotations.Formula;

/**
 *
 * @author daniel
 */
@Entity
@Table(name="usuarios")
public class Usuario {
    @Id
    @Column(name="idUSUARIOS")
    private int idUsuario;
    

    
    @Column(name="NOMBRE")
    private String nombre;
    @Column(name="PASSWORD")
    private String password;
    
    @Column(name="CODIGOBARRA")
    private String codigoBarra;
    
    @Formula("(SELECT NOW())")
    private java.util.Date fechaHoy;


    /**
     * @return the idUsuario
     */
    public int getIdUsuario() {
        return idUsuario;
    }

    /**
     * @return the nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @param nombre the nombre to set
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the codigoBarra
     */
    public String getCodigoBarra() {
        return codigoBarra;
    }

    /**
     * @param codigoBarra the codigoBarra to set
     */
    public void setCodigoBarra(String codigoBarra) {
        this.codigoBarra = codigoBarra;
    }

    /**
     * @return the fechaHoy
     */
    public java.util.Date getFechaHoy() {
        return fechaHoy;
    }


}
