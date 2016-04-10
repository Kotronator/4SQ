/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dsassigment;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.BindException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketImpl;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import logger.MyLogger;

/**
 *
 * @author tsipiripo
 */
public class Reducer extends Thread implements ReduceWorker 
{
    private InetAddress clientAddress;
    private int clientPortNum;
    private ServerSocket serverSocket;
    private int portNum=4321;
    
    List<Map<CheckinKey, List<CheckinValue>>> collectedData = new ArrayList<Map<CheckinKey, List<CheckinValue>>>();
    
    int countMapperReaded =0;
    int totalMappers =1;

    public Reducer()
    {
        MyLogger.log("Create Mapper");
    }

     public void run()
    {
        super.run(); //To change body of generated methods, choose Tools | Templates.
        MyLogger.log("intialize Reducer");
        intialize();
        waitForTasksThread();
        
    }
    
    
    @Override
    public void waitForMasterAck()
    {
//        MyLogger.log("Mapper starts waiting for tasks");
//        
//        try
//        {
//            do
//            {
//                Socket client = serverSocket.accept();
//                Reducer.WorkHandler ch = new Reducer.WorkHandler(client);
//                new Thread(ch).start();
//                
//            }while(true);
//            
//        } catch (IOException ex)
//        {
//            Logger.getLogger(Mapper.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    @Override
    public Map<Integer, Object> reduce(int key, Object val)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void sendResults(int key, Object val)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void intialize()
    {
//        String input = JOptionPane.showInputDialog("Enter Input:(123.123.123.123:80)");
//
//        String parts[]=input.split(":");
//        String reducerAddressString=parts[0];
//        String reducerPortString=parts[1];
//			
//	//int reducerPortNum=Integer.parseInt(reducerPortString);
//	//InetAddress  address=null;	
//        try {
//                System.out.println(InetAddress.getByName(reducerAddressString));
//                clientAddress = InetAddress.getByName(reducerAddressString);
//                clientPortNum = Integer.parseInt(reducerPortString);
//
//
//        } catch (UnknownHostException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//        }
        
        //MyLogger.log("Worker will sent results to"+ clientAddress.getHostAddress() +":"+clientPortNum);
        
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
            MyLogger.log("Reducer on address:"+ InetAddress.getLocalHost().getHostAddress()+" and port:"+portNum);
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        } catch (UnknownHostException ex)
        {
            Logger.getLogger(Mapper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void reduceData()
    {   
        MyLogger.log("Reducing "+ collectedData.size() );
        if(collectedData.size()>0)
            MyLogger.log("Reducing A0"+ collectedData.get(0).size() );
        
        HashMap<CheckinKey, List<CheckinValue>> finalValue = collectedData.stream().reduce(new MyMap(),(finalMap,partialMap)-> finalMap.addMap(partialMap),(finalMap1, finalMap2) -> finalMap1.addMap(finalMap2));
        
        int i=0;
        for (Map.Entry<CheckinKey, List<CheckinValue>> entrySet : finalValue.entrySet())
        {
            if(i>10)
                break;
            
            CheckinKey key = entrySet.getKey();
            List<CheckinValue> value = entrySet.getValue();
            MyLogger.log("CheckIn K:" + key+ "V size:"+ value.size()+ " | "+value);
            i++;
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
                Reducer.WorkHandler ch = new Reducer.WorkHandler(client);
                new Thread(ch).start();
                
            }while(true);
            
        } catch (IOException ex)
        {
            Logger.getLogger(Mapper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private class WorkHandler extends Thread
    {

        Socket socket;
        private ObjectInputStream in;
        private ObjectOutputStream out;


        WorkHandler(Socket socket)
        {
            this.socket=socket;
            MyLogger.log("ReductiontHandler created for socket"+socket.getInetAddress().getHostAddress());
        }

        @Override
        public void run()
        {
            try
            {
                in = new ObjectInputStream(socket.getInputStream());
                out = new ObjectOutputStream(socket.getOutputStream());
                List<Map<CheckinKey, List<CheckinValue>>> intermediate = null;

                MyLogger.log("Start Listening");
                while(countMapperReaded<totalMappers)
                {
                    Object pointstmp = in.readObject();
                    //ArrayList<HashMap<CheckinKey, ArrayList<CheckinValue>>> points
                    MyLogger.log("got object");
                    if(pointstmp instanceof ArrayList)
                    {
                        intermediate = (ArrayList<Map<CheckinKey, List<CheckinValue>>>) pointstmp;
                    }

                    if(intermediate!=null){
                        //collectedData.ad
                        MyLogger.log("Reducer  Readed:"+intermediate.size());
                        if(intermediate.size()>0)
                            MyLogger.log("Readed A0:"+intermediate.get(0).size());
                        collectedData.addAll(intermediate);
                    }
                    else
                        MyLogger.log("Empty income intermediate stream");
                    
                    countMapperReaded++;
                    MyLogger.log("Readed from "+countMapperReaded+" Mappers");
                }
                MyLogger.log("Reducing Data");
                reduceData();
                MyLogger.log("Data Reduced");
                    
                
                
            } catch (IOException ex)
            {
                Logger.getLogger(Reducer.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex)
            {
                Logger.getLogger(Reducer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        
    }
    
}
