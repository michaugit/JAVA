package files;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;


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
    DataFrame(String inputFile, String[] type, boolean header ) {
        try {
            // Open the file
            FileInputStream fstream = new FileInputStream(inputFile);
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
            String strLine;

            //Read File Line By Line
            String[] colName;
            if ((strLine = br.readLine()) != null) {
                if (header) {
                    colName = strLine.split(",");
                } else {
                    colName = new String[type.length];
                    Arrays.fill(colName, "default");
                }
                tab = new ArrayList<Column>();
                size = 0;
                int iter = 0;
                for (String str : colName) {
                    tab.add(new Column(str, type[iter]));
                    iter++;
                }
                if (!header) {
                    String[] row = strLine.split(",");
                    int itr = 0;
                    for (String s : row) {
                        switch((String) tab.get(itr).type) {
                            case "int":
                            case "Int":
                            case "integer":
                            case "Integer":
                                tab.get(itr).data.add(Integer.parseInt(s));
                                break;
                            case "String":
                            case "string":
                                tab.get(itr).data.add(s);
                                break;
                            case "double":
                            case "Double":
                                tab.get(itr).data.add(Double.parseDouble(s));
                                break;
                            default:
                                System.out.println("Unknown type while parsing! :(");
                                break;
                        }
                        itr++;
                    }
                }
            }

            while ((strLine = br.readLine()) != null) {
                String[] row = strLine.split(",");
                int itr = 0;
                for (String s : row) {
                    switch((String) tab.get(itr).type) {
                        case "int":
                        case "Int":
                        case "integer":
                        case "Integer":
                            tab.get(itr).data.add(Integer.parseInt(s));
                            break;
                        case "String":
                        case "string":
                            tab.get(itr).data.add(s);
                            break;
                        case "double":
                        case "Double":
                            tab.get(itr).data.add(Double.parseDouble(s));
                            break;
                        default:
                            System.out.println("Unknown type while parsing! :(");
                            break;
                    }
                    itr++;
                }
            }
            br.close();
        }
        catch(Exception e){
            System.out.println("something went wrong while reading the file!\n"+e.toString());
            System.exit(71830);

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

    protected void adjustSizeOfColumns(){
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
    protected int size;
}
