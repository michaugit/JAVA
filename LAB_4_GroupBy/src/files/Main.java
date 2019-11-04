package files;


import sun.util.calendar.BaseCalendar;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

public class Main {

    public static void main(String[] args) throws ParseException, CloneNotSupportedException, InstantiationException, IllegalAccessException {
//        IntegerObject i1= new IntegerObject();
//        i1.create("10");
//        IntegerObject i2= new IntegerObject();
//        i2.create("2");
//
//        DataFrame d1= new DataFrame(new String[]{"col1", "col2"}, new Class[]{IntegerObject.class, IntegerObject.class});
//        d1.tab.get(0).data.add(i1);
//        d1.tab.get(1).data.add(i2);
//        d1.print();
//
//
//        DataFrame d2= d1.get(new String[]{"col1", "col2"}, true);
//
//        d2.tab.get(0).data.get(0).add(i1);
//        d2.tab.get(1).data.get(0).add(i1);
//
//
//
//        d2.print();
//        d1.print();

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
//        DataFrame d1= new DataFrame(new String[]{"inter","outer"}, new Class[]{IntegerObject.class,IntegerObject.class});
//        DataFrame d2= new DataFrame(new String[]{"inter","outer"}, new Class[]{IntegerObject.class,IntegerObject.class});
//        IntegerObject i1= new IntegerObject();
//        IntegerObject i2= new IntegerObject();
//        IntegerObject i3= new IntegerObject();
//        IntegerObject i4= new IntegerObject();
//        i1.create("1");
//        i2.create("2");
//        i3.create("3");
//        i4.create("4");
//
//        d1.get("inter").data.add(i1);
//        d1.get("inter").data.add(i2);
//
//        d2.get("inter").data.add(i3);
//        d2.get("inter").data.add(i4);
//
//        d2.get("outer").data.add(i1);
//        d2.get("outer").data.add(i2);
//
//        d1.get("outer").data.add(i3);
//        d1.get("outer").data.add(i4);
//
//        d1.print();
//        d2.print();
//
//        d1.addAnotherDF(d2);
//        d1.print();

        DataFrame d1= new DataFrame("E:\\Java\\GIT\\LAB_4_GroupBy\\groupby.csv\\groupWithMe.csv", new Class[]{StringObject.class,DateObject.class,DoubleObject.class,DoubleObject.class});
        GroupDataFrame g1=d1.groupBy(new String[]{"id"});

        g1.max().print();
        g1.min().print();
        g1.sum().print();
        g1.apply(new Mediana()).print();
    }
}
