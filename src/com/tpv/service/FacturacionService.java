/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.service;

import com.tpv.exceptions.TpvException;
import com.tpv.modelo.Combo;
import com.tpv.modelo.CondicionIva;
import com.tpv.modelo.Factura;
import com.tpv.modelo.FacturaDetalle;
import com.tpv.modelo.Producto;
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
        EntityTransaction tx = null;
        try{
            tx = em.getTransaction();
            tx.begin();
            factura = em.find(Factura.class, id);
            tx.commit();
        }catch(RuntimeException e){
            tx.rollback();
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
    
    public void calcularCombos(Long id) throws TpvException{
        log.info("Calculando combos para id factura: "+id);
        List<Combo> listadoCombos = null;
        Factura factura = null;
        EntityManager em = Connection.getEm();
        factura = em.find(Factura.class, id);
        Query q = em.createNativeQuery(
                "SELECT DISTINCT c.* FROM facturasdetalle fd"
                +" INNER JOIN productos p ON fd.idPRODUCTOS=p.idPRODUCTOS AND p.DISCONTINUADO = 0"
                +" LEFT JOIN combosdetalle cd ON fd.idPRODUCTOS = cd.idPRODUCTOS"
                +" LEFT JOIN combos c ON cd.idCOMBOS = c.idCOMBOS"
                +" LEFT JOIN proveedores_productos pp ON fd.idPRODUCTOS = pp.idPRODUCTOS AND pp.idProveedor=cd.idProveedor"
                +" LEFT JOIN ("
                +"        SELECT gp.idGRUPOPRODUCTOS AS grupohijo"
                +"                ,glevel1.idGRUPOPRODUCTOS AS grupopadre FROM grupoproductos gp"
                +"        INNER JOIN grupoproductos glevel1 ON glevel1.idgrupoproductos = gp.padreid"
                +" ) grupoprod ON p.idgrupoproductos = grupoprod.grupohijo AND(cd.idgrupoproductos = grupoprod.grupohijo OR "
                +"                cd.idgrupoproductos = grupoprod.grupopadre)"
                +" WHERE cd.idcombos IS NOT NULL AND fd.idFACTURAS = ?1 AND CONVERT(NOW(),DATE) BETWEEN c.FECHADESDE AND c.FECHAHASTA"
                +" UNION "        
                +"  SELECT DISTINCT c.* FROM facturasdetalle fd"
                +"  INNER JOIN productos p ON fd.idPRODUCTOS=p.idPRODUCTOS AND p.DISCONTINUADO = 0"
                +"  LEFT JOIN combosdetalleliberado cdl ON fd.idPRODUCTOS= cdl.idPRODUCTOS"
                +"  LEFT JOIN combosliberado cl ON cdl.idCOMBOLIBERADO = cl.idCOMBOLIBERADO"
                +"  LEFT JOIN combos c ON cl.idCOMBOS = c.idCOMBOS"
                +"  WHERE c.idcombos IS NOT NULL AND fd.idFACTURAS = ?1 AND CONVERT(NOW(),DATE) BETWEEN c.FECHADESDE AND c.FECHAHASTA"
                , Combo.class).setParameter(1, id);
        try{
            listadoCombos = q.getResultList();
            for(Iterator iterator = listadoCombos.iterator();iterator.hasNext();){
                 
            }
        }catch(RuntimeException e){    
            log.error("Error al calcular el combo sin combinaciones para id factura: "+id,e);
            throw new TpvException("Error al calcular combo para id factura: "
                    +id+". "+e.getMessage());
        }finally{
            em.clear();
        }
    }
    
    //private 
}
