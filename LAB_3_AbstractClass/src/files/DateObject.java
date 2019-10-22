package files;


import org.omg.CORBA.DATA_CONVERSION;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

public class DateObject extends Value {
    Date value;
    @Override
    public String toString() {
        DateFormat dateFormat= new SimpleDateFormat("dd-MM-yyyy");
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
            value= new SimpleDateFormat("dd-MM-yyyy").parse(s);
        return this;
    }
}
