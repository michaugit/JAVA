package files;

public class IntegerObject extends Value {

    private Integer value;

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public Value add(Value x) {
        value+= ((IntegerObject) x).value;
        return this;
    }

    @Override
    public Value sub(Value x) {
        value-= ((IntegerObject) x).value;
        return this;
    }

    @Override
    public Value mul(Value x) {
        value*= ((IntegerObject) x).value;
        return this;
    }

    @Override
    public Value div(Value x) {
        value/= ((IntegerObject) x).value;
        return this;
    }

    @Override
    public Value pow(Value x) {
    value= (int) (Math.pow(value, ((IntegerObject) x).value));
        return this;
    }

    @Override
    public boolean eq(Value x) {
        return value.equals(((IntegerObject) x).value);
    }

    @Override
    public boolean lte(Value x) { //less than or equal
        return value <= ((IntegerObject) x).value;
    }

    @Override
    public boolean gte(Value x) { //greater than or equal
        return value >= ((IntegerObject) x).value;
    }

    @Override
    public boolean neq(Value x) {
        return !(value.equals(((IntegerObject) x).value));
    }

    @Override
    public boolean equals(Object other) {
        return value.equals((Integer) other);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public Value create(String s) {
        value= Integer.parseInt(s);
        return this;
    }

}
