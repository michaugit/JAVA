package GroupFunctions;

import GroupFunctions.Applyable;
import files.*;

import java.io.Serializable;
import java.util.Collections;

public class Mediana implements Applyable, Serializable {
    protected static final long serialVersionUID = 1112122200L;

    @Override
    public DataFrame apply(DataFrame group) {
        DataFrame ret = new DataFrame();
        DataFrame copy = group.clone();


        try {
            for (Column cln : copy.tab) {
                Collections.sort(cln.data, new CustomComparator());
                try {
                    Value mean = cln.type.newInstance();
                    if (cln.type.equals(StringObject.class) || cln.type.equals(DateObject.class)) {
                        mean = cln.data.get(0); //jesli srednia ze stringa zwraca pierwszą wartość
                        for (Value obj : cln.data) {
                            if (!obj.eq(mean)) {
                                throw new ThisIsNoIDException();
                            }
                        }
                    } else {
                        Integer sizeOfColumn = cln.data.size();
                        if (sizeOfColumn % 2 == 0) { //parzysty
                            Integer medPosition = cln.data.size() / 2;
                            // ze względu na numerowanie od 0, zamiast +1 to -1
                            Value x1 = cln.data.get(medPosition);
                            Value x2 = cln.data.get(medPosition - 1);
                            mean.create(x1.toString());
                            mean.add(x2);


                            IntegerObject divN = new IntegerObject();
                            divN.create("2");
                            mean.div(divN);
                        } else { //nieparzysty
                            mean.create(cln.data.get(sizeOfColumn/2).toString());
                        }
                    }
                    Column col = new Column(cln.name, cln.type);
                    col.data.add(mean);
                    ret.tab.add(col);
                } catch (ThisIsNoIDException e) {/*nie tworzy kolumny*/}
        }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return ret;
    }
}
