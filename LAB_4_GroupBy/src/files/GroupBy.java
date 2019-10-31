package files;

import java.text.ParseException;

public interface GroupBy {
    DataFrame max();
    DataFrame min();
    DataFrame mean();
    DataFrame std(); //odchylenie standardowe
    DataFrame sum();
    DataFrame var(); //wariancja
    DataFrame apply(Applyable fun);
}
