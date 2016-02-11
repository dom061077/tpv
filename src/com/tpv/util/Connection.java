/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.util;

import com.tpv.service.UsuarioService;
import java.io.IOException;
import java.net.SocketException;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx8tpv1.JavaFX8TPV1;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;
import org.apache.log4j.Logger;
import org.tpv.print.fiscal.comm.SpoolerTCPComm;

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
    private static SpoolerTCPComm stcp;
    private static EntityManager em;
    private static Logger log = Logger.getLogger(Connection.class);
    private static Button buttonFlowFire=null;
    private static boolean buttonEventFired = false;
    
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
    public static void initFiscalPrinter(){
        stcp = new SpoolerTCPComm("127.0.0.1",1600);
        try{
            stcp.connect();
        }catch(IOException e){
            log.info("Error de conexión con impresora fiscal, mensaje: "+e.getMessage());
        }
        
    }
    
    /**
     * Método principal de inicio de conexiones a la base de datos y
     * la impresora fiscal.
     */
    public static void initConnections()throws Exception{
        initEmf();
    }
    
    /**
     * Método que permite obtener una instancia de SpoolerTCPComm
     */
    public static SpoolerTCPComm getStcp(){
        return stcp;
    }
    
    /**
     * Retorna instancia de EntityManager
     */
    
    public static EntityManager getEm(){
        if(emf==null)
            return null;
        if(em == null){
            em = emf.createEntityManager();
        }
        return em;
    }
    
    /**
     * Retorna el estado de la conexión a la base de datos
     */
    public static boolean isDBConnected(){
        if(getEm()==null)
            return false;
        if(!em.isOpen()){
            log.debug("EntityManager está cerrado");
            return false;
        }
        EntityTransaction tx = em.getTransaction();
        //tx.begin();
        Query q = em.createNativeQuery("SELECT 1");
        try{
            q.getSingleResult();
            return true;
        //}catch(Exception e){
        //    log.error("La conexión a la base de datos no responde");
        }catch(Exception e)    {
            log.debug("EXCEPCION ATRAPADA en q.getSingleResult");
        }finally{
            //tx.commit();
            em.clear();
            return false;
        }
        
        
    }
    
}