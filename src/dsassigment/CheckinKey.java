/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dsassigment;

import java.io.Serializable;
import java.security.Key;

/**
 *
 * @author TOSHIBA
 */
public class CheckinKey implements Serializable{

    
    private static final long serialVersionUID = 5497841316127816999L;
    
    String POI,POI_name;
    
    public CheckinKey( String POI,String POI_name)
    {
        this.POI = POI;
        this.POI_name = POI_name;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof CheckinKey)) {
            return false;
        }
        
        return POI.equals(((CheckinKey)obj).POI);
    }
    


    @Override
    public String toString() {
        return POI+" Name:"+POI_name;
    }

    @Override
    public int hashCode() {
        return POI.hashCode();
    }
    
    
    
    
    
}
