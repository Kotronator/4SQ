/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dsassigment;

/**
 *
 * @author tsipiripo
 */
public class CheckinQuestion
{
    Point[] boundPoints;
    TimeBounds tb;
    
    CheckinQuestion(Point[] boundPoints, TimeBounds tb)
    {
        this.boundPoints=boundPoints;
        this.tb=tb;
    }
    
    CheckinQuestion(Point[] boundPoints)
    {
        this(boundPoints , null);
    }
            
}
