/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.service;

import com.tpv.modelo.Usuario;
import com.tpv.util.Connection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
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
}
