package GroupFunctions;

import files.*;

import java.io.Serializable;

public class Min implements Applyable, Serializable {
    protected static final long serialVersionUID = 1112122200L;
    @Override
    public DataFrame apply(DataFrame df) throws InconsistentTypeException {
        DataFrame ret = new DataFrame();
        try {
            for (Column colDF : df.tab) { //przechodzenie po każdej kolumnie
                Value min;
                min = (colDF.data.get(0));
                Integer iter = 0;
                for (Value obj : colDF.data) {//przechodzenie po danych  w kolumnie
                    try {
                        if (min.gte(obj)) {
                            min = obj;
                        }
                    } catch (Exception e) {
                        throw new InconsistentTypeException(colDF.name, iter);
                    }
                    iter++;
                }
                Value deepCopy = colDF.type.newInstance();
                deepCopy.create(min.toString());
                Column cln = new Column(colDF.name, colDF.type);
                cln.data.add(deepCopy);
                ret.tab.add(cln);
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
