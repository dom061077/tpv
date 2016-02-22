/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.service;

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
    
    public FormaPago getFormaPago(int codigoPago){
        EntityManager em = Connection.getEm();
        EntityTransaction tx = em.getTransaction();
        if(!tx.isActive())
            tx.begin();
        Query q = em.createQuery("FROM FormaPago fp WHERE fp.id = :id").setParameter("id", codigoPago);
        
        FormaPago formaPago = null;
        try{
            formaPago = (FormaPago)q.getSingleResult();
        }catch(NoResultException e){
            
        }catch(NonUniqueResultException e){
            
        }catch(Exception e){
            
        }finally{
            
        }
        tx.commit();
        em.clear();
        //em.close();
        return formaPago;
    }
    
}
