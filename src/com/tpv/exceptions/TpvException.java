/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.exceptions;

import java.util.Iterator;
import org.tpv.print.fiscal.exception.FiscalPrinterStatusError;
import org.tpv.print.fiscal.msg.FiscalMessage;

/**
 *
 * @author daniel
 */
public class TpvException extends Exception {
    
    private Exception exceptionOrigen;
    
    public TpvException(){
        
    }
    
    public TpvException(String s,Exception e){
        super(s);
        this.exceptionOrigen = e;
    }
    
    public TpvException(String s){
        super(s);
    }

    /**
     * @return the exceptionOrigen
     */
    public Exception getExceptionOrigen() {
        return exceptionOrigen;
    }
    
    public String getFiscalErrorMsg(){
        String msg = "";
        if(exceptionOrigen instanceof FiscalPrinterStatusError){
            
            
            for(Iterator fmsg = ((FiscalPrinterStatusError)exceptionOrigen).getErrorMsgs().iterator();fmsg.hasNext();){
                msg+=((FiscalMessage)fmsg.next()).getTitle();
            }
        }
        return msg;
    }
    
    
    
}
