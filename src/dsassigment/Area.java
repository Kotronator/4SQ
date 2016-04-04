/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dsassigment;

import dsassigment.CheckIn;
import dsassigment.CheckinKey;
import dsassigment.CheckinValue;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @author tsipiripo
 */
public class Area
{
    List<CheckIn> checkInList;
    
    public Area(List<CheckIn> checkInList)
    {
        this.checkInList=checkInList;
    }
    
    public HashMap<CheckinKey, List<CheckinValue>> collectCheckins()
    {
        return (HashMap<CheckinKey, List<CheckinValue>>)checkInList.parallelStream().collect(Collectors.groupingBy(CheckIn::getCheckinKey,Collectors.mapping(CheckIn::getCheckinValue, Collectors.toList())));
    }
}
