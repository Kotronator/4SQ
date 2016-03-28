/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dsassigment;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import logger.MyLogger;

/**
 *
 * @author tsipiripo
 */
public class ClientHandler extends Thread
{
    Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    
    
    ClientHandler(Socket socket)
    {
        this.socket=socket;
        MyLogger.log("ClientHandler created for socket"+socket.getInetAddress().getHostAddress());
    }

   
    @Override
    public void run()
    {
        
        try
        {
            //super.run(); //To change body of generated methods, choose Tools | Templates.
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            MyLogger.log("Start Listening");
            String ans = in.readUTF();
            if(ans.equals("4SQ"))
            {
                double x1=in.readDouble();
                double y1=in.readDouble();
                double x2=in.readDouble();
                double y2=in.readDouble();
                DBAgent dbag= new DBAgent();
                dbag.createQuery(""+((int)x1));
            }
            
            
        } catch (IOException ex)
        {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    
    
}
