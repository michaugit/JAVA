package GroupFunctions;

import files.*;

import java.io.Serializable;

public class Sum implements Applyable, Serializable {
    protected static final long serialVersionUID = 1112122200L;

    @Override
    public DataFrame apply(DataFrame df) throws InconsistentTypeException {
        DataFrame ret = new DataFrame();
        try {
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
                        Integer iter = 0;
                        for (Value obj : colDF.data) {//przechodzenie po danych  w kolumnie
                            try {
                                sum.add(obj);
                            } catch (Exception e) {
                                throw new InconsistentTypeException(colDF.name, iter);
                            }
                            iter++;
                        }
                    }

                    Value deepCopy = colDF.type.newInstance();
                    deepCopy.create(sum.toString());
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
