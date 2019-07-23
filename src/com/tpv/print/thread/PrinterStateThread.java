/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.print.thread;

import com.tpv.exceptions.TpvException;
import com.tpv.service.ImpresoraService;
import javafx.application.Platform;
import javafx8tpv1.TabPanePrincipalController;

/**
 *
 * @author COMPUTOS
 */
public class PrinterStateThread {
    private ImpresoraService impresoraService = new ImpresoraService();
    private Thread stateThread ;
    private boolean flagEstadoImpresora;
    private boolean flagCheckPrinter = false;
    private TabPanePrincipalController tabController;
    
    public PrinterStateThread(TabPanePrincipalController tabPane){
        this.tabController = tabPane;
        initThread();
    }
    
    public void setEstadoGral(boolean estado){
        flagCheckPrinter = estado;
    }
            
    
    private void initThread(){
        stateThread = new Thread(new Runnable(){
            @Override
            public void run(){
                flagEstadoImpresora = false;
                while(!flagEstadoImpresora ){
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {
                        
                    }
                    if (flagCheckPrinter)
                        try{
                            impresoraService.enviarConsultaEstado();
                            tabController.setMsgImpresoraVisible(false);
                            if (tabController.getStackPaneImpresoraEsperando()==
                                    tabController.getNextFocusedNode())
                                tabController.repeatFocus(tabController.getCurrentFocusedNode());
                        }catch(TpvException e){
                           Platform.runLater(new Runnable(){
                               public void run(){
                                   if(flagCheckPrinter)
                                        tabController.setMsgImpresoraVisible(true);
                               }
                           });

                        }                     
                }
            } 
        });
    }
    
    public void run(){
        stateThread.setDaemon(true);
        stateThread.start();
    }
}
