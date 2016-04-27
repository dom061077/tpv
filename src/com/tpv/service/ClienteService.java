/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.service;


import com.tpv.exceptions.TpvException;
import com.tpv.modelo.Cliente;
import com.tpv.util.Connection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import org.apache.log4j.Logger;

/**
 *
 * @author daniel
 */
public class ClienteService {
    Logger log = Logger.getLogger(ClienteService.class);
    public Cliente getClientePorCodYDni(int filtroCodigo) throws TpvException{
        log.info("Capa de servicios, recuperar cliente por parametro: "+filtroCodigo);
        Cliente cliente = null;
        EntityManager em = Connection.getEm();
        EntityTransaction tx = null;
        try{
            
            tx = em.getTransaction();
            tx.begin();
            Query q = em.createQuery("FROM Cliente c WHERE c.id = :id or c.dni = :dni").setParameter("id"
                    ,filtroCodigo).setParameter("dni", filtroCodigo);
            cliente = (Cliente)q.getSingleResult();
            tx.commit();
            log.info("Cliente recuperado "+cliente.getCuit()+" - "+cliente.getRazonSocial());
        }catch(NoResultException e){
            tx.rollback();
        }catch(RuntimeException e){
            tx.rollback();
//            String fullTraceStr=e.getMessage()+"\n";
//            for(int i=0;i<=e.getStackTrace().length-1;i++){
//                fullTraceStr+="Clase: "+e.getStackTrace()[i].getClassName()+"; "
//                        +"Archivo: "+e.getStackTrace()[i].getFileName()+"; "
//                        +"Mètodo: "+e.getStackTrace()[i].getMethodName()+"; "
//                        +"Nro. Línea: "+e.getStackTrace()[i].getLineNumber()+"; "
//                        +"\n";
//            }
            log.error("Error en la capa de servicios de cliente al recuperar cliente por cod. o D.N.I",e);
            throw new TpvException("Error en la capa de servicios de cliente al recuperar cliente por cod. o D.N.I.");
        }finally{
            em.clear();
        }
       return cliente;         
    }
    
    public List getClientes(String filtro) throws TpvException{
        log.info("Capa de servicios, recuperar clientes por parámetro búsqueda: "+filtro);
        List<Cliente> clientes;
        EntityManager em = Connection.getEm();
        EntityTransaction tx = null;
        try{
            tx = em.getTransaction();
            tx.begin();
            int codigoProducto=0;
            Query q = em.createQuery("FROM Cliente c WHERE c.razonSocial like  :razonSocial");
            q.setParameter("razonSocial", "%"+filtro+"%");
            q.setFirstResult(0);
            q.setMaxResults(100);
            clientes = q.getResultList();
            log.info("Clientes recuperados, cantidad: "+clientes.size());
        }catch(RuntimeException e){
            tx.rollback();
            String fullTraceStr=e.getMessage()+"\n";
            log.error("Error en la capa de servicios al recuperar listado de clientes",e);
            throw new TpvException("Error en la capa de servicios al recuperar listado de clientes.");
        }finally{
            em.clear();
        }
        
        
        return clientes;
    }
}
