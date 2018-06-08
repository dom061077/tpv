/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.service;

import com.tpv.exceptions.TpvException;
import com.tpv.modelo.FormaPago;
import com.tpv.util.Connection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
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
        FormaPago formaPago = null;        
        try{
            Query q = em.createQuery("FROM FormaPago fp WHERE fp.id = :id").setParameter("id", codigoPago);
            formaPago = (FormaPago)q.getSingleResult();
            log.info("Forma de pago recuperada: "+formaPago.toString());
        }catch(NoResultException e){
            log.info("No se encontró el pago con el código: "+codigoPago
                    +" no se toma la excepción como error");
             
        }catch(RuntimeException e){
            log.error("Error en la capa de servicios al recuperar un pago.",e);
            throw new TpvException("Error en la capa de servicios al recuperar un pago.");
        }finally{
            em.clear();

        }
        return formaPago;
    }
    
    public List<FormaPago> getFormasPago()throws TpvException{
        log.info("Capa de servicios listando formas de pago");
        List<FormaPago> formas=null;
        EntityManager em = Connection.getEm();
        try{
            Query q = em.createQuery("FROM FormaPago");
            formas = q.getResultList();
            log.info("cantidad de formas : "+formas.size());
        }catch(RuntimeException e){
            log.error("Error en capa de servicios al recuperar listado de formas de pago",e);
            throw new TpvException("Error en la capa de servicios al recuperar las formas de pago");
        }finally{
            em.clear();
        }
        
        return formas;
    }
    

    
}
