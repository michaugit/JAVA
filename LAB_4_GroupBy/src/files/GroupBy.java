package files;

import java.text.ParseException;

public interface GroupBy {
    DataFrame max() throws IllegalAccessException, InstantiationException, CloneNotSupportedException, ParseException;
    DataFrame min() throws IllegalAccessException, InstantiationException, ParseException;
    DataFrame mean() throws IllegalAccessException, InstantiationException, ParseException, ThisIsNoIDException;
    DataFrame std();
    DataFrame sum();
    DataFrame var();
    DataFrame apply(Applyable fun);
}
