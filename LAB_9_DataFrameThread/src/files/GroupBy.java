package files;

import GroupFunctions.Applyable;

public interface GroupBy {
    DataFrame max() throws InconsistentTypeException;
    DataFrame min() throws InconsistentTypeException;
    DataFrame mean() throws InconsistentTypeException;
    DataFrame std() throws InconsistentTypeException; //odchylenie standardowe
    DataFrame sum() throws InconsistentTypeException;
    DataFrame var() throws InconsistentTypeException; //wariancja
    DataFrame apply(Applyable fun) throws InconsistentTypeException;
}
