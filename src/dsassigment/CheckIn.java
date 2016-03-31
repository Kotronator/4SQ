/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dsassigment;

import com.sun.corba.se.spi.ior.Identifiable;
import java.sql.Date;
import org.omg.CORBA_2_3.portable.OutputStream;

/**
 *
 * @author Giannis
 */
class CheckIn 
{
    
    int id,  user, POI_category_id;
    String POI,  POI_name, POI_category, photos;
    double latitude, longitude; 
    Date time;
    
    CheckIn(int id, int user, String POI, String POI_name, String POI_category, int POI_category_id, double latitude, double longitude, Date time, String photos)
    {
        this.id=id;
        this.user=user;
        this.POI=POI;
        this.POI_name=POI_name;
        this.POI_category=POI_category;
        this.POI_category_id= POI_category_id;
        this.latitude=latitude;
        this.longitude=longitude;
        this.time=time;
        this.photos=photos;
    }

   
    public static String getPoi(CheckIn c)
    {
        return c.POI;
    }

    
}
