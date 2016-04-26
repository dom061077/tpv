/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.service;

import com.tpv.exceptions.TpvException;
import com.tpv.modelo.FormaPago;
import com.tpv.modelo.Producto;
import com.tpv.util.Connection;
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
public class PagoService {
    Logger log = Logger.getLogger(PagoService.class);
    
    public FormaPago getFormaPago(int codigoPago) throws TpvException{
        log.info("Capa de servicios, parametro de busqueda: "+codigoPago);
        EntityManager em = Connection.getEm();
        EntityTransaction tx = null;
        FormaPago formaPago = null;        
        tx = em.getTransaction();
        try{
            tx.begin();
            Query q = em.createQuery("FROM FormaPago fp WHERE fp.id = :id").setParameter("id", codigoPago);
            formaPago = (FormaPago)q.getSingleResult();
            log.info("Forma de pago recuperada: "+formaPago.toString());
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
            throw new TpvException("Error en la capa de servicios al autenticar usuario.");
        }finally{
            em.close();
        }
        return formaPago;
    }
    
}
