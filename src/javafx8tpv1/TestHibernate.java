/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafx8tpv1;

import com.tpv.modelo.Combo;
import com.tpv.modelo.Factura;
import com.tpv.modelo.GrupoProducto;
import com.tpv.modelo.ListaPrecioProducto;
import com.tpv.modelo.Producto;
import com.tpv.modelo.ProveedorProducto;
import com.tpv.service.FacturacionService;
import com.tpv.service.ImpresoraService;
import com.tpv.service.ProductoService;
import com.tpv.util.Connection;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
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
    
    static void nativeQuerySQL(){
        
        EntityManager em = Connection.getEm();
        Query q = em.createNativeQuery(
                "SELECT DISTINCT c.* FROM facturasdetalle fd"
                +" INNER JOIN productos p ON fd.idPRODUCTOS=p.idPRODUCTOS AND p.DISCONTINUADO = 0"
                +" LEFT JOIN combosgrupodetalle cgd ON fd.idPRODUCTOS = cgd.idproductos"
                +" LEFT JOIN combosgrupo cg ON cgd.idCOMBOSGRUPO = cg.idCOMBOSGRUPO"
                +" LEFT JOIN combos c ON cg.idCOMBOS = c.idCOMBOS"
                +" LEFT JOIN proveedores_productos pp ON fd.idPRODUCTOS = pp.idPRODUCTOS AND pp.idProveedor=cgd.idProveedor"
                +" LEFT JOIN ("
                +"        SELECT gp.idGRUPOPRODUCTOS AS grupohijo"
                +"                ,glevel1.idGRUPOPRODUCTOS AS grupopadre FROM grupoproductos gp"
                +"        INNER JOIN grupoproductos glevel1 ON glevel1.idgrupoproductos = gp.padreid"
                +" ) grupoprod ON p.idgrupoproductos = grupoprod.grupohijo AND(cgd.idgrupoproductos = grupoprod.grupohijo OR "
                +"                cgd.idgrupoproductos = grupoprod.grupopadre)"
                +" WHERE c.idcombos IS NOT NULL AND fd.idFACTURAS = ?1 AND CONVERT(NOW(),DATE) BETWEEN c.FECHADESDE AND c.FECHAHASTA"                
                , Combo.class).setParameter(1, 303);
                
        List listado = null;
        try{
            Combo combo = (Combo)q.getSingleResult();
            System.out.println("--------------------------------------");
            System.out.println("COMBO: "+combo.getDescripcion());
            System.out.println("FECHA desde: "+combo.getFechaDesde());
            System.out.println("FECHA hasta: "+combo.getFechaHasta());
            combo.getCombosGrupo().forEach(item->{
                System.out.println("ITEM: "+item.getPorcentaje());
            });
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    
    static void recuperarCombos(){
       FacturacionService facturaService = new FacturacionService();
       try{
            facturaService.calcularCombos(new Long(303));
       }catch(Exception e){
           e.printStackTrace();
       }
        
    }
    
    static void mostrarFacturaDetalle(){
        EntityManager em = Connection.getEm();
        try{
            
            Factura factura = em.find(Factura.class, new Long(303));
            factura.getDetalle().forEach(item ->{
                System.out.println("Producto: "+item.getProducto().getDescripcion());
                System.out.println("Cantidad: "+item.getCantidad());
                System.out.println("Cantidad para calculos: "+item.getCantidadAuxCombo());
            });
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    static void getProveedorFromProductos(){
        EntityManager em = Connection.getEm();
        try{
            Query q = em.createQuery("FROM Producto p ORDER BY p.descripcion");
            List<Producto> productos = q.setMaxResults(200).getResultList();
            productos.forEach(item->{
                System.out.println("Producto: "+item.getDescripcion());
                item.getProveedores().forEach(prov_prod->{
                    System.out.println("             Proveedor: "+prov_prod.getProveedor().getRazonSocial());
                });
            });
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
    
    static void getProveedorFromCombo(){
        EntityManager em = Connection.getEm();
        try{
            Query q = em.createQuery("FROM Combo c WHERE c.id = 1");
            List<Combo> combos = q.getResultList();
            combos.forEach(it ->{
                System.out.println("Combo: "+it.getDescripcion());
                it.getCombosGrupo().forEach(itcg->{
                    itcg.getGruposDetalle().forEach(itcgd->{
                        System.out.println("Proveedor de Grupo: "
                                +itcgd.getProveedor().getId()
                                +" "
                                +itcgd.getProveedor().getNombreFantasia());
                    });
                    
                });
            });
        }catch(Exception e){
            e.printStackTrace();
        }
            
        
        
    }
            
    
    static void probarRecursivadaGrupoProducto(){
        EntityManager em = Connection.getEm();
        try{
            GrupoProducto gp = em.find(GrupoProducto.class, new Long(1));
            System.out.println("Grupo: "+gp.getDescripcion());
            Query q = em.createQuery("FROM Producto p");
            List<Producto> productos = q.getResultList();
            productos.forEach(item->{
                if(item.tieneEsteGrupo(gp)){
                    System.out.println("PRODUCTO: "+item.getDescripcion()
                        +" - Grupo Producto: "+item.getGrupoProducto().getDescripcion());
                    
                }
            });
        }catch(Exception e){
           e.printStackTrace();
        }
    }
    
    static void calcularCombos(){
        EntityManager em = Connection.getEm();
        FacturacionService facService = new FacturacionService();
        try{
            facService.calcularCombos(new Long(303));
        }catch(Exception e){
           e.printStackTrace();
        }
    }
    

	public static String formatNumber(BigDecimal number, int integerPart, int decimalPart) {
		String num = number.toString();
		int pointIndex = num.indexOf('.');
		if(pointIndex == -1) {
			num = number + ".";
			for(int i = 1; i <= decimalPart; num += "0", i++);
		} else{ 
                        System.out.println("Substring: "+num.substring(pointIndex + 1));
                        int repetirceros = decimalPart- num.substring(pointIndex + 1).length();
                        for(int i = 1; i<=repetirceros; num+= "0", i++);
                    
                }
                
		return num;
	}
            
    public static void formatCodigoBarra(){
        String codigoBarra = "2097289000003";
        System.out.println("Inicio de codigo: "+codigoBarra.substring(0,2));
        System.out.println("Codigo dentro el codigo de barra: "+codigoBarra.substring(2,7));
        System.out.println("Peso o precio: "+codigoBarra.substring(7, 13));
        String precio = codigoBarra.substring(7,9)+"."+codigoBarra.substring(9, 13);
        System.out.println("String de precio armado: "+precio);
        BigDecimal precioFinal = BigDecimal.valueOf(Double.parseDouble(precio)).setScale(3,BigDecimal.ROUND_HALF_EVEN);
        System.out.println("BigDecimal final: "+precioFinal);
    }
        
    public static void main(String[] args){
//        DOMConfigurator.configure(TestHibernate.class.getResource("log4j.xml"));
//        try{
//            Connection.initConnections();
//            //System.out.println("Direccion MAC: "+Connection.getMACAddress());
//        }catch(Exception e){
//            e.printStackTrace();
//        }
        
       formatCodigoBarra();    
        //getProveedorFromCombo();
        //calcularCombos();
        
        //probarRecursivadaGrupoProducto();
        //mostrarFacturaDetalle();
        //recuperarCombos();
        //nativeQuerySQL();
        //recuperarCombos();
        //getProveedorFromProductos();
        
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
        
        
    }
    
}
