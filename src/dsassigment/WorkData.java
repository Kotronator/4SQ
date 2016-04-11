/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dsassigment;

import java.io.Serializable;

/**
 *
 * @author tsipiripo
 */
public class WorkData implements Serializable
{
    private static final long serialVersionUID = 5497841316127816714L;
    
    CheckinQuestion chq;
    String reducerAddr;
    int numOfWorkers;


    public WorkData(CheckinQuestion chq, String reducerAddr, int numOfWorkers)
    {
        this.chq = chq;
        this.reducerAddr = reducerAddr;
        this.numOfWorkers=numOfWorkers;
    }
    
    
    
    
}
