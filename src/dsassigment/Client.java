package dsassigment;

import application.ClientWindow;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import logger.MyLogger;

public class Client extends Thread {
	
    CheckinQuestion question;
    private boolean forwardTopK=false;
    private int topK;
    
    private int portClientListensTo=4320;
    
    ClientWindow clientWindow;

    public Client()
    {
        clientWindow = new ClientWindow(this);
    }
    
    
    
	//Point[] points;//latitude,longitude;
//        @Deprecated
//	public Client()
//	{
//		points = new Point [2];
//		for (int i = 0; i < points.length; i++) {
//			points[i] = new Point();
//			
//		}
//                
//                new ClientWindow(this);
//	}
//        
//        @Deprecated
//	private void askCoordinates() {
//		Scanner sc = new Scanner(System.in);
//		for (int i = 0; i < 2; i++) {
//			System.out.print("Point("+i+")latitude>");
//			points[i].x = sc.nextDouble();
//			System.out.print("Point("+i+")longitude>");
//			points[i].y = sc.nextDouble();
//			
//		}
//		sc.close();
//		
//		
//	}
//        @Deprecated
//        public void setPoints(Point[] points)
//        {
//            this.points=points;
//        }
//	
	@Override
	public void run() {
            
            try {
                ServerSocket serversSock = new ServerSocket(portClientListensTo);
                 System.out.println("Client Addr"+InetAddress.getLocalHost().getHostAddress()+":"+portClientListensTo);
                Socket connection = serversSock.accept();
                ObjectInputStream in = new ObjectInputStream(connection.getInputStream());
                Object maptmp = in.readObject();
                Map<CheckinKey, List<CheckinValue>> result=null;
                if(maptmp instanceof Map)   
                {
                    result=(Map<CheckinKey, List<CheckinValue>>) maptmp;
                }
                
                MyLogger.log("Client recieved result");
                //int i=0;
                for (Map.Entry<CheckinKey, List<CheckinValue>> entrySet : result.entrySet())
                {
                    //if(i>10)
                    //    break;

                    CheckinKey key = entrySet.getKey();
                    List<CheckinValue> value = entrySet.getValue();
                    MyLogger.log("CheckIn K:" + key+ "V size:"+ value.size()+ " | "+value);
                    
                    clientWindow.appendResultNL("POI_NAME:"+key.POI_name+ " count:"+ value.size());
                    //i++;
                }
                
                  MyLogger.log("Sending Results to Client");
                //askCoordinates();
                //sendPointsToManager();
            } catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
	}
	public  void sendPointsToManager()
	{
		try {
			Socket socketToComManager = new Socket("localhost", 4321);
			ObjectOutputStream out = new ObjectOutputStream (socketToComManager.getOutputStream());
                       
                        question.setClientAddress(InetAddress.getLocalHost().getHostAddress()+":"+portClientListensTo);
                        question.setForwardTopK(forwardTopK);
			out.writeObject(question);
			out.flush();
			//out.close();
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

    public void setQuestion(CheckinQuestion question)
    {
        this.question=question;
    }
//
//    public void setForwardTopK(boolean enabled) {
//        this.forwardTopK=enabled;
//        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
    
   


}
