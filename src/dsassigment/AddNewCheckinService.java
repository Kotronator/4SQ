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
public interface AddNewCheckinService
{
    public void intialize();
    public void waitForNewCheckinsThread();
    public void insertCheckinToDatabase(Object obj);
    public void ackToClient();
}
