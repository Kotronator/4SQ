/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dsassigment;

import java.security.Key;

/**
 *
 * @author TOSHIBA
 */
public class CheckinKey {

    String POI,POI_name;
    
    public CheckinKey( String POI,String POI_name)
    {
        this.POI = POI;
        this.POI_name = POI_name;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof CheckinKey)) {
            return false;
        }
        
        return POI.equals(((CheckinKey)o).POI);
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
