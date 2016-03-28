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
public interface AndroidClient
{
    public void distributeToMappers();
    public void waitForMappers();
    public void ackToReducers();
    public void collectDataFromReducers();
}
