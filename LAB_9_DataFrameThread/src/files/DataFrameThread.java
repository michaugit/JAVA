package files;

import GroupFunctions.Applyable;

public class DataFrameThread implements Runnable {
    DataFrame ret;
    DataFrame DF;
    Applyable fun;

    public DataFrameThread(DataFrame df, Applyable fun, DataFrame ret) {
        this.DF = df;
        this.fun = fun;
        this.ret = ret;
    }

    @Override
    public void run() {
        DataFrame d = null;
        try {
            d = fun.apply(DF);
        } catch (InconsistentTypeException e) {
            e.printStackTrace();
        }
        synchronized (ret) {
            ret.addAnotherDF(d);
        }
    }
}
