package app;

import GroupFunctions.Applyable;
import files.GroupDataFrame;

import java.io.Serializable;

public class NodeRequestGDF extends ServerRequestGDF implements Serializable {
    Integer clientID;
    public NodeRequestGDF(Applyable fun, GroupDataFrame gDF, Integer clientID) {
        super(fun, gDF);
        this.clientID=clientID;
    }
    public Integer getClientID(){
        return clientID;
    }
}
