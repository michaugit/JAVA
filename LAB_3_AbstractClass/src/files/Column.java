package files;

import java.util.ArrayList;

public class Column{
    public Column(String name, Class type){
        this.name= new String(name);
        this.type= type; // mozna by zrobic walidacje wprowadzanego typu, tylko w tym przypadku nie ma to znaczenia
        this.data= new ArrayList();
        this.index=0;
    }

    public Column(Column copy){

    }
    protected String name;
    protected Class type;
    public ArrayList data;
    protected int index;
}
