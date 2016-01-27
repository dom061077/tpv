/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.util;

import javafx.concurrent.Task;
import javafx.concurrent.Worker;

/**
 *
 * @author daniel
 */
public class TaskMonitor {
    private Worker<String> worker;
    
    public TaskMonitor(){
        /*
        worker = new Task<String>(){
            @Override
            protected String call(){
                updateTitle("Monitor de Conexiones");
                updateMessage("Comenzando...");
                while(true){
                    if(isCancelled()){
                     
                        return null;
                    }else{
                        return null;
                    }
                }
                return "";
            }
            
            @Override
            protected void scheduled() {
                System.out.println("The task is scheduled.");
            }

            @Override
            protected void running() {
                System.out.println("The task is running.");
            }
        };*/
    }
    
    public static void main(String args[]){
        Runnable task = () -> {
            System.out.println("Corriendo el hilo");
        };
                
        new Thread(task).start();
    }
    
}
