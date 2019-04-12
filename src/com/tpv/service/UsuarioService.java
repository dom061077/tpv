/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.service;

import com.tpv.exceptions.TpvException;
import com.tpv.modelo.AperturaCierreCajeroDetalle;
import com.tpv.modelo.Checkout;
import com.tpv.modelo.Usuario;
import com.tpv.modelo.UsuarioPerfil;
import com.tpv.principal.Context;
import com.tpv.util.Connection;
import java.math.BigInteger;
import java.net.SocketException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
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
        Query query;
        
        //https://stackoverflow.com/questions/27905148/force-hibernate-to-read-database-and-not-return-cached-entity
            /*para probar que el objeto se refresque directo desde la base de datos*/
        
        try{
            query = em.createQuery("FROM Usuario u WHERE u.nombre = :nombre AND u.password = :clave")
                    .setParameter("nombre", nombre).setParameter("clave", getMD5(password));
            //query.setHint("org.hibernate.cacheMode",org.hibernate.CacheMode.IGNORE);
            usuario = (Usuario) query.getSingleResult();
            /*UsuarioPerfil usuPerfil=null;
            if(usuario!=null){
                usuPerfil = (UsuarioPerfil)em.createQuery("FROM UsuarioPerfil up WHERE up.id.usuarioId = :usuarioId AND up.id.perfilId = :perfilId")
                        .setParameter("usuarioId", usuario.getIdUsuario())
                        .setParameter("perfilId", Context.getInstance().currentDMParametroGral().getPerfilSupervisor())
                        .getSingleResult();
                if(usuPerfil!=null)
                    usuario.setSupervisor(true);
                else
                    usuario.setSupervisor(false);
                
            } */   
            //if(usuario.getPassword().compareTo(password)==0)
            //        flagReturn=true;
            //else
            //    flagReturn=false;
        }catch(NoResultException e){
            log.info("No se encontró ningún usuario con nombre: "+nombre+" password: "+password);
        }catch(RuntimeException e){
            log.error("Error en la capa de servicios al autenticar usuario.",e);
            throw new TpvException("Error en la capa de servicios al autenticar usuario.");
        }finally{
            em.clear();
        }
        
        return usuario;
    }
    
    public void logout() throws TpvException{
        EntityManager em = Connection.getEm();
        EntityTransaction tx = em.getTransaction();
        Checkout checkout;
        try{
            tx.begin();
            checkout = em.find(Checkout.class,
                            Context.getInstance().currentDMTicket()
                              .getCheckout().getId());
            checkout.setTomado(false);
            tx.commit();
        }catch(RuntimeException e){
            if(tx!=null)
                tx.rollback();
            log.error("Error en la capa de servicios al liberar el checkout",e);
            throw new TpvException("Error en la capa de servicios al liberar el checkout");
        }finally{
            em.clear();
        }
                
    }
    
    public Usuario authenticarSupervisor(String nombre, String password, String codigoBarra)throws TpvException{
        log.info("Autenticando usuario supervisor: "+nombre);
        
        if(codigoBarra.length()!=13)
            return null;        
        StringBuffer sb = new StringBuffer(codigoBarra);
        StringBuffer sb2 = new StringBuffer(codigoBarra);
        for(int i = 7;i<sb.length();i++){
            String str = String.valueOf(Integer.parseInt(String.valueOf(sb.charAt(i)))+7);
            if(str.length()>1)
                sb2.replace(i, i+1,str.substring(1));
            else
                sb2.replace(i, i+1, str);
        }
        
        codigoBarra = sb2.toString();
        boolean flagReturn=false;
        Usuario usuario = null;
        UsuarioPerfil usuPerfil = null;
        EntityManager em = Connection.getEm();
        try{            
            usuario = (Usuario)em.createQuery("FROM Usuario u WHERE u.nombre = :nombre AND u.password = :clave AND u.codigoBarra = :codigoBarra")
                    .setParameter("nombre", nombre)
                    .setParameter("clave" , getMD5(password))
                    .setParameter("codigoBarra", codigoBarra)
                    .getSingleResult();
            if(usuario!=null){
                usuPerfil = (UsuarioPerfil)em.createQuery("FROM UsuarioPerfil up WHERE up.id.usuarioId = :usuarioId AND up.id.perfilId = :perfilId")
                        .setParameter("usuarioId", usuario.getIdUsuario())
                        .setParameter("perfilId", Context.getInstance().currentDMParametroGral().getPerfilSupervisor())
                        .getSingleResult();
                if(usuPerfil!=null)
                    flagReturn=true;
            }
                    
        }catch(NoResultException e){
            log.info("No se encontró ningún usuario con nombre: "+nombre+" password: "+password);
        }catch(RuntimeException e){
            log.error("Error en la capa de servicios al autenticar usuario.",e);
            throw new TpvException("Error en la capa de servicios al autenticar usuario.");
        }finally{
            em.clear();
        }
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
                log.info("MAC recuperada: "+mac);
                if(mac!=null){
                    EntityManager em = Connection.getEm();
                    EntityTransaction tx = null;
                    try{
                        tx = em.getTransaction();
                        tx.begin();
                        Query q = em.createQuery("FROM Checkout c WHERE c.placa = :placa AND c.habilitado=true").setParameter("placa",mac);    
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
                
        }catch(SocketException e){
            log.error("Error en la capa de servicios al recuperar la MAC",e);
            throw new TpvException("Error en la capa de servicios al recuperar la MAC");
        }
        return null;
    }
    
    private String getMD5(String clave){
        String md5 = "";
        try{
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(clave.getBytes(),0,clave.length());
            md5 = new BigInteger(1,digest.digest()).toString(16);
        }catch(NoSuchAlgorithmException e){
            
        }
        return md5;
    }
    
    /*
        Apertura de cajero:
        APERTURA = 1;
        CIERRE   = 0;
        en cabecera y detalle
    */
    
    public AperturaCierreCajeroDetalle verificarAperturaCaja(int idUsuario,int idCheckout) throws TpvException{
        log.info("Verificando apertura de caja para Uusuario Id: ");
        Query query;
        EntityManager em = Connection.getEm();
        AperturaCierreCajeroDetalle a = null;
        try{
            query = em.createQuery("FROM AperturaCierreCajeroDetalle a "
                    +" WHERE a.aperturaCierreCab.fecha = a.aperturaCierreCab.fechaHoy"
                    +" AND a.abierta = 1 AND a.cerrada = 0"
                    +" AND a.checkout.id = "+idCheckout
                    +" AND a.usuario.idUsuario = "+idUsuario
            );
            a = (AperturaCierreCajeroDetalle)query.getSingleResult();
        }catch(NoResultException e){
            log.warn("No se encontró ninguna apertura de caja para usuario: "+idUsuario
                    +" checkout: "+idCheckout);
            //throw new TpvException("La caja no está abierta");
        }catch(RuntimeException e){
            log.error("Error en la capa de servicios al verificar apertura de caja.",e);
            throw new TpvException("Error en la capa de servicios al verificar apertura de caja");
        }
        
            
        if(a!=null){
            EntityTransaction tx = em.getTransaction();
            Checkout checkout;
            try{
                tx.begin();
                checkout = em.find(Checkout.class,
                            Context.getInstance().currentDMTicket()
                                .getCheckout().getId()
                        );
                checkout.setTomado(true);
                tx.commit();
                log.info("Checkout tomado luego de la autenticación");
            }catch(RuntimeException e){
                if(tx!=null)
                    tx.rollback();
                log.error("Error en la capa de servicios al tomar el checkout.",e);
                throw new TpvException("Error en la capa de servicios al tomar el checkout.");
            }finally{
                em.clear();
            }
        }
            
            
            
        return a;

    }
    
    public static void main(String[] args){
        String md5 = "";
        String message = "luque123";

         
        message = message;//adding a salt to the string before it gets hashed.
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");//Create MessageDigest object for MD5
            digest.update(message.getBytes(), 0, message.length());//Update input string in message digest
            md5 = new BigInteger(1, digest.digest()).toString(16);//Converts message digest value in base 16 (hex)
  
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        System.out.println("MD5 resultante: "+md5);
    }
    
}
