package files;


import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        DataFrame df = new DataFrame( new String [] {"kol 1", "kol 2"}, new String [] {"String", "Integer"});
        df.tab.get(0).data.add("ala");
        df.tab.get(0).data.add("ala ma");
        df.tab.get(0).data.add("ala ma kota");
        df.tab.get(0).data.add("ala myśli");
        df.tab.get(0).data.add("ala myśli że");
        df.tab.get(0).data.add("ala myśli że ma kota");

        df.tab.get(1).data.add(123);
        df.tab.get(1).data.add(124);
        df.tab.get(1).data.add(125);
        df.tab.get(1).data.add(126);
        df.tab.get(1).data.add(127);

        df.get("kol 1").data.add("siemanko");
        df.get("kol 2").data.add(14874853);


        DataFrame copy=df.get(new String [] {"kol 1"},true);
        copy.tab.get(0).data.add("trzydzieści sześć");

        DataFrame copyTwo= df.iloc(2,6);

        df.print();
        copy.print();
        copyTwo.print();

    }
}
