package app;
import files.*;

import java.io.Serializable;

public class ServerRequestGDF implements Serializable {
    protected static final long serialVersionUID = 1112122200L;

    protected String function;
    protected GroupDataFrame groupedDF;

    public ServerRequestGDF(String fun, GroupDataFrame gDF){
        this.function=fun;
        this.groupedDF=gDF;
    }
    //getters
    String getFunction(){
        return function;
    }

    GroupDataFrame getGroupedDF(){
        return groupedDF;
    }
}
