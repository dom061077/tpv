/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.print.event;

import com.tpv.service.ImpresoraService;
import org.tpv.print.fiscal.FiscalPacket;
import org.tpv.print.fiscal.FiscalPrinter;
import org.tpv.print.fiscal.FiscalPrinterEventListener;
import org.tpv.print.fiscal.msg.FiscalMessages;

/**
 *
 * @author daniel
 */
public class FiscalPrinterEvent implements FiscalPrinterEventListener {
    
    
    
    
    @Override
    public void commandExecuted(FiscalPrinter source, FiscalPacket command, FiscalPacket response){
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }    

    @Override
    public void statusChanged(FiscalPrinter source, FiscalPacket command, FiscalPacket response, FiscalMessages msgs) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void printEnded(FiscalPrinter source, FiscalMessages msgs) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void fiscalCloseEnded(FiscalPrinter source, FiscalMessages msgs) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void openDrawerEnded(FiscalPrinter source, FiscalMessages msgs) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
        
}
