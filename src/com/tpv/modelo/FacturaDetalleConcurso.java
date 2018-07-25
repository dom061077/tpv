/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.modelo;

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
@Table(name="facturasdetalleconcurso")
public class FacturaDetalleConcurso {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "idFACTURASCONCURSO")
    private Long id;
    
    @Column(name = "CANTIDADCUPONES")
    private int cantidadCupones;
    
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "idFACTURAS", referencedColumnName = "idFACTURAS", nullable=false)
    private Factura factura;
    
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "idCONCURSOS", referencedColumnName = "idCONCURSOS", nullable = false)
    private Concurso concurso;

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the cantidadCupones
     */
    public int getCantidadCupones() {
        return cantidadCupones;
    }

    /**
     * @param cantidadCupones the cantidadCupones to set
     */
    public void setCantidadCupones(int cantidadCupones) {
        this.cantidadCupones = cantidadCupones;
    }

    /**
     * @return the factura
     */
    public Factura getFactura() {
        return factura;
    }

    /**
     * @param factura the factura to set
     */
    public void setFactura(Factura factura) {
        this.factura = factura;
    }

    /**
     * @return the concurso
     */
    public Concurso getConcurso() {
        return concurso;
    }

    /**
     * @param concurso the concurso to set
     */
    public void setConcurso(Concurso concurso) {
        this.concurso = concurso;
    }
}
