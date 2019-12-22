package files;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.Serializable;

public class StringObject extends Value implements Serializable {
    protected static final long serialVersionUID = 1112122200L;
    String value;

    public StringObject clone() {
        StringObject copy =  new StringObject();
        copy.value= this.value;
        return copy;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public Value add(Value x) {
        value=new String(this.value + x.toString());
        return this;
    }

    @Override
    public Value sub(Value x) {
        throw new NotImplementedException();
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
        boolean ret=false;
        if(x instanceof StringObject){
            ret=value.equals(((StringObject) x).value);
        }
        else{
            throw new IllegalArgumentException();
        }
        return ret;
    }

    @Override
    public boolean lte(Value x) {
        boolean ret=false;
        if(x instanceof StringObject){
            ret=( value.length() <= ((StringObject) x).value.length());
        }
        else{
            throw new IllegalArgumentException();
        }
        return ret;
    }

    @Override
    public boolean gte(Value x) {
        boolean ret=false;
        if(x instanceof StringObject){
            ret=( value.length() >= ((StringObject) x).value.length());
        }
        else{
            throw new IllegalArgumentException();
        }
        return ret;
    }

    @Override
    public boolean neq(Value x) {
        return !this.eq(x);
    }

    @Override
    public boolean equals(Object other) {
        return value.equals((String) other);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public Value create(String s) {
        value= new String(s);
        return this;
    }
}
