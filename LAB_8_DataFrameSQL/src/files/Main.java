package files;


import sun.util.calendar.BaseCalendar;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws SizeOfColumnsException, ParseException, IllegalAccessException, InconsistentTypeException, InstantiationException {
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

//        DataFrame d1= new DataFrame("E:\\Java\\GIT\\LAB_4_GroupBy\\groupby.csv\\groupWithMe.csv", new Class[]{StringObject.class,DateObject.class,DoubleObject.class,DoubleObject.class});
//        GroupDataFrame g1=d1.groupBy(new String[]{"id"});

//        g1.max().print();
//        g1.min().print();
//        g1.sum().print();
//        g1.apply(new Mediana()).print();


//        DataFrame d1 = new DataFrame("E:\\Java\\GIT\\LAB_5_ColumnMultiplication\\groupby.csv\\zad2.csv", new Class[]{StringObject.class, IntegerObject.class, DoubleObject.class, DateObject.class});
//
//        d1.print();
////
//        IntegerObject io1 = new IntegerObject();
//        io1.create("2");
//        DoubleObject do1 = new DoubleObject();
//        do1.create("2.25");
//        StringObject so1 = new StringObject();
//        so1.create(" XD");
//        DateObject date1= new DateObject();
//        date1.create("2016-06-23");
////
////
////        d1.addValueToCln("id", so1);
////        d1.powClnToValue("val1", io1);
////        d1.mulClnByValue("val2", do1);
////        d1.subValueFromCln("val3", io1);
//
//
//        d1.get("id").data.add(so1);
//        d1.get("val1").data.add(io1);
//        d1.get("val2").data.add(so1);
//        d1.get("val3").data.add(date1);
//
//        d1.print();
//
//        DataFrame df1 = d1.groupBy(new String[]{}).max();
//        df1.print();
//        StringObject s1= new StringObject();
//        s1.create("pole");
//        StringObject s2= new StringObject();
//        s2.create("poles");
//        IntegerObject i1= new IntegerObject();
//        i1.create("10");
//        DoubleObject d1= new DoubleObject();
//        d1.create("2.4545");

//        DataFrameDB df = new DataFrameDB(new String[]{"col1", "col2" , "col3"}, new Class[]{StringObject.class, IntegerObject.class, DoubleObject.class});
//        df.tab.get(0).data.add(s1);
//        df.tab.get(1).data.add(i1);
//        df.tab.get(2).data.add(d1);
//        df.tab.get(0).data.add(s2);
//        df.tab.get(1).data.add(i1);
//        df.tab.get(2).data.add(d1);
//        df.print();

//        DataFrame df= new DataFrame("C:\\Users\\resta\\Desktop\\group.csv", new Class[]{StringObject.class,DateObject.class,DoubleObject.class,DoubleObject.class});
//        df.exportDataFrameToMySQL("datafrejm");

        DataFrameDB df= new DataFrameDB("mysql.agh.edu.pl/mpieniad", "mpieniad", "W202QfWBGpyZGzWt");

        //DataFrame df2= df.iloc(0,3,"datafrejm");
        DataFrame df2= df.groupBy("datafrejm", new String[]{"id"}).applySQLfun( "variance", new String[]{"val","total"});
        DataFrame df3= df.groupBy("datafrejm", new String[]{"id"}).mean();


        System.out.println(df.size("datafrejm"));
        df2.print();
        df3.print();





        }
}
