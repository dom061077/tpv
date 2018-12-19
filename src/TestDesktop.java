
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author daniel
 */
public class TestDesktop {
    enum TipoComprobanteEnum{
        FACTURA("F"),
        NOTA_CREDITO("NC");
        private String value;
        TipoComprobanteEnum(String value){
            this.value=value;
        }
        public String getValue(){
            return value;
        }
        
        @Override
        public String toString(){
            return this.getValue();
        }
    }
    
    public static void main(String[] args){
        /*EntityManagerFactory emf = Persistence.createEntityManagerFactory("tpvpersistence");        
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        tx.commit();
        em.close();
        System.exit(0);*/
        System.out.println(TipoComprobanteEnum.FACTURA);
    }
            
    
}
