package files;

import java.util.LinkedList;

public class GroupDataFrame {
    GroupDataFrame(){
        data= new LinkedList<DataFrame>();
    }

    public void print(String idItWasGroupedBy){
        for(DataFrame df : data){
            System.out.println(df.get(idItWasGroupedBy).data.get(0).toString());
        }
    }

    LinkedList<DataFrame> data;
}
