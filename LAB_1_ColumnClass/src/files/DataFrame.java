package files;

import java.util.ArrayList;


public class DataFrame {
    DataFrame(){
        tab = new ArrayList<Column>();
        size= 0;
    }

    DataFrame(String[] name, String[] type) {
        tab = new ArrayList<Column>();
        size= 0;
        int iter=0;
        for (String str : name) {
            tab.add(new Column(str, type[iter]));
            iter++;
        }
    }

    public int size(){
        if(!tab.isEmpty()){
            for(Column cln : tab){
                if(cln.data != null) {
                    if (size < cln.data.size()) {
                        size = cln.data.size();
                    }
                }
            }
        }
        return size;
    }

    public Column get(String colname){
        for (Column cln:tab) {
            if (cln.name.equals(colname)){
                return cln;
            }
        }
        return null;
    }

    public DataFrame get(String [] cols, boolean deepcopy){
        DataFrame back= new DataFrame();
        for( Column cln : tab){
            for( String str : cols){
                if(deepcopy) {
                    if (cln.name.equals(str)) {
                        Column columnToBack = new Column(cln.name, cln.type);
                        columnToBack.data = (ArrayList) cln.data.clone();
                        back.tab.add(columnToBack);
                    }
                }
                else{ //shallow copy
                    back=this;
                }
            }
        }
        return back;
    }

    public DataFrame iloc(int indexOfRow){
        this.adjustSizeOfColumns();
        DataFrame back= new DataFrame();
        for (Column cln: tab) {
            Column copy = new Column(cln.name, cln.type);
            copy.data.add(cln.data.get(indexOfRow));
            back.tab.add(copy);
        }
        return back;
    }
    public DataFrame iloc(int from, int to){
        this.adjustSizeOfColumns();
        DataFrame back= new DataFrame();
        for (Column cln: tab) {
            Column copy = new Column(cln.name, cln.type);
            for(int i=from; i<=to; i++) {
                copy.data.add(cln.data.get(i));
            }
            back.tab.add(copy);
        }
        return back;
    }

    private void adjustSizeOfColumns(){
        int size=this.size();
        for (Column cln : tab ) {
            while( cln.data.size()< size) {
                cln.data.add(null);
            }
        }

    }
    public void print(){
        System.out.println("\n\nPRINTED COLUMN:");
        for( Column cln : this.tab){
            System.out.println("Name of column: " + cln.name +
                    "\t\tType of column: " + cln.type +
                    "\t\tDane: " + cln.data.toString());
        }
        System.out.println("Number of rows: " + this.size());
    }


    public ArrayList<Column> tab;
    private int size;
}
