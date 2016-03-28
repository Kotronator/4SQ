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
public interface ReduceWorker extends Worker
{
    public void waitForMasterAck();
    public Map<Integer,Object> reduce(int key, Object val);
    public void sendResults(int key, Object val);
}
