package files;

import GroupFunctions.*;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class GroupDataFrame implements GroupBy, Serializable {
    protected static final long serialVersionUID = 1112122200L;
    protected LinkedList<DataFrame> data;
    static final int MAX_Threads= Runtime.getRuntime().availableProcessors();

    public GroupDataFrame() {
        data = new LinkedList<DataFrame>();
    }

    public void print() {
        for (DataFrame df : data) {
            df.print();
        }
    }
    public Integer getSize(){
        return data.size();
    }
    public LinkedList<DataFrame> getData(){
        return data;
    }


    @Override
    public DataFrame max() throws InconsistentTypeException {
        DataFrame ret = new DataFrame();
        Max maxfun= new Max();
        for (DataFrame df : data) { //przechodzenie po każdej grupie
            ret.addAnotherDF(maxfun.apply(df));
        }
        return ret;
    }
    public DataFrame maxWithThreads() {
        DataFrame ret = new DataFrame();
        Max maxfun= new Max();
        ArrayList<DataFrameThread> threadList = new ArrayList<>();
        for (DataFrame df : this.data) {
            DataFrameThread tmp = new DataFrameThread(df, maxfun, ret);
            threadList.add(tmp);
        }
        ExecutorService threadPool = Executors.newFixedThreadPool(MAX_Threads);
        for (DataFrameThread th : threadList) {
            threadPool.execute(th);
        }
        threadPool.shutdown();
        while(!threadPool.isTerminated()) {}
        return ret;
    }

    @Override
    public DataFrame min() throws InconsistentTypeException {
        DataFrame ret = new DataFrame();
        Min minfun= new Min();
        for (DataFrame df : data) { //przechodzenie po każdej grupie
            ret.addAnotherDF(minfun.apply(df));
        }
        return ret;
    }
    public DataFrame minWithThreads() {
        DataFrame ret = new DataFrame();
        Min minfun= new Min();
        ArrayList<DataFrameThread> threadList = new ArrayList<>();
        for (DataFrame df : this.data) {
            DataFrameThread tmp = new DataFrameThread(df, minfun, ret);
            threadList.add(tmp);
        }
        ExecutorService threadPool = Executors.newFixedThreadPool(MAX_Threads);
        for (DataFrameThread th : threadList) {
            threadPool.execute(th);
        }
        threadPool.shutdown();
        while(!threadPool.isTerminated()) {}
        return ret;
    }

    @Override
    public DataFrame mean() throws InconsistentTypeException {
        DataFrame ret = new DataFrame();
        Mean meanfun= new Mean();
        for (DataFrame df : data) { //przechodzenie po każdej grupie
            ret.addAnotherDF(meanfun.apply(df));
        }
        return ret;
    }
    public DataFrame meanWithThreads() {
        DataFrame ret = new DataFrame();
        Mean meanfun= new Mean();
        ArrayList<DataFrameThread> threadList = new ArrayList<>();
        for (DataFrame df : this.data) {
            DataFrameThread tmp = new DataFrameThread(df, meanfun, ret);
            threadList.add(tmp);
        }
        ExecutorService threadPool = Executors.newFixedThreadPool(MAX_Threads);
        for (DataFrameThread th : threadList) {
            threadPool.execute(th);
        }
        threadPool.shutdown();
        while(!threadPool.isTerminated()) {}
        return ret;
    }

    @Override
    public DataFrame std() throws InconsistentTypeException {
        DataFrame ret = new DataFrame();
        Std stdfun= new Std();
        for (DataFrame df : data) { //przechodzenie po każdej grupie
            ret.addAnotherDF(stdfun.apply(df));
        }
        return ret;
    }
    public DataFrame stdWithThreads() {
        DataFrame ret = new DataFrame();
        Std stdfun= new Std();
        ArrayList<DataFrameThread> threadList = new ArrayList<>();
        for (DataFrame df : this.data) {
            DataFrameThread tmp = new DataFrameThread(df, stdfun, ret);
            threadList.add(tmp);
        }
        ExecutorService threadPool = Executors.newFixedThreadPool(MAX_Threads);
        for (DataFrameThread th : threadList) {
            threadPool.execute(th);
        }
        threadPool.shutdown();
        while(!threadPool.isTerminated()) {}
        return ret;
    }

    @Override
    public DataFrame sum() throws InconsistentTypeException {
        DataFrame ret = new DataFrame();
        Sum sumfun= new Sum();
        for (DataFrame df : data) { //przechodzenie po każdej grupie
            ret.addAnotherDF(sumfun.apply(df));
        }
        return ret;
    }
    public DataFrame sumWithThreads() {
        DataFrame ret = new DataFrame();
        Sum sumfun= new Sum();
        ArrayList<DataFrameThread> threadList = new ArrayList<>();
        for (DataFrame df : this.data) {
            DataFrameThread tmp = new DataFrameThread(df, sumfun, ret);
            threadList.add(tmp);
        }
        ExecutorService threadPool = Executors.newFixedThreadPool(MAX_Threads);
        for (DataFrameThread th : threadList) {
            threadPool.execute(th);
        }
        threadPool.shutdown();
        while(!threadPool.isTerminated()) {}
        return ret;
    }

    @Override
    public DataFrame var() throws InconsistentTypeException {
        DataFrame ret = new DataFrame();
        Var varfun= new Var();
        for (DataFrame df : data) { //przechodzenie po każdej grupie
            ret.addAnotherDF(varfun.apply(df));
        }
        return ret;
    }
    public DataFrame varWithThreads() {
        DataFrame ret = new DataFrame();
        Var varfun= new Var();
        ArrayList<DataFrameThread> threadList = new ArrayList<>();
        for (DataFrame df : this.data) {
            DataFrameThread tmp = new DataFrameThread(df, varfun, ret);
            threadList.add(tmp);
        }
        ExecutorService threadPool = Executors.newFixedThreadPool(MAX_Threads);
        for (DataFrameThread th : threadList) {
            threadPool.execute(th);
        }
        threadPool.shutdown();
        while(!threadPool.isTerminated()) {}
        return ret;
    }

    @Override
    public DataFrame apply(Applyable fun) throws InconsistentTypeException {
        DataFrame ret = new DataFrame();
        for (DataFrame df : this.data) {
            ret.addAnotherDF(fun.apply(df));
        }
        return ret;
    }
    public DataFrame applyWithThreads(Applyable fun) {
//        DataFrame ret = new DataFrame();
//        ArrayList<DataFrameThread> threadList = new ArrayList<>();
//        for (DataFrame df : this.data) {
//            DataFrameThread tmp = new DataFrameThread(df, fun, ret);
//            threadList.add(tmp);
//        }
//        ExecutorService threadPool = Executors.newFixedThreadPool(MAX_Threads);
//        for (DataFrameThread th : threadList) {
//            threadPool.execute(th);
//        }
//        threadPool.shutdown();
//        while(!threadPool.isTerminated()) {}
//        return ret;

// other "type" of threadpool
        DataFrame ret = new DataFrame();
        List<Future<DataFrameThread>> futures = new ArrayList<Future<DataFrameThread>>();
        ExecutorService threadPool = Executors.newFixedThreadPool(MAX_Threads);
        for (DataFrame df : this.data) {
            DataFrameThread tmp = new DataFrameThread(df, fun, ret);
            futures.add(threadPool.submit(tmp, (DataFrameThread) null));
        }

        for (Future<DataFrameThread> future : futures) {
//            // this joins with the submitted job
            try {
                future.get();
            }
            catch (ExecutionException | InterruptedException e){
                e.printStackTrace();
            }
        }
        threadPool.shutdown();
        return ret;
    }
}


/** old version of max function before exporting all functions to GroupFunctions package with inheritance after Applyable's interface*/
//    @Override
//    public DataFrame max() throws InconsistentTypeException {
//        DataFrame ret = new DataFrame();
//        try {
//            for (DataFrame df : data) { //przechodzenie po każdej grupie
//                DataFrame retGroup = new DataFrame();
//                for (Column colDF : df.tab) { //przechodzenie po każdej kolumnie
//                    Value max;
//                    max = (colDF.data.get(0));
//                    Integer iter = 0;
//                    for (Value obj : colDF.data) {//przechodzenie po danych  w kolumnie
//                        try {
//                            if (max.lte(obj)) {
//                                max = obj;
//                            }
//                        } catch (Exception e) {
//                            throw new InconsistentTypeException(colDF.name, iter);
//                        }
//                        iter++;
//                    }
//                    Value deepCopy = colDF.type.newInstance();
//                    deepCopy.create(max.toString());
//                    Column cln = new Column(colDF.name, colDF.type);
//                    cln.data.add(deepCopy);
//                    retGroup.tab.add(cln);
//                }
//                ret.addAnotherDF(retGroup);
//            }
//        } catch (Exception e) {
//            if (e instanceof InconsistentTypeException) {
//                throw (InconsistentTypeException) e;
//            }
//            System.out.println(e.toString());
//        }
//        return ret;
//    }

/** old version of min function before exporting all functions to GroupFunctions package with inheritance after Applyable's interface*/
//    @Override
//    public DataFrame min() throws InconsistentTypeException {
//        DataFrame ret = new DataFrame();
//        try {
//            for (DataFrame df : data) {  //przechodzenie po każdej grupie
//                DataFrame retGroup = new DataFrame();
//                for (Column colDF : df.tab) { //przechodzenie po każdej kolumnie
//                    Value min;
//                    min = (colDF.data.get(0));
//                    Integer iter=0;
//                    for (Value obj : colDF.data) {//przechodzenie po danych  w kolumnie
//                        try {
//                            if (min.gte(obj)) {
//                                min = obj;
//                            }
//                        } catch (Exception e) {
//                            throw new InconsistentTypeException(colDF.name, iter);
//                        }
//                        iter++;
//                    }
//                    Value deepCopy = colDF.type.newInstance();
//                    deepCopy.create(min.toString());
//                    Column cln = new Column(colDF.name, colDF.type);
//                    cln.data.add(deepCopy);
//                    retGroup.tab.add(cln);
//                }
//                ret.addAnotherDF(retGroup);
//            }
//        } catch (Exception e) {
//            if (e instanceof InconsistentTypeException) {
//                throw (InconsistentTypeException) e;
//            }
//            System.out.println(e.toString());
//        }
//        return ret;
//    }

/** old version of mean function before exporting all functions to GroupFunctions package with inheritance after Applyable's interface*/
//    @Override
//    public DataFrame mean() {
//        DataFrame ret = new DataFrame();
//        try {
//            for (DataFrame df : data) { //przechodzenie po każdej grupie
//                DataFrame retGroup = new DataFrame();
//                for (Column colDF : df.tab) { //przechodzenie po każdej kolumnie
//                    Value sum = colDF.type.newInstance();
//                    try {
//                        if (colDF.type.equals(StringObject.class)) {
//                            sum = colDF.data.get(0); //jesli srednia ze stringa zwraca pierwszą wartość
//                            for (Value obj : colDF.data) {
//                                if (!obj.eq(sum)) {
//                                    throw new ThisIsNoIDException();
//                                }
//                            }
//                        } else if (colDF.type.equals(DateObject.class)) {
//                            long totalSeconds = 0L;
//                            for (Value obj : colDF.data) {
//                                totalSeconds += ((DateObject) obj).value.getTime() / 1000L;
//                            }
//                            long averageSeconds = totalSeconds / colDF.data.size();
//                            Date averageDate = new Date(averageSeconds * 1000L);
//                            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//                            sum.create(dateFormat.format(averageDate));
//                        } else {
//                            sum.create("0");
//                            for (Value obj : colDF.data) {//przechodzenie po danych  w kolumnie
//                                sum.add(obj);
//                            }
//                            IntegerObject size = new IntegerObject();
//                            size.create(((Integer) colDF.data.size()).toString());
//                            sum.div(size);
//                        }
//
//                        Value deepCopy = colDF.type.newInstance();
//                        deepCopy.create(sum.toString());
//                        Column cln = new Column(colDF.name, colDF.type);
//                        cln.data.add(deepCopy);
//                        retGroup.tab.add(cln);
//                    } catch (ThisIsNoIDException e) {/*nie tworzy kolumny*/}
//                }
//                ret.addAnotherDF(retGroup);
//            }
//        } catch (Exception e) {
//            System.out.println(e.toString());
//        }
//        return ret;
//    }

/** old version of std function before exporting all functions to GroupFunctions package with inheritance after Applyable's interface*/
//    @Override
//    public DataFrame std() {
//        DataFrame ret = new DataFrame();
//        try {
//            for (DataFrame df : data) { //przechodzenie po każdej grupie
//                DataFrame retGroup = new DataFrame();
//                for (Column colDF : df.tab) { //przechodzenie po każdej kolumnie
//                    Value mean = colDF.type.newInstance();
//                    Value std = colDF.type.newInstance();
//                    try {
//                        if (colDF.type.equals(StringObject.class) || colDF.type.equals(DateObject.class)) {
//                            std = colDF.data.get(0); //jesli srednia ze stringa zwraca pierwszą wartość
//                            for (Value obj : colDF.data) {
//                                if (!obj.eq(std)) {
//                                    throw new ThisIsNoIDException();
//                                }
//                            }
//                        } else {
//                            mean.create("0");
//                            std.create("0");
//                            for (Value obj : colDF.data) {//przechodzenie po danych  w kolumnie
//                                mean.add(obj);
//                            }
//                            IntegerObject size = new IntegerObject();
//                            size.create(((Integer) colDF.data.size()).toString());
//                            mean.div(size);
//
//                            for (Value obj : colDF.data) {//przechodzenie po danych  w kolumnie
//                                Value x = colDF.type.newInstance();
//                                x.create("0");
//                                x.add(obj);
//                                x.sub(mean);
//                                IntegerObject powerTwo = new IntegerObject();
//                                powerTwo.create("2");
//                                x.pow(powerTwo);
//                                std.add(x);
//                            }
//                            IntegerObject n = new IntegerObject();
//                            n.create(((Integer) colDF.data.size()).toString());
//                            std.div(n);
//                            DoubleObject sqrtPower = new DoubleObject();
//                            sqrtPower.create("0.5");
//                            std.pow(sqrtPower);
//                        }
//
//                        Value deepCopy = colDF.type.newInstance();
//                        deepCopy.create(std.toString());
//                        Column cln = new Column(colDF.name, colDF.type);
//                        cln.data.add(deepCopy);
//                        retGroup.tab.add(cln);
//                    } catch (ThisIsNoIDException e) {/*nie tworzy kolumny*/}
//                }
//                ret.addAnotherDF(retGroup);
//            }
//        } catch (Exception e) {
//            System.out.println(e.toString());
//        }
//        return ret;
//    }

/** old version of sum function before exporting all functions to GroupFunctions package with inheritance after Applyable's interface*/
//    @Override
//    public DataFrame sum() {
//        DataFrame ret = new DataFrame();
//        try {
//            for (DataFrame df : data) { //przechodzenie po każdej grupie
//                DataFrame retGroup = new DataFrame();
//                for (Column colDF : df.tab) { //przechodzenie po każdej kolumnie
//                    Value sum = colDF.type.newInstance();
//                    try {
//                        if (colDF.type.equals(StringObject.class) || colDF.type.equals(DateObject.class)) {
//                            sum = colDF.data.get(0); //jesli srednia ze stringa zwraca pierwszą wartość
//                            for (Value obj : colDF.data) {
//                                if (!obj.eq(sum)) {
//                                    throw new ThisIsNoIDException();
//                                }
//                            }
//                        } else {
//                            sum.create("0");
//                            for (Value obj : colDF.data) {//przechodzenie po danych  w kolumnie
//                                sum.add(obj);
//                            }
//                        }
//
//                        Value deepCopy = colDF.type.newInstance();
//                        deepCopy.create(sum.toString());
//                        Column cln = new Column(colDF.name, colDF.type);
//                        cln.data.add(deepCopy);
//                        retGroup.tab.add(cln);
//                    } catch (ThisIsNoIDException e) {/*nie tworzy kolumny*/}
//                }
//                ret.addAnotherDF(retGroup);
//            }
//        } catch (Exception e) {
//            System.out.println(e.toString());
//        }
//        return ret;
//    }

/** old version of var function before exporting all functions to GroupFunctions package with inheritance after Applyable's interface*/
//    @Override
//    public DataFrame var() {
//        DataFrame ret = new DataFrame();
//        try {
//            for (DataFrame df : data) { //przechodzenie po każdej grupie
//                DataFrame retGroup = new DataFrame();
//                for (Column colDF : df.tab) { //przechodzenie po każdej kolumnie
//                    Value mean = colDF.type.newInstance();
//                    Value var = colDF.type.newInstance();
//                    try {
//                        if (colDF.type.equals(StringObject.class) || colDF.type.equals(DateObject.class)) {
//                            var = colDF.data.get(0); //jesli srednia ze stringa zwraca pierwszą wartość
//                            for (Value obj : colDF.data) {
//                                if (!obj.eq(var)) {
//                                    throw new ThisIsNoIDException();
//                                }
//                            }
//                        } else {
//                            mean.create("0");
//                            var.create("0");
//                            for (Value obj : colDF.data) {//przechodzenie po danych  w kolumnie
//                                mean.add(obj);
//                            }
//                            IntegerObject size = new IntegerObject();
//                            size.create(((Integer) colDF.data.size()).toString());
//                            mean.div(size);
//
//                            for (Value obj : colDF.data) {//przechodzenie po danych  w kolumnie
//                                Value x = colDF.type.newInstance();
//                                x.create("0");
//                                x.add(obj);
//                                x.sub(mean);
//                                IntegerObject powerTwo = new IntegerObject();
//                                powerTwo.create("2");
//                                x.pow(powerTwo);
//                                var.add(x);
//                            }
//                            IntegerObject n = new IntegerObject();
//                            n.create(((Integer) colDF.data.size()).toString());
//                            var.div(n);
//                        }
//
//                        Value deepCopy = colDF.type.newInstance();
//                        deepCopy.create(var.toString());
//                        Column cln = new Column(colDF.name, colDF.type);
//                        cln.data.add(deepCopy);
//                        retGroup.tab.add(cln);
//                    } catch (ThisIsNoIDException e) {/*nie tworzy kolumny*/}
//                }
//                ret.addAnotherDF(retGroup);
//            }
//        } catch (Exception e) {
//            System.out.println(e.toString());
//        }
//        return ret;
//    }
//
