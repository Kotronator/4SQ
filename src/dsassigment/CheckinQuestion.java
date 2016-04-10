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
public class CheckinQuestion implements Serializable
{
    private static final long serialVersionUID = 5497841316127816575L;
    
    Point[] boundPoints;
    TimeBounds tb;
    String clientAddress;
    
    public CheckinQuestion(Point[] boundPoints, TimeBounds tb)
    {
        this.boundPoints=boundPoints;
        this.tb=tb;
    }
    
    public CheckinQuestion(Point[] boundPoints)
    {
        this(boundPoints , null);
    }

    void setClientAddress(String hostAddress)
    {
        clientAddress=hostAddress;
    }
    
    
            
}
