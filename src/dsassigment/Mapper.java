/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dsassigment;

import com.sun.jmx.snmp.BerDecoder;
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.swing.JOptionPane;
import logger.MyLogger;

/**
 *
 * @author tsipiripo
 */
public class Mapper extends Thread implements MapWorker 
{
    ServerSocket serverSocket;
    int portNum=4321;
    private InetAddress reducerAddress;
    private int reducerPortNum;

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
//                reducerAddress= InetAddress.getByName(reducerAddressString);
//                reducerPortNum=Integer.parseInt(reducerPortString);
//
//
//        } catch (UnknownHostException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//        }
//        
//        MyLogger.log("Worker will sent results to"+ reducerAddress.getHostAddress() +":"+reducerPortNum);
//        
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
                WorkData workdata=null;
//                Point[] points= new Point[2];
//                            for (int i = 0; i < points.length; i++) {
//                                    points[i] = new Point();
//
//                            }
                MyLogger.log("Start Listening");
                Object workTemp = in.readObject();

                            if(workTemp instanceof WorkData)
                            {
                                    workdata=(WorkData)workTemp;
                            }

                            MyLogger.log("P(0)"+workdata.chq.boundPoints[0].x+","+workdata.chq.boundPoints[0].y);
                            MyLogger.log("P(1)"+workdata.chq.boundPoints[1].x+","+workdata.chq.boundPoints[1].y);
                            
                            long start, end; 
                            start = System.currentTimeMillis();
                            
                            ArrayList<CheckIn> list = askDataBase(workdata.chq);
                            System.out.println("Worker is processing:"+list.size()+" checkins");
                            List<Map<CheckinKey, List<CheckinValue>>> intermediate = mapData(list,workdata.chq);
                            
                            end = System.currentTimeMillis();
                            
                            System.out.println("Processing Time:"+(end-start)/1000.0);
                            
                            sendToReducer(intermediate, workdata);
//                            Set set = intermediate.entrySet();
//                            Iterator it = set.iterator();
//
//                            int i=0;
//                            while(it.hasNext()&& i<10)
//                            {
//                                Map.Entry entry = (Map.Entry )it.next();
//                                System.out.println("Poi:"+entry.getKey()+" Count:"+entry.getValue());
//                                i++;
//                            }


            } catch (IOException | ClassNotFoundException ex)
            {
                Logger.getLogger(WorkHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        private ArrayList<CheckIn> askDataBase(CheckinQuestion question) {
            
            DBAgent dba = new DBAgent();
            ArrayList<CheckIn> results= dba.createQuery(DBAgent.formQueryWithPoints(question));

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

        public List<Map<CheckinKey, List<CheckinValue>>> mapData(ArrayList<CheckIn> list,CheckinQuestion chq) 
        {

//           Comparator<Map.Entry<String,Integer>> comp =Map.Entry.<String,Integer>comparingByValue(Comparator.reverseOrder())
//                   .thenComparing(Map.Entry.comparingByKey());
//            return  input.parallelStream().collect(  Collectors.groupingBy(CheckIn::getCheckinKey,Collectors.mapping(CheckIn::getCheckinValue, Collectors.toList())));
            if(!chq.forwardTopK)
                return  splitData(list).parallelStream().map(p->p.collectCheckins(0)).collect(Collectors.toList());
            else
                return  splitData(list).parallelStream().map(p->p.collectCheckins(chq.getTopK())).collect(Collectors.toList());
     
        }

        private ArrayList<Area> splitData(ArrayList<CheckIn> list) {
  
            int processors = Runtime.getRuntime().availableProcessors();
            int partSize = list.size()/processors;
            ArrayList<Area> areas = new ArrayList<>(processors);
            
            if(list.size()>= processors*2){

                for (int i = 0; i < processors; i++)
                {
                    areas.add(new Area(list.subList(i*partSize, (i+1)*partSize)));
                }
                
            }else
            {
                areas.add(new Area(list));
            }
            
//             Map<CheckinKey, List<CheckinValue>> data = mapData(areas);
////            
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
            
            return areas;
        }

        private void sendToReducer(List<Map<CheckinKey, List<CheckinValue>>> intermediate, WorkData work)
        {
            try
            {
                
                Socket reducerSocket = new Socket(work.reducerAddr.split(":")[0], Integer.parseInt(work.reducerAddr.split(":")[1]));
                ObjectOutputStream out = new ObjectOutputStream(reducerSocket.getOutputStream());
		out.writeObject(new ReduceData(intermediate, work.chq.clientAddress,work.numOfWorkers,work.chq.getTopK()));
                MyLogger.log("Mapper wrotre:"+intermediate.size());
                MyLogger.log("A0:"+intermediate.get(0).size());
		out.flush();
                //reducerSocket.close();
                
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            } catch (IOException ex)
            {
                Logger.getLogger(Mapper.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
         
        

    }
    

   
    
}
