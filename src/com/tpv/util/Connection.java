/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.util;

import com.tpv.service.UsuarioService;
import javafx.stage.Stage;
import javafx8tpv1.JavaFX8TPV1;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;
import org.apache.log4j.Logger;

/**
 * Clase que permite EntityManagerFactory para poder comunicarse con la base de datos.
 *
 * @author daniel
 */
public class Connection {
    /**
     * propiedad emf que será inicializada en el método getEmf
     */
    private static EntityManagerFactory emf;
    private static EntityManager em;
    private static Logger log = Logger.getLogger(Connection.class);
    private static Stage stage=null;
    
    /**
     * Este método cumple la función de obligar a la creación de la instancia
     * del EntityManagerFactory.
     */
    
    private static void initEmf() throws Exception{
        emf = Persistence.createEntityManagerFactory("tpvpersistence");
        
        if(emf==null)
            throw new Exception("No se pudo realizar la conexión con la base de datos");
        em = emf.createEntityManager();
        
    }
    
    /**
     * Este método conecta la impresora fiscal
     */
    private static void initFiscalPrinter(){
        
    }
    
    /**
     * Método principal de inicio de conexiones a la base de datos y
     * la impresora fiscal.
     */
    public static void initConnections()throws Exception{
        initEmf();
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
    
    /**
     * Retorna el estado de la conexión a la base de datos
     */
    public static boolean isDBConnected(){
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        Query q = em.createNativeQuery("SELECT 1");
        try{
            q.getSingleResult();
            return true;
        }catch(Exception e){
            log.error("La conexión a la base de datos no responde");
        }finally{
            tx.commit();
            em.clear();
            return false;
        }
        
        
    }
    
    /**
     * Metodo para recuperar el stage actual mientras el thread de test de conexión
     * 
     */
    public static Stage getStage(){
        return stage;
    }
    
    /**
     * Método para fijar el stage actual mientras el thread de test de conexión
     * 
     */
    public static void setStage(Stage stage){
        stage = stage;
    }
}
