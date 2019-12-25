package GroupFunctions;


import files.*;

public interface Applyable{
     public DataFrame apply(DataFrame group) throws InconsistentTypeException;
}
