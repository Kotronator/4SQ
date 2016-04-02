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
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
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
        MyLogger.log("Mapper has "+Runtime.getRuntime().availableProcessors()+" Processors");
        
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
                WorkHandler ch = new WorkHandler(client);
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

    
    private class WorkHandler extends Thread
    {
        Socket socket;
        private ObjectInputStream in;
        private ObjectOutputStream out;


        WorkHandler(Socket socket)
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
                in = new ObjectInputStream(socket.getInputStream());
                out = new ObjectOutputStream(socket.getOutputStream());
                Point[] points= new Point[2];
                            for (int i = 0; i < points.length; i++) {
                                    points[i] = new Point();

                            }
                MyLogger.log("Start Listening");
                Object pointstmp = in.readObject();

                            if(pointstmp instanceof Point[])
                            {
                                    points=(Point[])pointstmp;
                            }

                            MyLogger.log("P(0)"+points[0].x+","+points[0].y);
                            MyLogger.log("P(1)"+points[1].x+","+points[1].y);
                            ArrayList<CheckIn> list = askDataBase(points);
                            mapData(list);


            } catch (IOException | ClassNotFoundException ex)
            {
                Logger.getLogger(WorkHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        private ArrayList<CheckIn> askDataBase(Point[] points) {
            
            DBAgent dba = new DBAgent();
            ArrayList<CheckIn> results= dba.createQuery(DBAgent.formQueryWithPoints(points));

            return results;
//            Map<CheckinKey, List<CheckinValue>> data = mapList(results);
//            
//            Set set = data.entrySet();
//            Iterator it = set.iterator();
//            
//            int i=0;
//            while(it.hasNext()&& i<10)
//            {
//                Map.Entry entry = (Map.Entry )it.next();
//                System.out.println("Poi"+entry.getKey()+" Count:"+entry.getValue());
//                i++;
//            }
          
            
        }

        public Map<CheckinKey, List<CheckinValue>> mapList(ArrayList<CheckIn> input) 
        {

           Comparator<Map.Entry<String,Integer>> comp =Map.Entry.<String,Integer>comparingByValue(Comparator.reverseOrder())
                   .thenComparing(Map.Entry.comparingByKey());
            return  input.parallelStream().collect(  Collectors.groupingBy(CheckIn::getCheckinKey,Collectors.mapping(CheckIn::getCheckinValue, Collectors.toList())));
        }

        private void mapData(ArrayList<CheckIn> list) {
             Map<CheckinKey, List<CheckinValue>> data = mapList(list);
//            
            Set set = data.entrySet();
            Iterator it = set.iterator();
            
            int i=0;
            while(it.hasNext()&& i<10)
            {
                Map.Entry entry = (Map.Entry )it.next();
                System.out.println("Poi"+entry.getKey()+" Count:"+entry.getValue());
                i++;
            }
        }
         
        

    }
    

   
    
}
