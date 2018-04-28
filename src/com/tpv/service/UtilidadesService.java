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
import javax.persistence.EntityManager;

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
            throw new TpvException("Error en la capa de servicios al devolver la factura.");
        }finally{
            em.clear();
        }
        return parametros;
    } 
    
}
