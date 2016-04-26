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
import com.tpv.modelo.enums.FacturaEstadoEnum;
import com.tpv.principal.LineaTicketData;
import com.tpv.util.Connection;
import java.math.BigDecimal;
import java.util.List;
import javafx.beans.property.ListProperty;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
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
            }
            em.persist(factura);
            tx.commit();
        }catch(RuntimeException e){
            String fullTraceStr=e.getMessage()+"\n";
            for(int i=0;i<=e.getStackTrace().length-1;i++){
                fullTraceStr+="Clase: "+e.getStackTrace()[i].getClassName()+"; "
                        +"Archivo: "+e.getStackTrace()[i].getFileName()+"; "
                        +"Mètodo: "+e.getStackTrace()[i].getMethodName()+"; "
                        +"Nro. Línea: "+e.getStackTrace()[i].getLineNumber()+"; "
                        +"\n";
            }
            log.error(fullTraceStr);
            throw new TpvException("Error en la capa de servicios al registrar la factura por primera vez.");
        }finally{
            em.close();
        }
        return factura;
    }
    
    /*Metodo que deberia ser eliminado*/
    public Factura devolverFactura(Long id) throws TpvException{
        Factura factura = null;
        EntityManager em = Connection.getEm();
        EntityTransaction tx = null;
        try{
            tx = em.getTransaction();
            tx.begin();
            factura = em.find(Factura.class, id);
            tx.commit();
        }catch(RuntimeException e){
            String fullTraceStr=e.getMessage()+"\n";
            for(int i=0;i<=e.getStackTrace().length-1;i++){
                fullTraceStr+="Clase: "+e.getStackTrace()[i].getClassName()+"; "
                        +"Archivo: "+e.getStackTrace()[i].getFileName()+"; "
                        +"Mètodo: "+e.getStackTrace()[i].getMethodName()+"; "
                        +"Nro. Línea: "+e.getStackTrace()[i].getLineNumber()+"; "
                        +"\n";
            }
            log.error(fullTraceStr);
            throw new TpvException("Error en la capa de servicios al devolver la factura.");
        }finally{
            em.close();
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
            String fullTraceStr=e.getMessage()+"\n";
            for(int i=0;i<=e.getStackTrace().length-1;i++){
                fullTraceStr+="Clase: "+e.getStackTrace()[i].getClassName()+"; "
                        +"Archivo: "+e.getStackTrace()[i].getFileName()+"; "
                        +"Mètodo: "+e.getStackTrace()[i].getMethodName()+"; "
                        +"Nro. Línea: "+e.getStackTrace()[i].getLineNumber()+"; "
                        +"\n";
            }
            log.error(fullTraceStr);
            throw new TpvException("Error en la capa de servicios al agregar detalle de la factura.");
        }finally{
            em.close();
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
            String fullTraceStr=e.getMessage()+"\n";
            for(int i=0;i<=e.getStackTrace().length-1;i++){
                fullTraceStr+="Clase: "+e.getStackTrace()[i].getClassName()+"; "
                        +"Archivo: "+e.getStackTrace()[i].getFileName()+"; "
                        +"Mètodo: "+e.getStackTrace()[i].getMethodName()+"; "
                        +"Nro. Línea: "+e.getStackTrace()[i].getLineNumber()+"; "
                        +"\n";
            }
            log.error(fullTraceStr);
            throw new TpvException("Error en la capa de servicios al confirmar la factura.");
        }finally{
            em.close();
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
            String fullTraceStr=e.getMessage()+"\n";
            for(int i=0;i<=e.getStackTrace().length-1;i++){
                fullTraceStr+="Clase: "+e.getStackTrace()[i].getClassName()+"; "
                        +"Archivo: "+e.getStackTrace()[i].getFileName()+"; "
                        +"Mètodo: "+e.getStackTrace()[i].getMethodName()+"; "
                        +"Nro. Línea: "+e.getStackTrace()[i].getLineNumber()+"; "
                        +"\n";
            }
            log.error(fullTraceStr);
            throw new TpvException("Error en la capa de servicios al cancelar la factura.");
        }finally{
            em.close();
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
        Factura factura;
        EntityManager em = Connection.getEm();
        EntityTransaction tx = null;
        Query q = em.createQuery("FROM Combo c WHERE c.fechaHoy BETWEEN c.fechaDesde and c.fechaHasta");
        try{
            tx = em.getTransaction();
            tx.begin();
            
            tx.commit();
        }catch(RuntimeException e){    
            log.error("Error al calcular el combo para id factura: "+id);
            throw new TpvException("Error al calcular combo para id factura: "
                    +id+". "+e.getMessage());
        }finally{
            em.close();
        }
        
        
        
        
    }
    
    
}
