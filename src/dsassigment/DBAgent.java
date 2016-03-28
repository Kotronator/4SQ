/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dsassigment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import logger.MyLogger;

/**
 *
 * @author tsipiripo
 */
public class DBAgent
{
    String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
    String DB_URL = "jdbc:mysql://localhost/mysocialnetwork";
    String USER = "root";//"auebuser";
    String PASS = "";//"3120077pauebDS";
    Connection conn = null;
    Statement stmt = null;
    
    DBAgent()
    {
        connect();
    }
    
    public void createQuery(String query)
    {
        try
        {
            System.out.println("Creating statement...");
            stmt = conn.createStatement();
            String sql;
            sql = "SELECT id, POI_category_id FROM checkins where POI_category_id="+query;
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
        } catch (SQLException ex)
        {
            Logger.getLogger(DBAgent.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void connect()
    {
           

            //  Database credentials
           

        
        try{
                //STEP 2: Register JDBC driver
                //Class.forName("com.mysql.jdbc.Driver");

                //STEP 3: Open a connection
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);

            
            
        
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
//            try{
//                if(conn!=null)
//                   conn.close();
//                MyLogger.log("MySQLServer Connection Closed");
//            }catch(SQLException se){
//                se.printStackTrace();
//            }//end finally try
        }//end try
        System.out.println("Goodbye!");
            /*//STEP 4: Execute a query
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
          // TODO code application logic here*/
      }
}
