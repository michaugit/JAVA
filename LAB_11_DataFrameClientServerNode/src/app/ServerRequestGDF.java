package app;

import GroupFunctions.Applyable;
import files.*;

import java.io.Serializable;

public class ServerRequestGDF implements Serializable {
    protected static final long serialVersionUID = 1112122200L;

    protected Applyable function;
    protected GroupDataFrame groupedDF;

    public ServerRequestGDF(Applyable fun, GroupDataFrame gDF) {
        this.function = fun;
        this.groupedDF = gDF;
    }

    //getters
    public Applyable getFunction() {
        return function;
    }

    public GroupDataFrame getGroupedDF() {
        return groupedDF;
    }
}
