/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dsassigment;

import java.util.List;
import java.util.Map;
import java.util.stream.Collector;

/**
 *
 * @author TOSHIBA
 */
public class CheckinValue {
    
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

    
    
}
