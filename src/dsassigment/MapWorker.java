/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dsassigment;

import java.util.Map;

/**
 *
 * @author tsipiripo
 */
public interface MapWorker extends Worker
{
    public Map<Integer,Object> map(Object key , Object val);
    public void notifyMaster();
    public void sentToReducers(Map<Integer,Object> intermediate);
    
}
