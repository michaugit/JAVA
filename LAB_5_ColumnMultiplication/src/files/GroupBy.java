package files;

import java.text.ParseException;

public interface GroupBy {
    DataFrame max() throws InconsistentTypeException, InstantiationException, ParseException, IllegalAccessException;
    DataFrame min();
    DataFrame mean();
    DataFrame std(); //odchylenie standardowe
    DataFrame sum();
    DataFrame var(); //wariancja
    DataFrame apply(Applyable fun);
}
