/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.service;


import com.tpv.modelo.Cliente;
import com.tpv.util.Connection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import org.apache.log4j.Logger;

/**
 *
 * @author daniel
 */
public class ClienteService {
    Logger log = Logger.getLogger(ClienteService.class);
    public Cliente getClientePorCodYDni(int filtroCodigo){
        Cliente cliente = null;
        EntityManager em = Connection.getEm();
        EntityTransaction tx = em.getTransaction();
        if(!tx.isActive())
            tx.begin();
        Query q = em.createQuery("FROM Cliente c WHERE c.id = :id or c.dni = :dni").setParameter("id"
                ,filtroCodigo).setParameter("dni", filtroCodigo);
        
        
        try{
            cliente = (Cliente)q.getSingleResult();
        }catch(Exception e){
            log.error("Error en consulta de cliente, filtroCodigo: "+filtroCodigo);
            log.error(e.getMessage());
        }
        tx.commit();
        em.clear();
       return cliente;         
    }
    
    public List getClientes(String filtro){
        log.info("Filtro búsqueda: "+filtro);
        List<Cliente> clientes;
        EntityManager em = Connection.getEm();
        EntityTransaction tx = em.getTransaction();
        if(!tx.isActive())
           tx.begin();
        int codigoProducto=0;

        Query q = em.createQuery("FROM Cliente c WHERE c.razonSocial like  :razonSocial");
        q.setParameter("razonSocial", "%"+filtro+"%");
        q.setFirstResult(0);
        q.setMaxResults(100);
        clientes = q.getResultList();
        
        
        tx.commit();
        em.clear();
        
        
        return clientes;
    }
}
