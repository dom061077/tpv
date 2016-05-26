/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.service;

import com.tpv.exceptions.TpvException;
import com.tpv.modelo.Combo;
import com.tpv.modelo.ComboGrupo;
import com.tpv.modelo.ComboGrupoDetalle;
import com.tpv.modelo.CondicionIva;
import com.tpv.modelo.Factura;
import com.tpv.modelo.FacturaDetalle;
import com.tpv.modelo.FacturaDetalleCombo;
import com.tpv.modelo.Producto;
import com.tpv.modelo.ProductoAgrupadoEnFactura;
import com.tpv.modelo.Proveedor;
import com.tpv.modelo.ProveedorProducto;
import com.tpv.modelo.enums.FacturaEstadoEnum;
import com.tpv.principal.LineaTicketData;
import com.tpv.util.Connection;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import javafx.beans.property.ListProperty;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;
import org.apache.log4j.Logger;

/**
 *
 * @author daniel
 */
public class FacturacionService  {
    Logger log = Logger.getLogger(FacturacionService.class);
    
    
    public Factura registrarFactura(Factura factura)throws TpvException{
        log.info("Capa de servicios FacturacionService, registrando por primera vez factura");
        EntityManager em = Connection.getEm();
        EntityTransaction tx = null;
        try{
            tx = em.getTransaction();
            tx.begin(); 
            if(factura.getCliente()==null){
                factura.setCondicionIva(em.find(CondicionIva.class, 1));
                factura.setTotal(BigDecimal.ZERO);
                factura.setBonificaTarjeta(BigDecimal.ZERO);
                factura.setIvaBonificaTarjeta(BigDecimal.ZERO);
                factura.setImpuestoInterno(BigDecimal.ZERO);
                
                
            }
            em.persist(factura);
            tx.commit();
        }catch(RuntimeException e){
            tx.rollback();
            log.error("Error en la capa de servicios al registrar la factura por primera vez.",e);
            throw new TpvException("Error en la capa de servicios al registrar la factura por primera vez.");
        }finally{
            em.clear();
        }
        return factura;
    }
    
    public Factura getFactura(Long id) throws TpvException{
        Factura factura = null;
        EntityManager em = Connection.getEm();
        try{
            factura = em.find(Factura.class, id);
        }catch(RuntimeException e){
            log.error("Error en la capa de servicios al devolver la factura.",e);
            throw new TpvException("Error en la capa de servicios al devolver la factura.");
        }finally{
            em.clear();
        }
        return factura;
    }

    public Factura getFacturaAbiertaPorCheckout(int idCheckout) throws TpvException{
        Factura factura = null;
        EntityManager em = Connection.getEm();
        EntityTransaction tx = null;
        try{
            Query q = em.createQuery("FROM Factura f WHERE f.estado = :estado and f.checkout.id = :idCheckout")
                        .setParameter("estado", FacturaEstadoEnum.ABIERTA)
                        .setParameter("idCheckout", idCheckout);
            factura = (Factura)q.getSingleResult();
        }catch(NonUniqueResultException e){    
            log.error("Hay más de una factura abierta",e);
            
        }catch(NoResultException e){
            log.info("no se encontró factura abierta, no se toma como error NoResultException");
        }catch(RuntimeException e){
            log.error("Error en la capa de servicios al devolver la factura.",e);
            throw new TpvException("Error en la capa de servicios al devolver la factura.");
        }finally{
            em.clear();
        }
        
        return factura;
    }
    
    public void agregarDetalleFactura(Long id,FacturaDetalle facturaDetalle) throws TpvException{
        log.info("Capa de servicios FacturacionService, agregando detalle de factura");
        Factura factura;
        EntityManager em = Connection.getEm();
        EntityTransaction tx = null;
        try{
            tx = em.getTransaction();
            tx.begin();
            factura = em.find(Factura.class,id);
            facturaDetalle.setFactura(factura);
            factura.getDetalle().add(facturaDetalle);
            tx.commit();
        }catch(RuntimeException e){
            if(tx.isActive())
                tx.rollback();
            log.error("Error en la capa de servicios al agregar detalle de la factura.",e);
            throw new TpvException("Error en la capa de servicios al agregar detalle de la factura.");
        }finally{
            em.clear();
        }
    }
    
    public void confirmarFactura(Factura factura)throws TpvException{
        //Factura factura;
        log.info("Capa de servicios, Confirmar Factura con id: "+factura.getId());
        EntityManager em = Connection.getEm();
        EntityTransaction tx = null;
        try{
            tx = em.getTransaction();
            tx.begin();
            //factura = em.find(Factura.class, id);
            factura.setEstado(FacturaEstadoEnum.CERRADA);
            factura=em.merge(factura);
            tx.commit();
            log.info("Factura guardada, id: "+factura.getId());
            log.info("                      Nro. factura: "+factura.getNumeroComprobante());
        }catch(RuntimeException e){
            tx.rollback();
            log.error("Error en la capa de servicios al confirmar la factura.",e);
            throw new TpvException("Error en la capa de servicios al confirmar la factura.");
        }finally{
            em.clear();
        }
    }
    
    public void cancelarFactura(Long id) throws TpvException{
        log.info("Capa de servicios, cancelar factura");
        Factura factura;
        EntityManager em = Connection.getEm();
        EntityTransaction tx = null;
        try{
            tx = em.getTransaction();
            tx.begin();
            factura = em.find(Factura.class, id);
            factura.setEstado(FacturaEstadoEnum.ANULADA);
            tx.commit();
            log.info("Factura con id: "+factura.getId()+", Nro. factura: "
                      +factura.getNumeroComprobante());   
        }catch(RuntimeException e){
            tx.rollback();
            log.error("Error en la capa de servicios al cancelar la factura",e);
            throw new TpvException("Error en la capa de servicios al cancelar la factura.");
        }finally{
            em.clear();
        }
    }
            
//    public void registrarCombos(Long id){
//        Factura factura;
//        EntityManager em = Connection.getEm();
//        EntityTransaction tx = em.getTransaction();
//        if(!tx.isActive())
//            tx.begin();
//        factura = em.find(Factura.class,id );
//        factura.setEstado(FacturaEstadoEnum.CERRADA);
//        tx.commit();
//        em.clear();
//    }
    

    
    public Factura calcularCombos(Long id) throws TpvException{
        log.info("Calculando combos para id factura: "+id);
        List<Combo> listadoCombos = null;
        Factura factura = null;
        EntityManager em = Connection.getEm();
        factura = em.find(Factura.class, id);
        factura.agruparProductosEnFactura();
//        for(Iterator<ProductoAgrupadoEnFactura> it = factura.getProductosAgrupados().iterator();it.hasNext();){
//            ProductoAgrupadoEnFactura paf = it.next();
//            System.out.println("Producto: "+paf.getProducto().getDescripcion());
//            System.out.println("Cantidad Acumulada; "+paf.getCantidad());
//            System.out.println("Precio del producto: "+paf.getPrecioUnitario());
//        }
//        if(factura!=null)
//            return;
        
        factura.getDetalleCombosAux().clear();
        Query q = em.createNativeQuery(
                "SELECT DISTINCT c.* FROM facturasdetalle fd"
                +" INNER JOIN productos p ON fd.idPRODUCTOS=p.idPRODUCTOS AND p.DISCONTINUADO = 0"
                +" LEFT JOIN ("
                +"       SELECT gp.idGRUPOPRODUCTOS AS grupohijo"
                +"                ,glevel1.idGRUPOPRODUCTOS AS grupopadre FROM grupoproductos gp"
                +"        INNER JOIN grupoproductos glevel1 ON glevel1.idgrupoproductos = gp.padreid"
                +" ) grupoprod ON (p.idgrupoproductos = grupoprod.grupohijo OR p.idgrupoproductos = grupoprod.grupopadre)"
                +" LEFT JOIN combosgrupodetalle cgd ON fd.idPRODUCTOS = cgd.idproductos OR grupoprod.grupohijo = cgd.idGRUPOPRODUCTOS"
		+"		OR grupoprod.grupopadre = cgd.idGRUPOPRODUCTOS"
                +" LEFT JOIN combosgrupo cg ON cgd.idCOMBOSGRUPO = cg.idCOMBOSGRUPO"
                +" LEFT JOIN combos c ON cg.idCOMBOS = c.idCOMBOS"
                +" LEFT JOIN proveedores_productos pp ON fd.idPRODUCTOS = pp.idPRODUCTOS AND pp.idProveedor=cgd.idProveedor"
                +" WHERE c.idcombos IS NOT NULL AND fd.idFACTURAS = ?1 AND CONVERT(NOW(),DATE) BETWEEN c.FECHADESDE AND c.FECHAHASTA"
                +" ORDER BY c.PRIORIDAD"
                , Combo.class).setParameter(1, id);
        try{
            listadoCombos = q.getResultList();
//            if(listadoCombos.size()>0){
//                for(Iterator<Combo> it = listadoCombos.iterator();it.hasNext(); ){
//                    Combo c = it.next();
//                    System.out.println("Combo: "+c.getDescripcion()+" Prioridad: "
//                            +c.getPrioridad());
//                }
//                return;
//            }
            boolean hayDetalleGrupo;
            for(Iterator itCombo = listadoCombos.iterator();itCombo.hasNext();){
                Combo combo = (Combo)itCombo.next();
                for(Iterator itGrupo = combo.getCombosGrupo().iterator();itGrupo.hasNext();){
                    ComboGrupo grupo = (ComboGrupo)itGrupo.next();
                    for(Iterator<ProductoAgrupadoEnFactura> itAg = factura.getProductosAgrupados().iterator();itAg.hasNext();){
                        ProductoAgrupadoEnFactura paf = itAg.next();
                        hayDetalleGrupo=false;
                        for(Iterator itDetalle = grupo.getGruposDetalle().iterator();itDetalle.hasNext();){
                            ComboGrupoDetalle gDetalle = (ComboGrupoDetalle)itDetalle.next();
                            if(paf.getCantidad()<=0)
                                continue;
                            if(gDetalle.getProducto()!=null){
                                if(gDetalle.getProducto().equals(paf.getProducto())){
                                    if(gDetalle.getProveedor()!=null){
                                        if(paf.getProducto().tieneEsteProveedor(gDetalle.getProveedor())){
                                            hayDetalleGrupo = true;
                                        }
                                    }else{
                                            hayDetalleGrupo= true;
                                    }
                                }
                            }else{
                                if(gDetalle.getGrupoProducto()!=null){
                                    if(paf.getProducto().tieneEsteGrupo(gDetalle.getGrupoProducto())){
                                        if(gDetalle.getProveedor()!=null){
                                            if(paf.getProducto().tieneEsteProveedor(gDetalle.getProveedor())){
                                                hayDetalleGrupo = true;
                                            }
                                        }else{
                                            hayDetalleGrupo = true;
                                        }
                                    }
                                }else{
                                    if(gDetalle.getProveedor()!=null){
                                        if(paf.getProducto().tieneEsteProveedor(gDetalle.getProveedor())){
                                            hayDetalleGrupo = true;
                                        }
                                    }
                                }
                            }
                            if(hayDetalleGrupo){
                                if(combo.isCombinarProductos()){
                                    grupo.addDetallePrecioProducto(
                                                paf.getPrecioUnitario(), paf
                                                );
                                }else{
                                    grupo.addDetallePrecioProducto(
                                             paf.getPrecioUnitario(),paf);
                                }
                                    
                                
                            }
                        }
                    }
                }
                if(combo.cumpleCondicion()){
                    FacturaDetalleCombo fd = new FacturaDetalleCombo();
                    fd.setCombo(combo);
                    fd.setCantidad(combo.getCantidadCombosArmados());
                    if(combo.isCombinarProductos())
                        fd.setBonificacion(combo.getBonificacion());
                    else    
                        fd.setBonificacion(combo.getBonificacionSinCombinacion());
                    factura.getDetalleCombosAux().add(fd);
                }
                
            }
            
//            factura.getDetalleCombosAux().forEach(item ->{
//                System.out.println("Combo armado: "+item.getCombo().getDescripcion());
//                System.out.println("Bonificación: "+item.getBonificacion());
//                System.out.println("Cantidad Combos: "+item.getCantidad());
//                System.out.println("");
//                item.getCombo().getCombosGrupo().forEach(itemg->{
//                    System.out.println("            Grupo condicion de cantidad: "+itemg.getCantidad());
//                });
//            });
//            System.out.println("---------------------------------------------------------");
//            for(Iterator<ProductoAgrupadoEnFactura> it = factura.getProductosAgrupados().iterator();it.hasNext();){
//                ProductoAgrupadoEnFactura paf = it.next();
//                System.out.println("Producto: "+paf.getProducto().getIdProducto()+" "+paf.getProducto().getCodigoProducto()+"-"+paf.getProducto().getDescripcion());
//                System.out.println("Cantidad Sobrante: "+paf.getCantidad());
//            }
            
            
        }catch(RuntimeException e){    
            e.printStackTrace();
            //log.error("Error al calcular el combo sin combinaciones para id factura: ",e);
            throw new TpvException("Error al calcular combo para id factura: "
                    +". "+e.getMessage());
        }finally{
            em.clear();
        }
        return factura;
    }
    
}
