package files;

import java.io.Serializable;

public class DoubleObject extends Value implements Serializable {
    protected static final long serialVersionUID = 1112122200L;

    public Double value;

    public DoubleObject clone(){
        DoubleObject copy = new DoubleObject();
        copy.value= this.value;
        return copy;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public Value add(Value x) {
        if (x instanceof DoubleObject) {
            value += ((DoubleObject) x).value;
        } else if (x instanceof IntegerObject) {
            value += ((IntegerObject) x).value.doubleValue();
        } else if (x instanceof FloatObject) {
            value += ((FloatObject) x).value.doubleValue();
        }
        else if(x instanceof COOValue){
            this.add(((COOValue) x).value);
        }
        else {
            throw new IllegalArgumentException();
        }
        return this;
    }

    @Override
    public Value sub(Value x) {
        if (x instanceof DoubleObject) {
            value -= ((DoubleObject) x).value;
        } else if (x instanceof IntegerObject) {
            value -= ((IntegerObject) x).value.doubleValue();
        } else if (x instanceof FloatObject) {
            value -= ((FloatObject) x).value.doubleValue();
        }
        else if(x instanceof COOValue){
            this.sub(((COOValue) x).value);
        }
        else {
            throw new IllegalArgumentException();
        }
        return this;
    }

    @Override
    public Value mul(Value x) {
        if (x instanceof DoubleObject) {
            value *= ((DoubleObject) x).value;
        } else if (x instanceof IntegerObject) {
            value *= ((IntegerObject) x).value.doubleValue();
        } else if (x instanceof FloatObject) {
            value *= ((FloatObject) x).value.doubleValue();
        }
        else if(x instanceof COOValue){
            this.mul(((COOValue) x).value);
        }
        else {
            throw new IllegalArgumentException();
        }
        return this;
    }

    @Override
    public Value div(Value x) {
        if (x instanceof DoubleObject) {
            value /= ((DoubleObject) x).value;
        } else if (x instanceof IntegerObject) {
            value /= ((IntegerObject) x).value.doubleValue();
        } else if (x instanceof FloatObject) {
            value /= ((FloatObject) x).value.doubleValue();
        }
        else if(x instanceof COOValue){
            this.div(((COOValue) x).value);
        }
        else {
            throw new IllegalArgumentException();
        }
        return this;
    }

    @Override
    public Value pow(Value x) {
        if (x instanceof DoubleObject) {
            value = (Math.pow(value, ((DoubleObject) x).value));
        } else if (x instanceof IntegerObject) {
            value = (Math.pow(value, ((IntegerObject) x).value));
        } else if (x instanceof FloatObject) {
            value =(Math.pow(value, ((FloatObject) x).value));
        }
        else if(x instanceof COOValue){
            this.pow(((COOValue) x).value);
        }
        else {
            throw new IllegalArgumentException();
        }
        return this;
    }

    @Override
    public boolean eq(Value x) {
        boolean ret = false;
        if (x instanceof DoubleObject) {
            ret = value.equals(((DoubleObject) x).value);
            ;
        } else if (x instanceof IntegerObject) {
            ret = value.equals(((IntegerObject) x).value.doubleValue());
        } else if (x instanceof FloatObject) {
            ret = value.equals(((FloatObject) x).value.doubleValue());
        }
        else if(x instanceof COOValue){
            this.eq(((COOValue) x).value);
        }
        else {
            throw new IllegalArgumentException();
        }
        return ret;
    }

    @Override
    public boolean lte(Value x) { //less than or equal
        boolean ret = false;
        if (x instanceof DoubleObject) {
            ret = (value <= ((DoubleObject) x).value);
        } else if (x instanceof IntegerObject) {
            ret = (value <= ((IntegerObject) x).value.doubleValue());
        } else if (x instanceof FloatObject) {
            ret = (value <= (((FloatObject) x).value.intValue()));
        }
        else if(x instanceof COOValue){
            this.lte(((COOValue) x).value);
        }
        else {
            throw new IllegalArgumentException();
        }
        return ret;
    }

    @Override
    public boolean gte(Value x) { //greater than or equal
        boolean ret = false;
        if (x instanceof DoubleObject) {
            ret = (value >= ((DoubleObject) x).value);
        } else if (x instanceof IntegerObject) {
            ret = (value >= ((IntegerObject) x).value.doubleValue());
        } else if (x instanceof FloatObject) {
            ret = (value >= (((FloatObject) x).value.doubleValue()));
        }
        else if(x instanceof COOValue){
            this.gte(((COOValue) x).value);
        }
        else {
            throw new IllegalArgumentException();
        }
        return ret;
    }

    @Override
    public boolean neq(Value x) {
        return !(this.eq(x));
    }

    @Override
    public boolean equals(Object other) {
        return value.equals((Double) other);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public Value create(String s) {
        value = Double.parseDouble(s);
        return this;
    }

}


