package files;

import java.io.Serializable;
import java.util.ArrayList;

public class Column implements Serializable {
    protected static final long serialVersionUID = 1112122200L;
    public Column(String name, Class type){
        this.name= new String(name);
        this.type= type; // mozna by zrobic walidacje wprowadzanego typu, tylko w tym przypadku nie ma to znaczenia
        this.data= new ArrayList<Value>();
        this.index=0;
    }

    public Column(Column copy){

    }
    public String name;
    public Class<Value> type;
    public ArrayList<Value> data;
    protected int index;
}
