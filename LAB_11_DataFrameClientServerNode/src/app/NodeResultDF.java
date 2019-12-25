package app;

import files.DataFrame;

import java.io.Serializable;

public class NodeResultDF implements Serializable {
    protected static final long serialVersionUID = 1112122200L;

    DataFrame dataFrame;
    Integer clientID;

    public NodeResultDF(DataFrame dataFrame, Integer clientID){
        this.dataFrame=dataFrame;
        this.clientID=clientID;
    }
}
