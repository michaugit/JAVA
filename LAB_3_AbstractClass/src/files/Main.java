package files;


import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {


        IntegerObject i1= new IntegerObject();
        IntegerObject i2= new IntegerObject();
        i1.create("10");
        i2.create("2");
        System.out.println("i1: " + i1.toString());

        i1.add(i2);
        System.out.println("i1 = i1 + 2: "+ i1.toString());
        i1.sub(i2);
        System.out.println("i1 = i1 - 2: "+ i1.toString());
        i1.mul(i2);
        System.out.println("i1 = i1 * 2: "+ i1.toString());
        i1.div(i2);
        System.out.println("i1 = i1 / 2: "+ i1.toString());
        i1.pow(i2);
        System.out.println("i1 = i1 ^ 2: "+ i1.toString());
        System.out.println("i1 eq 2: "+ i1.eq(i2));
        System.out.println("i1 neq 2: "+ i1.neq(i2));
        System.out.println("i1 lte 2: "+ i1.lte(i2));
        System.out.println("i1 gte 2: "+ i1.gte(i2));
        System.out.println("i1 equals 25: "+ i1.equals(100));
        System.out.println("\nDziała :D");


        DataFrame d1= new DataFrame(new String[]{"inter"}, new Class[]{IntegerObject.class});
        d1.get("inter").data.add(i1);
        d1.get("inter").data.add(i2);
        Class type= IntegerObject.class;

        d1.print();
    }
}
