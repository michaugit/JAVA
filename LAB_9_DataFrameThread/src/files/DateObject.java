package files;


import org.omg.CORBA.DATA_CONVERSION;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateObject extends Value implements Serializable {
    protected static final long serialVersionUID = 1112122200L;
    public Date value;

    @Override
    public DateObject clone() {
        DateObject copy = new DateObject();
        copy.value= (Date) this.value.clone();
        return copy;
    }

    @Override
    public String toString() {
        DateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(value);
    }

    @Override
    public Value add(Value x) {
        if(x instanceof IntegerObject){
            Calendar c= Calendar.getInstance();
            c.setTime(value);
            c.add(Calendar.DAY_OF_MONTH, ((IntegerObject) x).value);
            value= c.getTime();
        }
        else{
            throw new NotImplementedException();
        }
        return this;
    }

    @Override
    public Value sub(Value x) {
        if(x instanceof IntegerObject){
            Calendar c= Calendar.getInstance();
            c.setTime(value);
            c.add(Calendar.DAY_OF_MONTH, - ((IntegerObject) x).value);
            value= c.getTime();
        }
        return this;
    }

    @Override
    public Value mul(Value x) {
        throw new NotImplementedException();
    }

    @Override
    public Value div(Value x) {
        throw new NotImplementedException();
    }

    @Override
    public Value pow(Value x) {
        throw new NotImplementedException();
    }

    @Override
    public boolean eq(Value x) {
        if(!(x instanceof DateObject)){
            throw new NotImplementedException();
        }
        return value.equals(((DateObject) x).value);
    }

    @Override
    public boolean lte(Value x) {
        boolean ret=false;
        if(x instanceof  DateObject){
            if(value.compareTo(((DateObject) x).value)<=0){
                ret=true;
            }
        }
        else{
            throw new  IllegalArgumentException();
        }
        return ret;
    }

    @Override
    public boolean gte(Value x) {
        boolean ret=false;
        if(x instanceof  DateObject){
            if(value.compareTo(((DateObject) x).value)>=0){
                ret=true;
            }
        }
        else{
            throw new  IllegalArgumentException();
        }
        return ret;
    }

    @Override
    public boolean neq(Value x) {
        return !this.eq(x);
    }

    @Override
    public boolean equals(Object other) {
        throw new NotImplementedException();
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public Value create(String s) throws ParseException {
            value= new SimpleDateFormat("yyyy-MM-dd").parse(s);
        return this;
    }
}
