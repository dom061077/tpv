/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;


/**
 *
 * @author daniel
 */
public class TpvLogger extends Logger {
    private TpvLogger(String name, String resourceBundleName){
        super(name,resourceBundleName);
        final InputStream inputStream = TpvLogger.class.getResourceAsStream("logging.properties");
        try
        {
            LogManager.getLogManager().readConfiguration(inputStream);
        }
        catch (final IOException e)
        {
            Logger.getAnonymousLogger().severe("Could not load default logging.properties file");
            Logger.getAnonymousLogger().severe(e.getMessage());
        }        
    }
    
    public static TpvLogger getTpvLogger(String className){
        
        LogManager manager = LogManager.getLogManager();

        if( className == null ) {
            className = "";
        }

        Logger result = manager.getLogger( className );

        if( (result != null) && (result instanceof TpvLogger) ) {
            return( TpvLogger )result;
        }

        //

        TpvLogger newTpvLogger = new TpvLogger( className,null );

        manager.addLogger( newTpvLogger );

        return newTpvLogger;

    }
    
    public static TpvLogger getTpvLogger(Class clazz){
        return getTpvLogger(clazz.getName());
    }
    
    public void debug(String msg){
        if( this instanceof Logger){
            ((Logger)this).info(msg);
        }
        
    }
    
    public void info(String msg){
        if( this instanceof Logger){
            ((Logger)this).log(Level.INFO,msg);
        }
        
    }
    
    public void error(String msg){
        if( this instanceof Logger){
            //((Logger)this).
        }
        
    }
    
    public void fatal(String msg){
        if( this instanceof Logger){
            ((Logger)this).log(Level.INFO,msg);
        }
        
    }
    
}
