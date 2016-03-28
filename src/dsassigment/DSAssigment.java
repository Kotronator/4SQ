/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dsassigment;


import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author tsipiripo
 */
public class DSAssigment
{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        //testDB();
        testMapper();
    }
    
    public static void testMapper() 
    {
        try
        {
            new Mapper();
            Thread.sleep(1000);
            try
            {
                Socket sc = new Socket("localhost", 4321);
                DataOutputStream out = new DataOutputStream(sc.getOutputStream());
                out.writeUTF("4SQ");
                 out.flush();
                for (int i = 1; i <= 4; i++)
                {
                   out.writeDouble(i+1); 
                    out.flush();
                }
                //out.flush();
                
                //new Mapper();
            } catch (IOException ex)
            {
                Logger.getLogger(DSAssigment.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (InterruptedException ex)
        {
            Logger.getLogger(DSAssigment.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void testDB()
    {
        String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
        String DB_URL = "jdbc:mysql://localhost/mysocialnetwork";

        //  Database credentials
        String USER = "auebuser";
        String PASS = "3120077pauebDS";
                
         Connection conn = null;
        Statement stmt = null;
        try{
           //STEP 2: Register JDBC driver
           //Class.forName("com.mysql.jdbc.Driver");

           //STEP 3: Open a connection
           System.out.println("Connecting to database...");
           conn = DriverManager.getConnection(DB_URL,USER,PASS);

           //STEP 4: Execute a query
           System.out.println("Creating statement...");
           stmt = conn.createStatement();
           String sql;
           sql = "SELECT id, POI_category_id FROM checkins where POI_category_id<2";
           ResultSet rs = stmt.executeQuery(sql);
           
           //STEP 5: Extract data from result set
           while(rs.next()){
              //Retrieve by column name
              int id  = rs.getInt("id");
              int age = rs.getInt("POI_category_id");
             

              //Display values
              System.out.print("ID: " + id);
              System.out.println(", POI_category_id: " + age);
            
           }
           //STEP 6: Clean-up environment
           rs.close();
           stmt.close();
           conn.close();
        }catch(SQLException se){
           //Handle errors for JDBC
           se.printStackTrace();
        }catch(Exception e){
           //Handle errors for Class.forName
           e.printStackTrace();
        }finally{
           //finally block used to close resources
           try{
              if(stmt!=null)
                 stmt.close();
           }catch(SQLException se2){
           }// nothing we can do
           try{
              if(conn!=null)
                 conn.close();
           }catch(SQLException se){
              se.printStackTrace();
           }//end finally try
        }//end try
        System.out.println("Goodbye!");
        // TODO code application logic here
    }
}
