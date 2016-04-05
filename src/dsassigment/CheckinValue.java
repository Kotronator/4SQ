/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dsassigment;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;

/**
 *
 * @author TOSHIBA
 */
public class CheckinValue implements Serializable{
    
    private static final long serialVersionUID = 5497841316127816969L;
    
    String photoURL;
    Double longtitude,latitude;
    
    public CheckinValue(String photoURL,Double longtitude, Double latitude)
    {
        this.photoURL=photoURL;
        this.longtitude=longtitude;
        this.latitude=latitude;
    }
    
    public CheckinValue getCheckinValue(){ return this;}

    @Override
    public String toString() {
        return "Photo:"+photoURL+" Long:"+longtitude+" Lati:"+latitude;
    }

    @Override
    public boolean equals(Object obj)
    {
         if (!(obj instanceof CheckinValue)) {
            return false;
        }
        
        CheckinValue ch = (CheckinValue)obj;
        return photoURL.equals(ch.photoURL)&& longtitude == ch.longtitude && latitude == ch.latitude;
    }

    
    
}
