/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.modelo;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 *
 * @author daniel
 */

@Entity
@Table(name="combosgrupo")
public class ComboGrupo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "idCOMBOSGRUPO")
    private Long id;
    
    @Column(name = "CANTIDADPRODUCTO")
    private BigDecimal cantidad;
            
    @Column(name = "PORCENTAJE")
    private BigDecimal porcentaje;

    
    @Column(name = "BONIFICACION_EN_MENOR_PRECIO", columnDefinition = "TINYINT(1)")
    private boolean tomarMenorPrecio;
    

    @ManyToOne
    @JoinColumn(name = "idCOMBOS", referencedColumnName = "idCOMBOS", nullable=true)
    private Combo combo;  
    
    @OneToMany(cascade = CascadeType.ALL,mappedBy="comboGrupo")
    private List<ComboGrupoDetalle> gruposDetalle = new ArrayList<ComboGrupoDetalle>();
    
    @Transient
    private List<ComboGrupoDetallePrecioProducto> detallePreciosProductos = new ArrayList<ComboGrupoDetallePrecioProducto>();

    @Transient
    private int cantidadGruposEnCombo;

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
     * @return the cantidad
     */
    public BigDecimal getCantidad() {
        return cantidad;
    }

    /**
     * @param cantidad the cantidad to set
     */
    public void setCantidad(BigDecimal cantidad) {
        this.cantidad = cantidad;
    }

    /**
     * @return the porcentaje
     */
    public BigDecimal getPorcentaje() {
        return porcentaje;
    }

    /**
     * @param porcentaje the porcentaje to set
     */
    public void setPorcentaje(BigDecimal porcentaje) {
        this.porcentaje = porcentaje;
    }


    /**
     * @return the combo
     */
    public Combo getCombo() {
        return combo;
    }

    /**
     * @param combo the combo to set
     */
    public void setCombo(Combo combo) {
        this.combo = combo;
    }

    /**
     * @return the gruposDetalle
     */
    public List<ComboGrupoDetalle> getGruposDetalle() {
        return gruposDetalle;
    }

    /**
     * @param gruposDetalle the gruposDetalle to set
     */
    public void setGruposDetalle(List<ComboGrupoDetalle> gruposDetalle) {
        this.gruposDetalle = gruposDetalle;
    }


    /**
     * @return the detallePreciosProductos
     */
    public List<ComboGrupoDetallePrecioProducto> getDetallePreciosProductos() {
        /*if(getPorcentaje().compareTo(BigDecimal.ZERO)>0 || getMonto().compareTo(BigDecimal.ZERO)>0)
            Collections.sort(detallePreciosProductos,
                    (o1,o2)->o1.getPaf().getPrecioUnitario()
                            .compareTo(o2.getPaf().getPrecioUnitario())*-1
            );
        else
            Collections.sort(detallePreciosProductos,
                    (o1,o2)->o1.getPaf().getPrecioUnitario()
                            .compareTo(o2.getPaf().getPrecioUnitario    ())
            );*/
        
        //if(tomarMenorPrecio)
        //    Collections.sort(detallePreciosProductos,
        //            (o1,o2)->o1.getPaf().getPrecioUnitario()
        //                    .compareTo(o2.getPaf().getPrecioUnitario    ())
        //    );
        //else
            Collections.sort(detallePreciosProductos,
                    (o1,o2)->o1.getPaf().getPrecioUnitario()
                            .compareTo(o2.getPaf().getPrecioUnitario())*-1
            );
            
            
        return detallePreciosProductos;
        
    }
    
    public void addDetallePrecioProducto(BigDecimal precioProducto,ProductoAgrupadoEnFactura paf){
        for(Iterator<ComboGrupoDetallePrecioProducto> it = detallePreciosProductos.iterator();it.hasNext();){
            ComboGrupoDetallePrecioProducto gpp = it.next();
            if(gpp.getPaf().getProducto().equals(paf.getProducto())){
               gpp.setCantidad(paf.getCantidad());
               return;
            }
        }
        ComboGrupoDetallePrecioProducto gdProd = new ComboGrupoDetallePrecioProducto();
        gdProd.setCantidad(paf.getCantidad());
        //gdProd.setPrecioProducto(precioProducto);
        gdProd.setPaf(paf);
        gdProd.setComboGrupo(this);
        detallePreciosProductos.add(gdProd);
        
    }
    
    @Transient
    private BigDecimal getCantidadEnCombinacion(){
        BigDecimal cantAcumulada = BigDecimal.ZERO;
        for(Iterator<ComboGrupoDetallePrecioProducto> iterator = getDetallePreciosProductos().iterator();
                iterator.hasNext();){
            ComboGrupoDetallePrecioProducto cGrupoDet = iterator.next();
            cantAcumulada = cantAcumulada.add(cGrupoDet.getCantidad());
        }
        return cantAcumulada;
    }
    
    @Transient
    public int getCantidadGrupos(){
        int cantGruposArmados = 0;
        if(getCombo().isCombinarProductos()){
            if(getCantidadEnCombinacion().compareTo(cantidad)<0) //if(getCantidadEnCombinacion()<cantidad)
                cantGruposArmados = 0;
            else
                cantGruposArmados = getCantidadEnCombinacion().divide(cantidad).intValue();  //getCantidadEnCombinacion()/cantidad;
        }else{
            for(Iterator<ComboGrupoDetallePrecioProducto> it = getDetallePreciosProductos().iterator();it.hasNext();){
                ComboGrupoDetallePrecioProducto cgdPP = it.next();
                cantGruposArmados = cgdPP.getCantidad().divide(cantidad).intValue();//cantGruposArmados += cgdPP.getCantidad()/cantidad;
            }
        }
        return cantGruposArmados;
    }    
    
    @Transient
    public BigDecimal getBonificacion(){
        BigDecimal bonificacion = new BigDecimal(BigInteger.ZERO);
//        int cantidadCombos = combo.getCantidadCombosArmados();
//        for(Iterator<ComboGrupoDetallePrecioProducto> iterator = getDetallePreciosProductos().iterator()
//                ;iterator.hasNext();){
//            ComboGrupoDetallePrecioProducto cGDetPrecioProducto = iterator.next();
//            //bonificacion = bonificacion.add(cGDetPrecioProducto.getSubTotal().multiply(this.getPorcentaje()).divide(new BigDecimal(100)));
//            bonificacion = cGDetPrecioProducto.getBonificacionPorPrecioProducto();
//            
//        }
        return bonificacion;
    }
    
    public void initCantidadDetPrecioProductoAux(){
        for(Iterator<ComboGrupoDetallePrecioProducto> it= getDetallePreciosProductos().iterator();it.hasNext(); ){
            ComboGrupoDetallePrecioProducto cgDPP = it.next();
            cgDPP.initCantidadAux();
        }
            
    }

    /**
     * @return the tomarMenorPrecio
     */
    public boolean isTomarMenorPrecio() {
        return tomarMenorPrecio;
    }
    
    public void incCantidadGruposEnCombo(){
        this.cantidadGruposEnCombo+=1;
        
    }
    
    
    
    public void resetCantidadGruposEnCombo(){
        this.cantidadGruposEnCombo = 0;
    }

    /**
     * @return the cantidadGruposEnCombo
     */
    public int getCantidadGruposEnCombo() {
        return cantidadGruposEnCombo;
    }

}
