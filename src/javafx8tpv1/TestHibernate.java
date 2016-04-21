/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafx8tpv1;

import com.tpv.modelo.GrupoProducto;
import com.tpv.modelo.ListaPrecioProducto;
import com.tpv.service.ImpresoraService;
import com.tpv.util.Connection;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

/**
 *
 * @author daniel
 */
public class TestHibernate {
    
    public static void getMACAddress(){
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
            System.out.println(sb.toString());            
	} catch (UnknownHostException e) {
		
		e.printStackTrace();
		
	} catch (SocketException e){
			
		e.printStackTrace();
			
	}

    }
    
    public static void main(String[] args){
//        EntityManager em = Connection.getEm();
//        Query q = em.createQuery("FROM ListaPrecioProducto p");
//        List<ListaPrecioProducto> lista=null;
//        try{
//            lista = q.getResultList();
//        }catch(Exception e){
//            e.printStackTrace();
//        }
//        lista.forEach(item -> {
//            System.out.println("Item: "
//                        +item.getProducto().getDescripcion()
//                        +", precio: "+item.getPrecioFinal());
//        });
//        System.out.println(lista.getClass().toString());
    
//            BigDecimal a = new BigDecimal("4.4182");
//            BigDecimal b = new BigDecimal("10.1255");
//
//            a = a.setScale(2, BigDecimal.ROUND_HALF_EVEN);
//            b = b.setScale(2, BigDecimal.ROUND_HALF_EVEN);
//
//            System.out.println(a);
//            System.out.println(b);

//        EntityManager em = Connection.getEm();
//        Query q = em.createQuery("FROM GrupoProducto gp");
//        List<GrupoProducto> lista = null;
//        try{
//            lista = q.getResultList();
//        }catch(Exception e){
//            e.printStackTrace();
//        }
//        lista.forEach(item ->{
//            System.out.println("Grupo: "+item.getDescripcion()
//                    +"      Padre: "+(item.getGrupoPadre() != null ? item.getGrupoPadre().getDescripcion() : "")
//            );
//        });
//        ImpresoraService impresoraService;
//        try{
//            Connection.initFiscalPrinter();
//            impresoraService = new ImpresoraService();
//            impresoraService.getPrinterVersion();
//        }catch(Exception e){
//            e.printStackTrace();
//        }
        getMACAddress();
    }
    
}
