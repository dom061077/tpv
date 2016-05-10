/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.modelo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.annotations.Formula;

/**
 *
 * @author daniel
 */

@Entity
@Table(name="combos")
public class Combo {
    @Id
    @Column(name = "idCOMBOS")
    private Long id;

    @Column(name = "DESCRIPCION")
    private String descripcion;
    
    @Column(name = "FECHADESDE")
    private java.sql.Date fechaDesde;
            
    @Column(name = "FECHAHASTA")
    private java.sql.Date fechaHasta;
    
    @OneToMany(cascade = CascadeType.ALL,mappedBy="combo")
    private List<ComboGrupo> combosGrupo = new ArrayList<ComboGrupo>();

    
    @Transient
    private BigDecimal totalBonificacion;
    
    
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
     * @return the fechaDesde
     */
    public java.sql.Date getFechaDesde() {
        return fechaDesde;
    }

    /**
     * @param fechaDesde the fechaDesde to set
     */
    public void setFechaDesde(java.sql.Date fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    /**
     * @return the fechaHasta
     */
    public java.sql.Date getFechaHasta() {
        return fechaHasta;
    }

    /**
     * @param fechaHasta the fechaHasta to set
     */
    public void setFechaHasta(java.sql.Date fechaHasta) {
        this.fechaHasta = fechaHasta;
    }

    /**
     * @return the combosGrupo
     */
    public List<ComboGrupo> getCombosGrupo() {
        return combosGrupo;
    }

    /**
     * @param combosGrupo the combosGrupo to set
     */
    public void setCombosGrupo(List<ComboGrupo> combosGrupo) {
        this.combosGrupo = combosGrupo;
    }

    

    /**
     * @return the totalBonificacion
     */
    public BigDecimal getTotalBonificacion() {
        return totalBonificacion;
    }

    /**
     * @param totalBonificacion the totalBonificacion to set
     */
    public void setTotalBonificacion(BigDecimal totalBonificacion) {
        this.totalBonificacion = totalBonificacion;
    }

    
 

    /**
     * @return the cumpleCondicion
     */
    public boolean cumpleCondicion() {
        
        for(Iterator<ComboGrupo> iterator = combosGrupo.iterator();iterator.hasNext();){
            ComboGrupo grupo = iterator.next();
            if((grupo.getCantidadAcumulada() / grupo.getCantidad())==0){
                return false;
            }
            
        }
        return true;
    }
    
    @Transient
    public int getCantidadCombosArmados(){
        int cantidadArmados=0;
        if(getCombosGrupo().size()>0)
            cantidadArmados = getCombosGrupo().get(0).getCantidadGrupos();
        for(Iterator<ComboGrupo> iterator = getCombosGrupo().iterator();iterator.hasNext();){
            ComboGrupo gp = iterator.next();
            if(cantidadArmados>gp.getCantidadGrupos()){
                cantidadArmados = gp.getCantidadGrupos();
            }
        }
        return cantidadArmados;
    }
    
    @Transient
    public BigDecimal getBonificacion(){
        BigDecimal bonificacion= new BigDecimal(0);
        for(Iterator<ComboGrupo> iterator = getCombosGrupo().iterator();iterator.hasNext();){
            ComboGrupo gp = iterator.next();
            //if(gp.)
        }
        return bonificacion;        
    }
    
    

    
    
}
