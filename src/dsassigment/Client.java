package dsassigment;

import application.ClientWindow;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client extends Thread {
	
	Point[] points;//latitude,longitude;
	public Client()
	{
		points = new Point [2];
		for (int i = 0; i < points.length; i++) {
			points[i] = new Point();
			
		}
                
                new ClientWindow(this);
	}

	private void askCoordinates() {
		Scanner sc = new Scanner(System.in);
		for (int i = 0; i < 2; i++) {
			System.out.print("Point("+i+")latitude>");
			points[i].x = sc.nextDouble();
			System.out.print("Point("+i+")longitude>");
			points[i].y = sc.nextDouble();
			
		}
		sc.close();
		
		
	}
        public void setPoints(Point[] points)
        {
            this.points=points;
        }
	
	@Override
	public void run() {
		//askCoordinates();
		//sendPointsToManager();
	}
	public  void sendPointsToManager()
	{
		try {
			Socket socketToComManager = new Socket("localhost", 4321);
			ObjectOutputStream out = new ObjectOutputStream (socketToComManager.getOutputStream());
			out.writeObject(points);
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

}
