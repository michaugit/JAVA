package files;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.Serializable;

public class COOValue extends Value implements Serializable {
    protected static final long serialVersionUID = 1112122200L;
    Integer index;
    Value value;

    private COOValue() {
    }

    COOValue(int index, Value value) {
        this.index = index;
        this.value = value;
    }

    public COOValue clone() {
        COOValue copy = new COOValue();
        copy.value = this.value.clone();
        copy.index = this.index;
        return copy;
    }

    @Override
    public String toString() {
        return new String("(" + index + "," + value.toString() + ") ");
    }

    @Override
    public Value add(Value x) {
        value.add(x);
        return this;
    }

    @Override
    public Value sub(Value x) {
        value.sub(x);
        return this;
    }

    @Override
    public Value mul(Value x) {
        value.mul(x);
        return this;
    }

    @Override
    public Value div(Value x) {
        value.div(x);
        return this;
    }

    @Override
    public Value pow(Value x) {
        value.pow(x);
        return this;
    }

    @Override
    public boolean eq(Value x) {
        return value.eq(x);
    }

    @Override
    public boolean lte(Value x) {
        return value.lte(x);
    }

    @Override
    public boolean gte(Value x) {
        return value.gte(x);
    }

    @Override
    public boolean neq(Value x) {
        return value.neq(x);
    }

    @Override
    public boolean equals(Object other) {
        return value.equals(other);
    }

    @Override
    public int hashCode() {
        return this.hashCode();
    }

    @Override
    public Value create(String s) {
        throw new NotImplementedException();
    }
}
