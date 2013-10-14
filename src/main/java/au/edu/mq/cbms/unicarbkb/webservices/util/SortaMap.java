package au.edu.mq.cbms.unicarbkb.webservices.util;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;



public class SortaMap {

    Map<String,Double> doCaculation(Map<String,Double> aMap){
        ValueComparator bvc =  new ValueComparator(aMap);
        TreeMap<String,Double> sorted_map = new TreeMap<String,Double>(bvc);
        sorted_map.putAll(aMap);
        return sorted_map;
    }
}
class ValueComparator implements Comparator<String> {

    Map<String, Double> base;
    public ValueComparator(Map<String, Double> base) {
        this.base = base;
    }

    // Note: this comparator imposes orderings that are inconsistent with equals.    
    public int compare(String a, String b) {
        if (base.get(a) >= base.get(b)) {
            return -1;
        } else {
            return 1;
        } // returning 0 would merge keys
    }
}

