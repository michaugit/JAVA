package GroupFunctions;

import files.*;

import java.io.Serializable;

public class Var implements Applyable, Serializable {
    protected static final long serialVersionUID = 1112122200L;

    @Override
    public DataFrame apply(DataFrame df) throws InconsistentTypeException {
        DataFrame ret = new DataFrame();
        try {
            for (Column colDF : df.tab) { //przechodzenie po każdej kolumnie
                Value mean = colDF.type.newInstance();
                Value var = colDF.type.newInstance();
                try {
                    if (colDF.type.equals(StringObject.class) || colDF.type.equals(DateObject.class)) {
                        var = colDF.data.get(0);
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
                        Integer iter=0;

                        for (Value obj : colDF.data) {//przechodzenie po danych  w kolumnie
                            Value x = colDF.type.newInstance();
                            x.create("0");
                            try {
                                x.add(obj);
                                x.sub(mean);
                                IntegerObject powerTwo = new IntegerObject();
                                powerTwo.create("2");
                                x.pow(powerTwo);
                                var.add(x);
                            }catch(Exception e){
                                throw new InconsistentTypeException(colDF.name,iter);
                            }
                            iter++;
                        }
                        IntegerObject n = new IntegerObject();
                        n.create(((Integer) colDF.data.size()).toString());
                        var.div(n);
                    }

                    Value deepCopy = colDF.type.newInstance();
                    deepCopy.create(var.toString());
                    Column cln = new Column(colDF.name, colDF.type);
                    cln.data.add(deepCopy);
                    ret.tab.add(cln);
                } catch (ThisIsNoIDException e) {/*nie tworzy kolumny*/}
            }
        } catch (Exception e) {
            if (e instanceof InconsistentTypeException) {
                throw (InconsistentTypeException) e;
            }
            System.out.println(e.toString());
        }
        return ret;
    }
}
