/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Clase que permite EntityManagerFactory para poder comunicarse con la base de datos.
 *
 * @author daniel
 */
public class EmfConnection {
    /**
     * propiedad emf que será inicializada en el método getEmf
     */
    private static EntityManagerFactory emf;
    private static EntityManager em;
    
    /**
     * Este método cumple la función de obligar a la creación de la instancia
     * del EntityManagerFactory.
     */
    
    public static void initEmf() throws Exception{
        emf = Persistence.createEntityManagerFactory("tpvpersistence");
        if(emf==null)
            throw new Exception("No se pudo realizar la conexión con la base de datos");
        em = emf.createEntityManager();
    }
    
    /**
     * Retorna instancia de EntityManager
     */
    
    public static EntityManager getEm(){
        if(em == null){
            em = emf.createEntityManager();
        }
        return em;
    }
    
}
