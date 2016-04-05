package application;

import java.util.Scanner;
import java.util.logging.Logger;

import javax.swing.plaf.SliderUI;

import logger.MyLogger;
import dsassigment.Client;
import dsassigment.Manager;
import dsassigment.Mapper;
import dsassigment.Reducer;
import javax.swing.JOptionPane;

public class Application {
	
	static Scanner sc;

	public static void main(String[] args) {
		sc = new Scanner(System.in);
		showMenu();

	}

	private static void showMenu() {
		//System.out.println("Choose Num to pick role");
		//System.out.println("1 Client");
		//System.out.println("2 Mapper");
		//System.out.println("3 Reducer");

                String[] choices = { "Client", "Mapper", "Reducer"};
                String input = (String) JOptionPane.showInputDialog(null, "Role",
                    "PickRole", JOptionPane.QUESTION_MESSAGE, null, choices, choices[0]);
                System.out.println(input);

//		int choice;
//		do{
//			System.out.print(">");
//			choice = sc.nextInt();
//		}while(choice>3||choice<=0);
		
		if(input.equals(choices[0]))
		{
		
			//new Thread(new Manager()).start();
                   
			new Manager().start();
			MyLogger.log("Manager Thread Started");
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			new Thread(new Client()).start();
			
		}
		else if(input.equals(choices[1]))
		{
			new Thread(new Mapper()).start();
		}
                else if(input.equals(choices[2]))
		{
			new Thread(new Reducer()).start();
		}
		System.out.println();
	}

}
