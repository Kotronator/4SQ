package application;

import java.util.Scanner;
import java.util.logging.Logger;

import javax.swing.plaf.SliderUI;

import logger.MyLogger;
import dsassigment.Client;
import dsassigment.Manager;
import dsassigment.Mapper;

public class Application {
	
	static Scanner sc;

	public static void main(String[] args) {
		sc = new Scanner(System.in);
		showMenu();

	}

	private static void showMenu() {
		System.out.println("Choose Num to pick role");
		System.out.println("1 Client");
		System.out.println("2 Mapper");
		System.out.println("3 Reducer");
		int choice;
		do{
			System.out.print(">");
			choice = sc.nextInt();
		}while(choice>3||choice<=0);
		
		if(choice==1)
		{
		
			new Thread(new Manager()).start();
			
			MyLogger.log("Manager Thread Started");
			
			try {
				Thread.sleep(15000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			new Thread(new Client()).start();
			
		}
		else if(choice==2)
		{
			new Thread(new Mapper()).start();
		}
		else
		{
			
		}
		System.out.println();
	}

}
