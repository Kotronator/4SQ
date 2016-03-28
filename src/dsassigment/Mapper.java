/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dsassigment;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.net.BindException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import logger.MyLogger;

/**
 *
 * @author tsipiripo
 */
public class Mapper extends Thread implements MapWorker 
{
    ServerSocket serverSocket;
    int portNum=4321;

    public Mapper()
    {
        MyLogger.log("Create Mapper");
       new Thread(this).start();
    }

    @Override
    public void run()
    {
        super.run(); //To change body of generated methods, choose Tools | Templates.
        MyLogger.log("intialize Mapper");
        intialize();
        waitForTasksThread();
        
    }
    
    
    
    @Override
    public void intialize()
    {
        boolean retry;
        do{
             retry=false;
            try
            {
                serverSocket = new ServerSocket(portNum);
                
            }
            catch (BindException ex)
            {
                retry=true;
                portNum++;
            }
            catch (IOException ex)
            {
                Logger.getLogger(Mapper.class.getName()).log(Level.SEVERE, null, ex);
            }
        }while(retry);
                        
        
        try
        {
            MyLogger.log("Worker on address:"+ InetAddress.getLocalHost().getHostAddress()+" and port:"+portNum);
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        } catch (UnknownHostException ex)
        {
            Logger.getLogger(Mapper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void waitForTasksThread()
    {
        
        MyLogger.log("Mapper starts waiting for tasks");
        
        try
        {
            do
            {
                Socket client = serverSocket.accept();
                ClientHandler ch = new ClientHandler(client);
                new Thread(ch).start();
                
            }while(true);
            
        } catch (IOException ex)
        {
            Logger.getLogger(Mapper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public Map<Integer, Object> map(Object key, Object val)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void notifyMaster()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void sentToReducers(Map<Integer, Object> intermediate)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    

    

   
    
}
