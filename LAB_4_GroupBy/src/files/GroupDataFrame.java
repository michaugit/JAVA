package files;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

public class GroupDataFrame implements GroupBy {
    LinkedList<DataFrame> data;

    GroupDataFrame(){
        data= new LinkedList<DataFrame>();
    }

    public void print(String idItWasGroupedBy){
        for(DataFrame df : data){
            System.out.println(df.get(idItWasGroupedBy).data.get(0).toString());
        }
    }

    @Override
    public DataFrame max() throws IllegalAccessException, InstantiationException, ParseException {
        DataFrame ret= new DataFrame();
        for(DataFrame df : data){ //przechodzenie po każdej grupie
            DataFrame retGroup= new DataFrame();
            for( Column colDF : df.tab){ //przechodzenie po każdej kolumnie
                Value max;
                max=(colDF.data.get(0));
                for(Value obj: colDF.data){//przechodzenie po danych  w kolumnie
                    if(max.lte(obj)){
                        max=obj;
                    }
                }
                Value deepCopy=  colDF.type.newInstance();
                deepCopy.create(max.toString());
                Column cln= new Column(colDF.name, colDF.type);
                cln.data.add(deepCopy);
                retGroup.tab.add(cln);
            }
            ret.addAnotherDF(retGroup);
        }
        return ret;
    }

    @Override
    public DataFrame min() throws IllegalAccessException, InstantiationException, ParseException {
        DataFrame ret= new DataFrame();
        for(DataFrame df : data){ //przechodzenie po każdej grupie
            DataFrame retGroup= new DataFrame();
            for( Column colDF : df.tab){ //przechodzenie po każdej kolumnie
                Value max;
                max=(colDF.data.get(0));
                for(Value obj: colDF.data){//przechodzenie po danych  w kolumnie
                    if(max.gte(obj)){
                        max=obj;
                    }
                }
                Value deepCopy=  colDF.type.newInstance();
                deepCopy.create(max.toString());
                Column cln= new Column(colDF.name, colDF.type);
                cln.data.add(deepCopy);
                retGroup.tab.add(cln);
            }
            ret.addAnotherDF(retGroup);
        }
        return ret;
    }

    @Override
    public DataFrame mean() throws IllegalAccessException, InstantiationException, ParseException{
        DataFrame ret= new DataFrame();
        for(DataFrame df : data){ //przechodzenie po każdej grupie
            DataFrame retGroup= new DataFrame();
            for( Column colDF : df.tab) { //przechodzenie po każdej kolumnie
                Value val = colDF.type.newInstance();
                try {
                    if (colDF.type.equals(StringObject.class)) {
                        val = colDF.data.get(0); //jesli srednia ze stringa zwraca pierwszą wartość
                        for (Value obj : colDF.data) {
                            if (!obj.eq(val)) {
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
                        val.create(dateFormat.format(averageDate));
                    } else {
                        val.create("0");
                        for (Value obj : colDF.data) {//przechodzenie po danych  w kolumnie
                            val.add(obj);
                        }
                        IntegerObject size = new IntegerObject();
                        size.create(((Integer) colDF.data.size()).toString());
                        val.div(size);
                    }

                    Value deepCopy = colDF.type.newInstance();
                    deepCopy.create(val.toString());
                    Column cln = new Column(colDF.name, colDF.type);
                    cln.data.add(deepCopy);
                    retGroup.tab.add(cln);
                } catch (ThisIsNoIDException e) {/*nie tworzy kolumny*/}
            }
            ret.addAnotherDF(retGroup);
        }
        return ret;
    }

    @Override
    public DataFrame std() {
        return null;
    }

    @Override
    public DataFrame sum() {
        return null;
    }

    @Override
    public DataFrame var() {
        return null;
    }

    @Override
    public DataFrame apply(Applyable fun) {
        return null;
    }
}
