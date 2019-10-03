package com.company;

import com.company.*;
public class Main {

    public static void main(String[] args) {
        DataFrame df =  new DataFrame(new String[] {"kolumna 1", "kolumna 2"}, new String []{"int","string"});



        df.get("kolumna 1").add(1);
        df.get("kolumna 1").add(2);
        df.get("kolumna 1").add(3);

        df.get("kolumna 1").forEach(name -> System.out.println(name));
        System.out.println("\n Number of rows: " + df.size());
    }
}
