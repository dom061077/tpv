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
import javax.persistence.Transient;
import org.hibernate.annotations.Formula;

/**
 *
 * @author daniel
 */
@Entity
@Table(name="tablausuarios")
public class Usuario {

    /**
     * @return the mesAnioCalc
     */
    public String getMesAnioCalc() {
        return mesAnioCalc;
    }

    /**
     * @return the nombreCompleto
     */
    public String getNombreCompleto() {
        return nombreCompleto;
    }

    /**
     * @param nombreCompleto the nombreCompleto to set
     */
    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    /**
     * @return the supervisor
     */
    public boolean isSupervisor() {
        return supervisor;
    }

    /**
     * @param supervisor the supervisor to set
     */
    public void setSupervisor(boolean supervisor) {
        this.supervisor = supervisor;
    }
    @Id
    @Column(name="idUSUARIO")
    private int idUsuario;
    

    
    @Column(name="Login")
    private String nombre;
    
    @Column(name="NombreUsuario")
    private String nombreCompleto;
    
    @Column(name="PasswordCajero")
    private String password;
    
    @Column(name="CodigoBarra")
    private String codigoBarra;
    
    @Column(name="EsSupervisor")
    private boolean supervisor; 
    
    
    @Formula("(SELECT current_date())")
    private java.util.Date fechaHoy;
    
    @Formula("(SELECT NOW())")
    private java.util.Date fechaHoraHoy;
    
    @Formula("(SELECT (DATE_FORMAT(NOW(),'%m%Y')))")
    private String mesAnioCalc;
    

    

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

    
    public java.util.Date getFechaHoraHoy(){
        return fechaHoraHoy;
    }

}
