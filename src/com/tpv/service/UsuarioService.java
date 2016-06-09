/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.service;

import com.tpv.exceptions.TpvException;
import com.tpv.modelo.Checkout;
import com.tpv.modelo.Usuario;
import com.tpv.util.Connection;
import java.net.SocketException;
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
    public Usuario authenticar(String nombre,String password) throws TpvException{
        log.info("Autenticando usuario: "+nombre);
        boolean flagReturn=false;
        Usuario usuario = null;
        EntityManager em = Connection.getEm();
        EntityTransaction tx = null;
        try{
//            tx = em.getTransaction();
//            tx.begin();
            usuario = (Usuario)em.createQuery("FROM Usuario u WHERE u.nombre = :nombre").setParameter("nombre", nombre).getSingleResult();
            if(usuario.getPassword().compareTo(password)==0)
                    flagReturn=true;
            else
                flagReturn=false;
//            tx.commit();
        }catch(NoResultException e){
            log.info("No se encontró ningún usuario con nombre: "+nombre+" password: "+password);
        }catch(RuntimeException e){
            log.error("Error en la capa de servicios al autenticar usuario.",e);
            throw new TpvException("Error en la capa de servicios al autenticar usuario.");
        }finally{
            em.clear();
        }
        
        //emf.close();
        if(flagReturn)
            return usuario;
        else
            return null;
    }
    
    public Checkout checkMac() throws TpvException{
        log.info("Capa de servicios, recuperando MAC de la PC");
        String mac = "";
        try{
                mac=Connection.getMACAddress();
        }catch(SocketException e){
            log.error("Error en la capa de servicios al recuperar la MAC",e);
            throw new TpvException("Error en la capa de servicios al recuperar la MAC");
        }
        log.info("MAC recuperada: "+mac);
        if(mac!=null){
            EntityManager em = Connection.getEm();
            EntityTransaction tx = null;
            try{
                tx = em.getTransaction();
                tx.begin();
                Query q = em.createQuery("FROM Checkout c WHERE c.placa = :placa").setParameter("placa",mac);    
                Checkout checkout = null;
                checkout = (Checkout)q.getSingleResult();
                tx.commit();
                log.info("Checkout recuperado: "+checkout.getId());
                if(checkout != null)
                    return checkout;
            }catch(RuntimeException e){
                log.error("Error recuperando MAC: "+mac,e);
                throw new TpvException("Error en la capa de servicios al recuperar checkout a través de la MAC.");
            }finally{
                em.clear();
            }
        }
        return null;
    }
}
