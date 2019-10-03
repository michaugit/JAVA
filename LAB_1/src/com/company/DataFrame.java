package com.company;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class DataFrame {
    DataFrame( String [] name, String [] type){
        data= new TreeMap<>();
        sizeOfDate=0;
        int iter=0;

        for ( String str : name) {
            switch(type[iter]){
                case "int":
                    data.put(str, new ArrayList<Integer>());
                    break;
                case "string":
                    data.put(str, new ArrayList<String>());
                    break;
                default:
                    System.out.println("Unknown type!");
                    break;
            }
            iter++;
        }

    }
    public final int size(){
        for(Map.Entry<String, ArrayList> pair : data.entrySet() ){
            if(sizeOfDate<pair.getValue().size()){
                sizeOfDate=pair.getValue().size();
            }
        }
        return sizeOfDate;
    }

    public ArrayList get(String colname){
        return data.get(colname);
    }

    public DataFrame iloc(int i){
        String [] columnNames= new String[data.size()];
        String [] columnTypes= new String[data.size()];
        int iter=0;
        for(Map.Entry<String, ArrayList> pair : data.entrySet() ){

            iter++;
        }

    }

    private Map< String , ArrayList> data;
    private int sizeOfDate;

}
