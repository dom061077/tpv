/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.service;

import com.tpv.exceptions.TpvException;
import com.tpv.modelo.Factura;
import com.tpv.util.Connection;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import org.apache.log4j.Logger;

/**
 *
 * @author daniel
 */
public class FacturacionService  {
    Logger log = Logger.getLogger(FacturacionService.class);
    
    
    public void registrarFactura(Factura factura)throws TpvException{
        EntityManager em = Connection.getEm();
        EntityTransaction tx = em.getTransaction();
        if(!tx.isActive())
           tx.begin();     
        try{
            em.merge(factura);
        }catch(Exception e){
            throw new TpvException(e.getMessage());
        }
        tx.commit();
        em.clear();        
    }
}
