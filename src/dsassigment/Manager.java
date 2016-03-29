package dsassigment;

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
	
	public Manager()
	{
		workersAddreses= new ArrayList<InetAddress>();
		ports= new ArrayList<Integer>();
//		try {
//			workersAddreses.add(InetAddress.getByName("172.16.2.34"));
//			ports.add(123);
//		} catch (UnknownHostException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
	}
	
	@Override
	public void run() {
		System.out.println("Starting Manager");
		try {
			askWorkersIP(); //----------------------------------------------------ask ip
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

	private class ClientRequestHandler extends Thread
	{
		Socket clientCon;
		ObjectInputStream in;
		ObjectOutputStream out;
		
		Point[] points;//latitude,longitude;
		
		ClientRequestHandler(Socket clientCon)
		{
			this.clientCon=clientCon;
		}
		
		
		@Override
		public void run() {
			points= new Point[2];
			for (int i = 0; i < points.length; i++) {
				points[i] = new Point();
				
			}
			
			try {
				in = new ObjectInputStream(clientCon.getInputStream());
				out = new ObjectOutputStream(clientCon.getOutputStream());
				Object pointstmp = in.readObject();
				
				if(pointstmp instanceof Point[])
				{
					points=(Point[])pointstmp;
				}
				
				MyLogger.log("P(0)"+points[0].x+","+points[0].y);
				MyLogger.log("P(1)"+points[1].x+","+points[1].y);
				
				int NumOfWorkersToUse = workersAddreses.size();
				double dx = points[1].x-points[0].x;
				double dy = points[1].y-points[0].y;
				
				double xbound= dx/(double)NumOfWorkersToUse;
				//double ybound= dy;
				
				ArrayList<Point> startPoints = new ArrayList<Point>();
				ArrayList<Point> endPoints = new ArrayList<Point>();
				for (int i = 0; i < NumOfWorkersToUse; i++) {
					Point tmpPoint1 = new Point(points[0].x+i*xbound, points[0].y);
					startPoints.add(tmpPoint1);
					Point tmpPoint2 = new Point(tmpPoint1.x+xbound, tmpPoint1.y+dy);
					endPoints.add(tmpPoint2);
				}
				
				for (int i = 0; i < workersAddreses.size(); i++) {
					SendWorkToWorkers work = new SendWorkToWorkers(workersAddreses.get(i),ports.get(i),startPoints.get(i),endPoints.get(i));
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
	
	private class SendWorkToWorkers //extends Thread
	{
		InetAddress address;
		int port;
		Point start;
		Point end;
		
		public SendWorkToWorkers(InetAddress address, int port, Point start,Point end)
		{
			this.address=address;
			this.port=port;
			this.start=start;
			this.end=end;
		}
		
		public void sendWork()
		{
			try {
				Socket socket = new Socket(address, port);
				ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
				Point[] points= new Point[2];
				points[0]=start;
				points[1]=end;
				out.writeObject(points);
				out.flush();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		
	}
}
