/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dsassigment;

import dsassigment.CheckIn;
import dsassigment.CheckinKey;
import dsassigment.CheckinValue;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
    
    public HashMap<CheckinKey, List<CheckinValue>> collectCheckins(int limit)
    {
        if(limit==0)
            return (HashMap<CheckinKey, List<CheckinValue>>)checkInList.parallelStream().collect(Collectors.groupingBy(CheckIn::getCheckinKey,Collectors.mapping(CheckIn::getCheckinValue, Collectors.toList())));
        else
        {
            HashMap<CheckinKey, List<CheckinValue>> finalValue =(HashMap<CheckinKey, List<CheckinValue>>)checkInList.parallelStream().collect(Collectors.groupingBy(CheckIn::getCheckinKey,Collectors.mapping(CheckIn::getCheckinValue, Collectors.toList())));
             Comparator<List<CheckinValue>> listcmp = (List<CheckinValue> o1, List<CheckinValue> o2)->  o2.size()-o1.size();
        
            Comparator<Map.Entry<CheckinKey, List<CheckinValue>>> cmp = Map.Entry.<CheckinKey, List<CheckinValue>>comparingByValue(listcmp);
            //Map.Entry.<CheckinKey, List<CheckinValue>>comparingByValue(Comparator.reverseOrder()).thenComparing(Map.Entry.comparingByKey());
            LinkedHashMap<CheckinKey, List<CheckinValue>> linkedresult =finalValue.entrySet().stream().sorted(cmp).limit(limit).collect(
                    Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> {throw new IllegalStateException();}, LinkedHashMap::new)
            );
        
            return linkedresult;
        }
             
    }
}
