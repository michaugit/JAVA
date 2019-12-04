package files;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

public class GroupDataFrame implements GroupBy {
    LinkedList<DataFrame> data;

    GroupDataFrame() {
        data = new LinkedList<DataFrame>();
    }

    public void print() {
        for (DataFrame df : data) {
            df.print();
        }
    }

    @Override
    public DataFrame max() throws InconsistentTypeException{
        DataFrame ret = new DataFrame();
        try {
            for (DataFrame df : data) { //przechodzenie po każdej grupie
                DataFrame retGroup = new DataFrame();
                for (Column colDF : df.tab) { //przechodzenie po każdej kolumnie
                    Value max;
                    max = (colDF.data.get(0));
                    Integer iter= 0;
                    for (Value obj : colDF.data) {//przechodzenie po danych  w kolumnie
                        try {
                            if (max.lte(obj)) {
                                max = obj;
                            }
                        }
                        catch(Exception e){
                            throw new InconsistentTypeException(colDF.name,iter);
                        }
                        iter++;
                    }
                    Value deepCopy = colDF.type.newInstance();
                    deepCopy.create(max.toString());
                    Column cln = new Column(colDF.name, colDF.type);
                    cln.data.add(deepCopy);
                    retGroup.tab.add(cln);
                }
                ret.addAnotherDF(retGroup);
            }
        }
        catch (Exception e) {
            if(e instanceof InconsistentTypeException){
                throw (InconsistentTypeException) e;
            }
                System.out.println(e.toString());
        }
        return ret;
    }

    @Override
    public DataFrame min() {
        DataFrame ret = new DataFrame();
        try {
            for (DataFrame df : data) { //przechodzenie po każdej grupie
                DataFrame retGroup = new DataFrame();
                for (Column colDF : df.tab) { //przechodzenie po każdej kolumnie
                    Value min;
                    min = (colDF.data.get(0));
                    for (Value obj : colDF.data) {//przechodzenie po danych  w kolumnie
                        if (min.gte(obj)) {
                            min = obj;
                        }
                    }
                    Value deepCopy = colDF.type.newInstance();
                    deepCopy.create(min.toString());
                    Column cln = new Column(colDF.name, colDF.type);
                    cln.data.add(deepCopy);
                    retGroup.tab.add(cln);
                }
                ret.addAnotherDF(retGroup);
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return ret;
    }

    @Override
    public DataFrame mean() {
        DataFrame ret = new DataFrame();
        try {
            for (DataFrame df : data) { //przechodzenie po każdej grupie
                DataFrame retGroup = new DataFrame();
                for (Column colDF : df.tab) { //przechodzenie po każdej kolumnie
                    Value sum = colDF.type.newInstance();
                    try {
                        if (colDF.type.equals(StringObject.class)) {
                            sum = colDF.data.get(0); //jesli srednia ze stringa zwraca pierwszą wartość
                            for (Value obj : colDF.data) {
                                if (!obj.eq(sum)) {
                                    throw new ThisIsNoIDException();
                                }
                            }
                        } else if (colDF.type.equals(DateObject.class)) {
                            long totalSeconds = 0L;
                            for (Value obj : colDF.data) {
                                totalSeconds += ((DateObject) obj).value.getTime() / 1000L;
                            }
                            long averageSeconds = totalSeconds / colDF.data.size();
                            Date averageDate = new Date(averageSeconds * 1000L);
                            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            sum.create(dateFormat.format(averageDate));
                        } else {
                            sum.create("0");
                            for (Value obj : colDF.data) {//przechodzenie po danych  w kolumnie
                                sum.add(obj);
                            }
                            IntegerObject size = new IntegerObject();
                            size.create(((Integer) colDF.data.size()).toString());
                            sum.div(size);
                        }

                        Value deepCopy = colDF.type.newInstance();
                        deepCopy.create(sum.toString());
                        Column cln = new Column(colDF.name, colDF.type);
                        cln.data.add(deepCopy);
                        retGroup.tab.add(cln);
                    } catch (ThisIsNoIDException e) {/*nie tworzy kolumny*/}
                }
                ret.addAnotherDF(retGroup);
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return ret;
    }

    @Override
    public DataFrame std() {
        DataFrame ret = new DataFrame();
        try {
            for (DataFrame df : data) { //przechodzenie po każdej grupie
                DataFrame retGroup = new DataFrame();
                for (Column colDF : df.tab) { //przechodzenie po każdej kolumnie
                    Value mean = colDF.type.newInstance();
                    Value std = colDF.type.newInstance();
                    try {
                        if (colDF.type.equals(StringObject.class) || colDF.type.equals(DateObject.class)) {
                            std = colDF.data.get(0); //jesli srednia ze stringa zwraca pierwszą wartość
                            for (Value obj : colDF.data) {
                                if (!obj.eq(std)) {
                                    throw new ThisIsNoIDException();
                                }
                            }
                        } else {
                            mean.create("0");
                            std.create("0");
                            for (Value obj : colDF.data) {//przechodzenie po danych  w kolumnie
                                mean.add(obj);
                            }
                            IntegerObject size = new IntegerObject();
                            size.create(((Integer) colDF.data.size()).toString());
                            mean.div(size);

                            for (Value obj : colDF.data) {//przechodzenie po danych  w kolumnie
                                Value x= colDF.type.newInstance();
                                x.create("0");
                                x.add(obj);
                                x.sub(mean);
                                IntegerObject powerTwo= new IntegerObject();
                                powerTwo.create("2");
                                x.pow(powerTwo);
                                std.add(x);
                            }
                            IntegerObject n = new IntegerObject();
                            n.create(((Integer) colDF.data.size()).toString());
                            std.div(n);
                            DoubleObject sqrtPower= new DoubleObject();
                            sqrtPower.create("0.5");
                            std.pow(sqrtPower);
                        }

                        Value deepCopy = colDF.type.newInstance();
                        deepCopy.create(std.toString());
                        Column cln = new Column(colDF.name, colDF.type);
                        cln.data.add(deepCopy);
                        retGroup.tab.add(cln);
                    } catch (ThisIsNoIDException e) {/*nie tworzy kolumny*/}
                }
                ret.addAnotherDF(retGroup);
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return ret;
    }

    @Override
    public DataFrame sum() {
        DataFrame ret = new DataFrame();
        try {
            for (DataFrame df : data) { //przechodzenie po każdej grupie
                DataFrame retGroup = new DataFrame();
                for (Column colDF : df.tab) { //przechodzenie po każdej kolumnie
                    Value sum = colDF.type.newInstance();
                    try {
                        if (colDF.type.equals(StringObject.class) || colDF.type.equals(DateObject.class)) {
                            sum = colDF.data.get(0); //jesli srednia ze stringa zwraca pierwszą wartość
                            for (Value obj : colDF.data) {
                                if (!obj.eq(sum)) {
                                    throw new ThisIsNoIDException();
                                }
                            }
                        } else {
                            sum.create("0");
                            for (Value obj : colDF.data) {//przechodzenie po danych  w kolumnie
                                sum.add(obj);
                            }
                        }

                        Value deepCopy = colDF.type.newInstance();
                        deepCopy.create(sum.toString());
                        Column cln = new Column(colDF.name, colDF.type);
                        cln.data.add(deepCopy);
                        retGroup.tab.add(cln);
                    } catch (ThisIsNoIDException e) {/*nie tworzy kolumny*/}
                }
                ret.addAnotherDF(retGroup);
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return ret;
    }

    @Override
    public DataFrame var() {
        DataFrame ret = new DataFrame();
        try {
            for (DataFrame df : data) { //przechodzenie po każdej grupie
                DataFrame retGroup = new DataFrame();
                for (Column colDF : df.tab) { //przechodzenie po każdej kolumnie
                    Value mean = colDF.type.newInstance();
                    Value var = colDF.type.newInstance();
                    try {
                        if (colDF.type.equals(StringObject.class) || colDF.type.equals(DateObject.class)) {
                            var = colDF.data.get(0); //jesli srednia ze stringa zwraca pierwszą wartość
                            for (Value obj : colDF.data) {
                                if (!obj.eq(var)) {
                                    throw new ThisIsNoIDException();
                                }
                            }
                        } else {
                            mean.create("0");
                            var.create("0");
                            for (Value obj : colDF.data) {//przechodzenie po danych  w kolumnie
                                mean.add(obj);
                            }
                            IntegerObject size = new IntegerObject();
                            size.create(((Integer) colDF.data.size()).toString());
                            mean.div(size);

                            for (Value obj : colDF.data) {//przechodzenie po danych  w kolumnie
                                Value x= colDF.type.newInstance();
                                x.create("0");
                                x.add(obj);
                                x.sub(mean);
                                IntegerObject powerTwo= new IntegerObject();
                                powerTwo.create("2");
                                x.pow(powerTwo);
                                var.add(x);
                            }
                            IntegerObject n = new IntegerObject();
                            n.create(((Integer) colDF.data.size()).toString());
                            var.div(n);
                        }

                        Value deepCopy = colDF.type.newInstance();
                        deepCopy.create(var.toString());
                        Column cln = new Column(colDF.name, colDF.type);
                        cln.data.add(deepCopy);
                        retGroup.tab.add(cln);
                    } catch (ThisIsNoIDException e) {/*nie tworzy kolumny*/}
                }
                ret.addAnotherDF(retGroup);
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return ret;
    }

    @Override
    public DataFrame apply(Applyable fun){
        DataFrame ret= new DataFrame();
        for(DataFrame df : this.data){
            ret.addAnotherDF(fun.apply(df));
        }
        return ret;
    }
}
