/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.service;

import com.tpv.modelo.Checkout;
import com.tpv.modelo.Usuario;
import com.tpv.util.Connection;
import java.util.List;
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
public class UsuarioService {
    Logger log = Logger.getLogger(UsuarioService.class);
    public Usuario authenticar(String nombre,String password){
        log.info("Autenticando usuario: "+nombre);
        boolean flagReturn=false;
        EntityManager em = Connection.getEm();
        EntityTransaction tx = em.getTransaction();
        if(!tx.isActive())
            tx.begin();
        List usuarios = em.createQuery("FROM Usuario u WHERE u.nombre = :nombre").setParameter("nombre", nombre).getResultList();
        
        Usuario usuario = (Usuario)usuarios.get(0); 
        if (usuarios.size()>0) {
                    usuario = (Usuario)usuarios.get(0);
            if(usuario.getPassword().equals(password))
                flagReturn=true;
        }else
            flagReturn=false;
        
        tx.commit();
        em.clear();
        
        //emf.close();
        return usuario;
    }
    
    public Checkout checkMac(){
        String mac = Connection.getMACAddress();
        if(mac!=null){
            EntityManager em = Connection.getEm();
            EntityTransaction tx = em.getTransaction();
            if(!tx.isActive())
                tx.begin();
            Query q = em.createQuery("FROM Checkout c WHERE c.placa = :placa").setParameter("placa",mac);    
            Checkout checkout = null;
            try{
                checkout = (Checkout)q.getSingleResult();
                if(checkout != null)
                    return checkout;
            }catch(NoResultException e){

            }catch(NonUniqueResultException e){

            }catch(Exception e){

            }finally{
                tx.commit();
                em.clear();
            }
                
            
           
            
        }
        return null;
    }
}
