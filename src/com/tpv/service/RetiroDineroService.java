/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.service;

import com.tpv.exceptions.TpvException;
import com.tpv.modelo.Billete;
import com.tpv.modelo.RetiroDinero;
import com.tpv.util.Connection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import org.apache.log4j.Logger;

/**
 *
 * @author COMPUTOS
 */
public class RetiroDineroService {
    Logger log = Logger.getLogger(RetiroDineroService.class);
    
    public List<Billete> getBilletes()throws TpvException{
        List<Billete> list=null;
        EntityManager em = Connection.getEm();
        try{
            Query q = em.createQuery("FROM Billete WHERE retiroHabilitado = true");
            list = q.getResultList();
        }catch(RuntimeException e){
            log.error("Error en la capa de servicios al recuperar billetes",e);
            throw new TpvException("Error en la capa de servicios al recuperar billetes",e);
            
        }
        return list;
    }
    
    public Billete getBillete(int idBillete)throws TpvException{
        log.info("Capa de servicios búsqueda de billete, id: "+idBillete);
        EntityManager em = Connection.getEm();
        Billete billete = null;
        try{
            Query q = em.createQuery("FROM Billete b WHERE b.estado = true AND b.id = :idBillete")
                    .setParameter("idBillete", idBillete);
            
        }catch(NoResultException e){
            log.error("No se puedo econtrar el billete con id: "+idBillete);
            throw new TpvException("Error en la capa de servicios al recuperar un billete con código: "+idBillete);
        }finally{
            em.clear();
        }
        return billete;
    }
    
    public void registrarRetiro(RetiroDinero retiroDinero)throws TpvException{
        log.info("Capa de servicios RetiroDineroService, registro de retiro");
        EntityManager em = Connection.getEm();
        EntityTransaction tx = null;
        try{
            tx = em.getTransaction();
            tx.begin();
            em.persist(retiroDinero);
            tx.commit();
            log.info("RetiroDinero guardado, id: "+retiroDinero.getId());
        }catch(RuntimeException e){
            tx.rollback();
            log.error("Error en la capa de servicios al registrar el retiro de dinero.",e);
            throw new TpvException("Error en la capa de servicios al registrar el retiro de dinero. "+e.getMessage());
        }finally{
            em.clear();
        }
    }
    
    
}
