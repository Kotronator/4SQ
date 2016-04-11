package dsassigment;

import application.ManagerWindow;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Logger;

import logger.MyLogger;

public class Manager extends Thread{

	ServerSocket serverSocket;
	ArrayList<InetAddress > workersAddreses;
	ArrayList<Integer > ports;
    private String reducerAddress;
	
	public Manager()
	{
		workersAddreses= new ArrayList<InetAddress>();
		ports= new ArrayList<Integer>();
                
                new ManagerWindow(this);
//		try {
//			workersAddreses.add(InetAddress.getByName("172.16.2.34"));
//			ports.add(123);
//		} catch (UnknownHostException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
	}
        
        public void addWorker(InetAddress address, int port)
        {
            workersAddreses.add(address);
            ports.add(port);
        }

    @Override
    public synchronized void start()
    {
        new Thread(this).start();
    }
	 
        
	@Override
	public void run() {
		System.out.println("Starting Manager");
		try {
			//askWorkersIP(); //----------------------------------------------------ask ip
			serverSocket = new ServerSocket(4321);			
			while(true)
			{
				MyLogger.log("Waiting Connection");
				Socket clientCon =serverSocket.accept();
				MyLogger.log("Accept socket"+clientCon.getInetAddress().getHostAddress());
				new Thread(new ClientRequestHandler(clientCon)).start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void askWorkersIP() {
		String ans="";
		Scanner sc = new Scanner(System.in);
		do {
			
			System.out.print("Worker Address:");
			ans=sc.nextLine();
			
			if(ans.trim().equals(""))
				break;
			String addressString,portString;
			
			String parts[]=ans.split(":");
			addressString=parts[0];
			portString=parts[1];
			
			int portNum=Integer.parseInt(portString);
			
			try {
				System.out.println(InetAddress.getByName(addressString));
				InetAddress  address= InetAddress.getByName(addressString);
				workersAddreses.add(address);
				ports.add(portNum);
				
				
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} while (!ans.trim().equals(""));
		
		for (int i = 0; i < workersAddreses.size(); i++) {
			MyLogger.log("Addr("+i+")"+workersAddreses.get(i).getHostAddress()+":"+ports.get(i));
		}
		
	}

    public void setReducerAddress(String text)
    {
        this.reducerAddress=text;
    }

	private class ClientRequestHandler extends Thread
	{
		Socket clientCon;
		ObjectInputStream in;
		ObjectOutputStream out;
		
		//Point[] points;//latitude,longitude;
                
                CheckinQuestion question;
		
		ClientRequestHandler(Socket clientCon)
		{
			this.clientCon=clientCon;
		}
		
		
		@Override
		public void run() {
//			points= new Point[2];
//			for (int i = 0; i < points.length; i++) {
//				points[i] = new Point();
//				
//			}
			
			try {
				in = new ObjectInputStream(clientCon.getInputStream());
				out = new ObjectOutputStream(clientCon.getOutputStream());
				Object questiontmp = in.readObject();
				
				if(questiontmp instanceof CheckinQuestion)
				{
					question=(CheckinQuestion)questiontmp;
				}
				
				MyLogger.log("Server Recieved P(0)"+question.boundPoints[0].x+","+question.boundPoints[0].y+" P(1)"+question.boundPoints[1].x+","+question.boundPoints[1].y);
				//MyLogger.log(");
				
				int NumOfWorkersToUse = workersAddreses.size();
				double dx = question.boundPoints[1].x-question.boundPoints[0].x;
				double dy = question.boundPoints[1].y-question.boundPoints[0].y;
				
				double xbound= dx/(double)NumOfWorkersToUse;
				//double ybound= dy;
				
                                ArrayList<CheckinQuestion> questions = new ArrayList<CheckinQuestion>();
				//ArrayList<CheckinQuestion> startPoints = new ArrayList<CheckinQuestion>();
				//ArrayList<CheckinQuestion> endPoints = new ArrayList<CheckinQuestion>();
				for (int i = 0; i < NumOfWorkersToUse; i++) {
					Point tmpPoint1 = new Point(question.boundPoints[0].x+i*xbound, question.boundPoints[0].y);
					//startPoints.add(tmpPoint1);
					Point tmpPoint2 = new Point(tmpPoint1.x+xbound, tmpPoint1.y+dy);
                                        CheckinQuestion q =new CheckinQuestion(new Point[]{tmpPoint1,tmpPoint2},question.tb);
                                        q.setClientAddress(question.clientAddress);
                                        q.setTopK(question.getTopK());
                                        questions.add(q);
					//endPoints.add(tmpPoint2);
				}
				
				for (int i = 0; i < workersAddreses.size(); i++) {
					//SendWorkToWorker work = new SendWorkToWorker(workersAddreses.get(i),ports.get(i),startPoints.get(i),endPoints.get(i));
                                        SendWorkToWorker work = new SendWorkToWorker(workersAddreses.get(i),ports.get(i),questions.get(i));
					work.sendWork();
				}
			
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private class SendWorkToWorker //extends Thread
	{
		InetAddress address;
		int port;
//		Point start;
//		Point end;
                CheckinQuestion questionToWork;
		
		public SendWorkToWorker(InetAddress address, int port, CheckinQuestion questionToWork)
		{
			this.address=address;
			this.port=port;
			this.questionToWork=questionToWork;
			//this.end=end;
		}
		
		public void sendWork()
		{
			try {
				Socket socket = new Socket(address, port);
				ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
//				Point[] points= new Point[2];
//				points[0]=start;
//				points[1]=end;
				out.writeObject(new WorkData(questionToWork, reducerAddress, workersAddreses.size()));
				out.flush();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		
	}
}
