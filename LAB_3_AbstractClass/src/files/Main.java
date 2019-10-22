package files;


import sun.util.calendar.BaseCalendar;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

public class Main {

    public static void main(String[] args) throws ParseException {
//        IntegerObject i1= new IntegerObject();
//        IntegerObject i2= new IntegerObject();
//        i1.create("10");
//        i2.create("2");
//        System.out.println("i1: " + i1.toString());
//
//        i1.add(i2);
//        System.out.println("i1 = i1 + 2: "+ i1.toString());
//        i1.sub(i2);
//        System.out.println("i1 = i1 - 2: "+ i1.toString());
//        i1.mul(i2);
//        System.out.println("i1 = i1 * 2: "+ i1.toString());
//        i1.div(i2);
//        System.out.println("i1 = i1 / 2: "+ i1.toString());
//        i1.pow(i2);
//        System.out.println("i1 = i1 ^ 2: "+ i1.toString());
//        System.out.println("i1 eq 2: "+ i1.eq(i2));
//        System.out.println("i1 neq 2: "+ i1.neq(i2));
//        System.out.println("i1 lte 2: "+ i1.lte(i2));
//        System.out.println("i1 gte 2: "+ i1.gte(i2));
//        System.out.println("i1 equals 25: "+ i1.equals(100));
//        System.out.println("\nDziała :D");
//        DataFrame d1= new DataFrame(new String[]{"inter"}, new Class[]{IntegerObject.class});
//        d1.get("inter").data.add(i1);
//        d1.get("inter").data.add(i2);
//        Class type= IntegerObject.class;
//        d1.print();
//        IntegerObject iob= new IntegerObject();
//        iob.create("25");
//        FloatObject dob= new FloatObject();
//        dob.create("25.2");
//        StringObject s1= new StringObject();
//        s1.create("Stoo lat ");
//        s1.add(dob);
//        iob.add(dob);
//
//
//        DataFrame dfFile= new DataFrame("E:\\Java\\GIT\\LAB_2_SparseDataFrame\\sparse.csv", new Class[]{DoubleObject.class,DoubleObject.class,DoubleObject.class});
//        DoubleObject hide = new DoubleObject();
//        hide.create("0.0");
//        SparseDataFrame sdfFile= new SparseDataFrame("E:\\Java\\GIT\\LAB_2_SparseDataFrame\\sparse.csv", new Class[]{DoubleObject.class,DoubleObject.class,DoubleObject.class}, hide);
//
//
//        sdfFile.print();
//        dfFile.print();
//        DataFrame dfFromSdf=sdfFile.toDense();
//        dfFromSdf.print();
//
        DateObject d1=new DateObject();
        d1.create("2019-09-29");
        DateObject d2=new DateObject();
        d2.create("2019-09-29");

        System.out.println(d1.toString()+"\n Equal: "+ d1.eq(d2));
        IntegerObject in= new IntegerObject();
        in.create("23");
        d1.add(in);
        System.out.println(d1.toString()+"\n Greater: "+ d1.gte(d2));
        d1.sub(in);
        d1.sub(in);
        System.out.println(d1.toString()+"\n Less: "+ d1.lte(d2));



    }
}
