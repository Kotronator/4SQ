/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dsassigment;

import java.io.Serializable;
import java.sql.Date;
/**
 *
 * @author tsipiripo
 */
public class TimeBounds implements Serializable
{
     private static final long serialVersionUID = 5497841116127816575L;
    
    Date d1,d2;
    
    public TimeBounds(Date d1, Date d2)
    {
        if(d1.after(d2))
        {
            this.d2=d1;
            this.d1=d2;
        }else
        {
            this.d1=d1;
            this.d2=d2;
        }
        
    }
        
}
