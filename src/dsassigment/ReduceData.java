/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dsassigment;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 *
 * @author TOSHIBA
 */
public class ReduceData implements Serializable{
 
    private static final long serialVersionUID = 5627841316127816714L;
    
    List<Map<CheckinKey, List<CheckinValue>>> intermediate;
    String clientAddress;
    int expectedNumOfWorkers;
    int limiResults;

    public ReduceData(List<Map<CheckinKey, List<CheckinValue>>> intermediate, String clientAddress, int expectedNumOfWorkers, int limiResults) {
        this.intermediate = intermediate;
        this.clientAddress = clientAddress;
        this.expectedNumOfWorkers=expectedNumOfWorkers;
        this.limiResults=limiResults;
    }
    
    
}
