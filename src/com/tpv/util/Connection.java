/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.util;

import com.tpv.exceptions.TpvException;
import com.tpv.service.UsuarioService;
import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
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
    private static SpoolerTCPComm stcpStatus;
    private static EntityManager em;
    private static Logger log = Logger.getLogger(Connection.class);
    private static Button buttonFlowFire=null;
    private static boolean buttonEventFired = false;
    private static String css = "";
    
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
    public static void initFiscalPrinter() throws TpvException{
        if(stcp==null)
            stcp = new SpoolerTCPComm("134.14.13.13",1600);//stcp = new SpoolerTCPComm("134.14.13.13",1600);
        if(stcp.isConnected())
            return;
        try{
            stcp.connect(100);
        }catch(IOException e){
            throw new TpvException("Error de conexión con la impresora: "
                    +e.getMessage(),e);
        }
//        stcpStatus = new SpoolerTCPComm("127.0.0.1",1600);
//        try{
//            stcpStatus.connect(100);
//        }catch(IOException e){
//            log.info("Error de conexión con impresora fiscal, mensaje: "+e.getMessage());
//        }
        
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
        //EntityTransaction tx = em.getTransaction();
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
    
    /**
     * Método que permite obtener una instancia de SpoolerTCPComm
     */
    public static SpoolerTCPComm getStcp(){
        return stcp;
    }
    
    
    public static SpoolerTCPComm getStcpStatus(){
        return stcpStatus;
    }
    
    
    public static String getMACAddress() throws SocketException {
        String firstInterface = null;        
        Map<String, String> addressByNetwork = new HashMap<>();
        Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();

        while(networkInterfaces.hasMoreElements()){
            NetworkInterface network = networkInterfaces.nextElement();

            byte[] bmac = network.getHardwareAddress();
            if(bmac != null){
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < bmac.length; i++){
                    sb.append(String.format("%02X%s", bmac[i], (i < bmac.length - 1) ? "-" : ""));        
                }

                if(sb.toString().isEmpty()==false){
                    addressByNetwork.put(network.getName(), sb.toString());
                    System.out.println("Address = "+sb.toString()+" @ ["+network.getName()+"] "+network.getDisplayName());
                }

                if(sb.toString().isEmpty()==false && firstInterface == null){
                    firstInterface = network.getName();
                }
            }
        }

        if(firstInterface != null){
            return (addressByNetwork.get(firstInterface)).replace("-", "");
        }

        return null;
    }
    
    public static String getCss(){
        return css;
    }
    
    public static void setCss(String cssParam){
        css = cssParam;
    }
    
    /*
    public static String getMACAddress(){
        InetAddress ip;
        try{
            ip = InetAddress.getLocalHost();
            
            NetworkInterface network = NetworkInterface.getByInetAddress(ip);

            byte[] mac = network.getHardwareAddress();

            System.out.print("Current MAC address : ");

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < mac.length; i++) {
                    sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));		
            }
            //System.out.println(sb.toString());            
            return sb.toString().replace("-", "");
	} catch (UnknownHostException e) {
		
		log.error("Error desconocido al intentar obtener la MAC ",e);
                log.error(e.getMessage());
		
	} catch (SocketException e){
			
		log.error("Error de Sockeet al intentar obtener la MAC ",e);
                log.error(e.getMessage());

			
	}
        return null;

    }
    */
    
}
