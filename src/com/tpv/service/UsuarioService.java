/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.service;

import com.tpv.modelo.Usuario;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

/**
 *
 * @author daniel
 */
public class UsuarioService {
    
    public boolean authenticar(String nombre,String password){
        boolean flagReturn=false;
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("tpvpersistence");        
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        List usuarios = em.createQuery("FROM Usuario u WHERE u.nombre = :nombre").setParameter("nombre", nombre).getResultList();
        

        if (usuarios.size()>0) {
                    Usuario usuario = (Usuario)usuarios.get(0);
            if(usuario.getPassword().equals(password))
                flagReturn=true;
        }else
            flagReturn=false;
        
        tx.commit();
        em.clear();
        em.close();
        
        emf.close();
        return flagReturn;
    }
}
