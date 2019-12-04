package files;

public class DataFrameThread extends Thread{
    DataFrame ret;
    DataFrame DF;
    Applyable fun;
    public  DataFrameThread(DataFrame df, Applyable fun, DataFrame ret){
        this.DF=df;
        this.fun=fun;
        this.ret=ret;
    }
    @Override
    public void run() {
        DataFrame d=fun.apply(DF);
        synchronized (ret){
            ret.addAnotherDF(d);
        }
    }
}
