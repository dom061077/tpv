/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.service;

import com.tpv.exceptions.TpvException;
import com.tpv.modelo.ParametroGeneral;
import com.tpv.util.Connection;
import java.util.List;
import javafx8tpv1.TabPanePrincipalController;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;

/**
 *
 * @author COMPUTOS
 */
public class UtilidadesService {
    
    public static List<ParametroGeneral> getParametroGral() throws TpvException{
        List<ParametroGeneral> parametros;
        EntityManager em = Connection.getEm();
        try{
            parametros = em.createQuery("FROM ParametroGeneral").getResultList();
        }catch(RuntimeException e){
            throw new TpvException("Error en la capa de servicios al devolver parámetros generales.");
        }finally{
            em.clear();
        }
        return parametros;
    } 
    
    public static ParametroGeneral getParametroGral(String id) throws TpvException{
        Logger log = Logger.getLogger(TabPanePrincipalController.class);
        ParametroGeneral param = null;
        EntityManager em = Connection.getEm();
        try{
            param = em.find(ParametroGeneral.class, id);
        }catch(RuntimeException e){
           log.error("Error al obtener parámetro general",e);
           throw new TpvException("Error en tiempo de ejecución: "+e.getMessage()); 
        }finally{
            em.clear();
        }
        if(param==null)
            throw new TpvException("El parámetro gral "+id+" no existe.");
        return param;
    }
    
}
