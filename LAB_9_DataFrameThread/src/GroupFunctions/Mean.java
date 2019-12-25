package GroupFunctions;

import files.*;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Mean implements Applyable, Serializable {
    protected static final long serialVersionUID = 1112122200L;
    @Override
    public DataFrame apply(DataFrame df) throws InconsistentTypeException {

        DataFrame ret = new DataFrame();
        try {
            for (Column colDF : df.tab) { //przechodzenie po każdej kolumnie
                Value sum = colDF.type.newInstance();
                try {
                    if (colDF.type.equals(StringObject.class)) {
                        sum = colDF.data.get(0); //jesli srednia ze stringa zwraca pierwszą wartość
                        for (Value obj : colDF.data) {
                            if (!obj.eq(sum)) {  //i jest różna od kolejnej oznacza że to nie id
                                throw new ThisIsNoIDException();
                            }
                        }
                    } else if (colDF.type.equals(DateObject.class)) {
                        long totalSeconds = 0L;
                        Integer iter = 0;
                        for (Value obj : colDF.data) {
                            try {
                                totalSeconds += ((DateObject) obj).value.getTime() / 1000L;
                            } catch (Exception e) {
                                throw new InconsistentTypeException(colDF.name, iter);
                            }
                            iter++;
                        }
                        long averageSeconds = totalSeconds / colDF.data.size();
                        Date averageDate = new Date(averageSeconds * 1000L);
                        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        sum.create(dateFormat.format(averageDate));
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
                        IntegerObject size = new IntegerObject();
                        size.create(((Integer) colDF.data.size()).toString());
                        sum.div(size);
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
