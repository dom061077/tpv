/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.modelo;

import com.tpv.modelo.enums.ComboPrioridadEnum;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

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

    @Column(name = "PRIORIDAD",nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private ComboPrioridadEnum prioridad;
    
    @Column(name = "DESCRIPCION")
    private String descripcion;
    
    @Column(name = "FECHADESDE")
    private java.sql.Date fechaDesde;
            
    @Column(name = "FECHAHASTA")
    private java.sql.Date fechaHasta;
    
    @Column(name = "CODIGOCOMBO")
    private int codigoCombo;
    
    @Column(name = "CANTIDADMAXIMAPORPERSONA")
    private int cantidadMaxima;
    
    @Column(name = "COMBINARPRODUCTOS", columnDefinition = "TINYINT(1)") 
    private boolean combinarProductos;
    
    @OneToMany(cascade = CascadeType.ALL,mappedBy="combo")
    private List<ComboGrupo> combosGrupo = new ArrayList<ComboGrupo>();

    
    
    
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
     * @return the cumpleCondicion
     */
    public boolean cumpleCondicion() {
        for(Iterator<ComboGrupo> iterator = combosGrupo.iterator();iterator.hasNext();){
            ComboGrupo grupo = iterator.next();
            if(grupo.getCantidadGrupos()==0){
                return false;
            }

        }
        return true;
    }
    
    private void initCantidadAuxGrupoPrecioProducto(){
        for(Iterator<ComboGrupo> it = getCombosGrupo().iterator();it.hasNext();){
            ComboGrupo cg = it.next();
            cg.initCantidadDetPrecioProductoAux();
        }
    }
    
    @Transient
    public int getCantidadCombosArmados(){
        int cantidadArmados=0;
//        if(isCombinarProductos()){
            if(getCombosGrupo().size()>0)
                cantidadArmados = getCombosGrupo().get(0).getCantidadGrupos();
            for(Iterator<ComboGrupo> iterator = getCombosGrupo().iterator();iterator.hasNext();){
                ComboGrupo gp = iterator.next();
                if(cantidadArmados>gp.getCantidadGrupos()){
                    cantidadArmados = gp.getCantidadGrupos();
                }
            }
//        }else{
//            boolean hacerBucle=true,hayCombo;
//            initCantidadAuxGrupoPrecioProducto();
//            while(hacerBucle){
//                hayCombo=true;
//                for(Iterator<ComboGrupo> itG = getCombosGrupo().iterator();itG.hasNext();){
//                    ComboGrupo cg = itG.next();
//                    for(Iterator<ComboGrupoDetallePrecioProducto> itGPP = cg.getDetallePreciosProductos().iterator();itGPP.hasNext();){
//                        ComboGrupoDetallePrecioProducto cdPP = itGPP.next();
//                        if(cdPP.getCantidadAux()>=cdPP.getComboGrupo().getCantidad()){
//                            cdPP.decrementarCantAux(cg.getCantidad());
//                            break;
//                        }
//                        hayCombo=false;
//                    }
//                }
//                if(hayCombo)
//                    cantidadArmados+=1;
//                else{
//                    hacerBucle = false;
//                    break;
//                }
//                
//            }
//        }
        if(getCantidadMaxima()>0 && getCantidadMaxima()<cantidadArmados)
            return getCantidadMaxima();
        else
            return cantidadArmados;
    }
    
    @Transient
    public BigDecimal getBonificacionSinCombinacion(){
        BigDecimal bonificacion = BigDecimal.ZERO,bonificacionAux = BigDecimal.ZERO;
        
        //int cantidadMinimaPorGrupo=getCantidadMinimaPorGrupo();
        int cantidadMinimaPorGrupo=getCantidadCombosArmados();
        int contadorCantPorGrupo;
        initCantidadAuxGrupoPrecioProducto();
        for(Iterator<ComboGrupo> itG = getCombosGrupo().iterator();itG.hasNext();){
            ComboGrupo cg = itG.next();
            contadorCantPorGrupo=0;
            while(contadorCantPorGrupo<cantidadMinimaPorGrupo){
                for(Iterator<ComboGrupoDetallePrecioProducto> itGPP = cg.getDetallePreciosProductos().iterator();itGPP.hasNext();){
                    ComboGrupoDetallePrecioProducto cdPP = itGPP.next();
                    if(contadorCantPorGrupo>=cantidadMinimaPorGrupo) 
                        break;
                    
                    //cdPP.decrementarCantAux(cg.getCantidad());
                    if(cdPP.getPaf().getCantidad()>= cg.getCantidad()){
                        cdPP.getPaf().decCantidad(cg.getCantidad());
                        
                        bonificacionAux = cdPP.getPaf().getPrecioUnitario().multiply(cg.getPorcentaje().divide(BigDecimal.valueOf(100)));
                        bonificacionAux = bonificacionAux.multiply(BigDecimal.valueOf(cg.getCantidad()));
                        bonificacion=bonificacion.add(bonificacionAux);
                        contadorCantPorGrupo++;
                    }
                    
                    
                }
            }
            if(cg.getMonto().compareTo(BigDecimal.ZERO)>0){
                bonificacion = bonificacion.add(cg.getMonto().multiply(BigDecimal.valueOf(cantidadMinimaPorGrupo)));
            }
        }
        return bonificacion;
    }
    
   
    private boolean isMenorPrecioEnGrupo(ComboGrupo gp,ProductoAgrupadoEnFactura pafParam){
        ProductoAgrupadoEnFactura paf=null;
        if(gp.getDetallePreciosProductos().size()>0)
            paf = gp.getDetallePreciosProductos().get(0).getPaf();
        
        for(Iterator<ComboGrupoDetallePrecioProducto> it = gp.getDetallePreciosProductos().iterator();it.hasNext();){
            ComboGrupoDetallePrecioProducto comboGrupoDetPP = it.next();
            if(comboGrupoDetPP.getPaf().getPrecioUnitario().compareTo(paf.getPrecioUnitario())<0){
               paf = comboGrupoDetPP.getPaf();
            }
        }
        if(pafParam.getProducto().equals(paf.getProducto()))    
            return true;
        return false;
    }
    
    @Transient
    public BigDecimal getBonificacion(){
        BigDecimal bonificacion= BigDecimal.ZERO;
        int cantCombosArmados = this.getCantidadCombosArmados();
        int cantReferenciaGrupo = 0;
        int acumulador = 0;
        
        BigDecimal menorPrecio = BigDecimal.ZERO;
        int cantidadMenor = 0;
        
        for(Iterator<ComboGrupo> iterator = getCombosGrupo().iterator();iterator.hasNext();){
            ComboGrupo gp = iterator.next();
            cantReferenciaGrupo = cantCombosArmados * gp.getCantidad();
            for(Iterator<ComboGrupoDetallePrecioProducto> comboGrupoDetPPIterator = gp.getDetallePreciosProductos().iterator()
                    ;comboGrupoDetPPIterator.hasNext();){
                ComboGrupoDetallePrecioProducto comboGrupoDetPP = comboGrupoDetPPIterator.next();
//                if(comboGrupoDetPP.getPaf().getPrecioUnitario().compareTo(menorPrecio)<0){
//                    menorPrecio = comboGrupoDetPP.getPaf().getPrecioUnitario();
//                }
                
                if(isCombinarProductos()){
                    if(acumulador == cantReferenciaGrupo){
                        //comboGrupoDetPP.getFactDetalle().incrementarCantidadAuxCombo(comboGrupoDetPP.getCantidad());
                        continue;
                    }

                    if(acumulador + comboGrupoDetPP.getCantidad()<=cantReferenciaGrupo){
                        if(gp.isTomarMenorPrecio()){
                            if(isMenorPrecioEnGrupo(gp, comboGrupoDetPP.getPaf())){
                                bonificacion = bonificacion.add(comboGrupoDetPP.getPaf().getPrecioUnitario()
                                        .multiply(BigDecimal.valueOf(comboGrupoDetPP.getCantidad()))
                                        .multiply(gp.getPorcentaje()).divide(new BigDecimal(100)));    
                                
                            }
                        }else{
                                bonificacion = bonificacion.add(comboGrupoDetPP.getPaf().getPrecioUnitario()
                                        .multiply(BigDecimal.valueOf(comboGrupoDetPP.getCantidad()))
                                        .multiply(gp.getPorcentaje()).divide(new BigDecimal(100)));    
                        }
                        acumulador+=comboGrupoDetPP.getCantidad();
                        comboGrupoDetPP.getPaf().decCantidad(comboGrupoDetPP.getCantidad());
                    }else{
                        int diferencia = acumulador + comboGrupoDetPP.getCantidad() - cantReferenciaGrupo;
                        if(gp.isTomarMenorPrecio()){
                            if(isMenorPrecioEnGrupo(gp, comboGrupoDetPP.getPaf())){
                                bonificacion = bonificacion.add(comboGrupoDetPP.getPaf().getPrecioUnitario()
                                        .multiply(BigDecimal.valueOf(comboGrupoDetPP.getCantidad()-diferencia))
                                        .multiply(gp.getPorcentaje()).divide(new BigDecimal(100)));    
                            }
                        }else{
                                bonificacion = bonificacion.add(comboGrupoDetPP.getPaf().getPrecioUnitario()
                                        .multiply(BigDecimal.valueOf(comboGrupoDetPP.getCantidad()-diferencia))
                                        .multiply(gp.getPorcentaje()).divide(new BigDecimal(100)));    
                        }
                        acumulador = cantReferenciaGrupo;
                        comboGrupoDetPP.getPaf().setCantidad(diferencia);
                    }
                }else{
                   // bonificacion = bonificacion.add(comboGrupoDetPP.getBonificacion());
                }    
                
            }
            if(gp.getMonto().compareTo(BigDecimal.ZERO)>0)
                bonificacion = bonificacion.add(gp.getMonto().multiply(BigDecimal.valueOf(cantCombosArmados)));

        }
        return bonificacion;        
    }
    
    @Transient
    private int getCantidadMinimaPorGrupo(){
        int cantidadMinima = 0;
        if(getCombosGrupo().size()==0)
            return cantidadMinima;
        cantidadMinima = getCombosGrupo().get(0).getCantidadGrupos();
        for(Iterator<ComboGrupo> it = getCombosGrupo().iterator();it.hasNext();){
            ComboGrupo cg = it.next();
            if(cantidadMinima>cg.getCantidadGrupos())
                cantidadMinima=cg.getCantidadGrupos();
        }
        return cantidadMinima;
    }
    

    /**
     * @return the combinarProductos
     */
    public boolean isCombinarProductos() {
        return combinarProductos;
    }

    /**
     * @param combinarProductos the combinarProductos to set
     */
    public void setCombinarProductos(boolean combinarProductos) {
        this.combinarProductos = combinarProductos;
    }
    
    public void calcularCombo(){
        for(Iterator<ComboGrupo> itG = getCombosGrupo().iterator();itG.hasNext();){
            ComboGrupo cg = itG.next();
            
        }
    }

    /**
     * @return the prioridad
     */
    public ComboPrioridadEnum getPrioridad() {
        return prioridad;
    }

    /**
     * @param prioridad the prioridad to set
     */
    public void setPrioridad(ComboPrioridadEnum prioridad) {
        this.prioridad = prioridad;
    }

    /**
     * @return the codigoCombo
     */
    public int getCodigoCombo() {
        return codigoCombo;
    }

    /**
     * @param codigoCombo the codigoCombo to set
     */
    public void setCodigoCombo(int codigoCombo) {
        this.codigoCombo = codigoCombo;
    }

    /**
     * @return the cantidadMaxima
     */
    public int getCantidadMaxima() {
        return cantidadMaxima;
    }

    /**
     * @param cantidadMaxima the cantidadMaxima to set
     */
    public void setCantidadMaxima(int cantidadMaxima) {
        this.cantidadMaxima = cantidadMaxima;
    }

  

    
    
}
