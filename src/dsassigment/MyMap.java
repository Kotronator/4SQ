/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dsassigment;

import dsassigment.CheckinKey;
import dsassigment.CheckinValue;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author tsipiripo
 */
public class MyMap extends HashMap<CheckinKey ,List<CheckinValue>> implements Serializable
{
    private static final long serialVersionUID = 5497841316127816666L;
    
    public MyMap addMap(Map<CheckinKey ,List<CheckinValue>> map)
    {
        for (Entry<CheckinKey, List<CheckinValue>> entrySet : map.entrySet())
        {
            CheckinKey key = entrySet.getKey();
            List<CheckinValue> value = entrySet.getValue();
            
            List<CheckinValue> list = this.get(key);
            if(list!=null)
            {
                list.addAll(value);
            }
            else 
                this.put(key, value);
            
        }
        
        return this;
    
    }
    
}
