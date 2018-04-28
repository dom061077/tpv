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
import javax.persistence.Table;

/**
 *
 * @author COMPUTOS
 */

@Entity
@Table(name="PARAMETROSGRAL")
public class ParametroGeneral {
    @Id
    @Column(name = "idPARAMETROSGRAL")
    private String id;
    
    @Column(name = "DESCRIPCION")
    private String descripcion;
    
    @Column(name = "PARAMETRONUMERICO")
    private BigDecimal parametroNumerico;
    
    @Column(name = "PARAMETROCADENA")
    private String parametroCadena;

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the descripcion
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * @param descripcion the descripcion to set
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * @return the parametroNumerico
     */
    public BigDecimal getParametroNumerico() {
        return parametroNumerico;
    }

    /**
     * @param parametroNumerico the parametroNumerico to set
     */
    public void setParametroNumerico(BigDecimal parametroNumerico) {
        this.parametroNumerico = parametroNumerico;
    }

    /**
     * @return the parametroCadena
     */
    public String getParametroCadena() {
        return parametroCadena;
    }

    /**
     * @param parametroCadena the parametroCadena to set
     */
    public void setParametroCadena(String parametroCadena) {
        this.parametroCadena = parametroCadena;
    }
}
