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
import java.util.ArrayList;
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
    String DB_URL = "jdbc:mysql://83.212.117.76";
    String USER = "omada64";//"auebuser";
    String PASS = "omada64db";//"3120077pauebDS";
    Connection conn = null;
    Statement stmt = null;
    
    DBAgent()
    {
        connect();
    }
    
    public static String formQueryWithPoints(Point[] points)
    {
        if(points.length!=2)
            throw new UnsupportedOperationException("Trying to create a query with bad points");
        Point UpLeft = points[0];
        Point BotRight = points[1];
        return "SELECT * FROM ds_systems_2016.checkins where longitude>"+UpLeft.x+" and longitude<"+BotRight.x+" and latitude<"+UpLeft.y+" and latitude>"+BotRight.y;
        
    }
    
    public ArrayList<CheckIn> createQuery(String query)
    {
        ArrayList results=null;
        try
        {
            System.out.println("Creating statement...");
            stmt = conn.createStatement();
            String sql;
            sql = query;//"SELECT * FROM checkins where POI_category_id="+query;
            ResultSet rs = stmt.executeQuery(sql);
            results = new ArrayList<CheckIn>();
            
            //rs.
            
            //STEP 5: Extract data from result set
            while(rs.next()){
                //Retrieve by column name
                int id  = rs.getInt("id");
                int age = rs.getInt("POI_category_id");
                
                results.add(new CheckIn(rs.getInt("id"), rs.getInt("user"), rs.getString("POI"), rs.getString("POI_name"), rs.getString("POI_category"), rs.getInt("POI_category_id"), rs.getDouble("latitude"), rs.getInt("longitude"), rs.getDate("time"), rs.getString("photos")) );
                        //CheckIn(int id, int user, String POI, String POI_name, String POI_category, int POI_category_id, double latitude, double longitude, Date time, String photos)
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
        return results;
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
