package files;

import java.util.ArrayList;

public class Column{
    public Column(String name, String type){
        this.name= new String(name);
        this.type= new String(type); // mozna by zrobic walidacje wprowadzanego typu, tylko w tym przypadku nie ma to znaczenia
        this.data= new ArrayList();
    }

    public Column(Column copy){

    }
    protected String name;
    protected String type;
    protected ArrayList data;
}
