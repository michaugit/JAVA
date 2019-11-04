package files;

import jdk.nashorn.internal.runtime.arrays.ArrayLikeIterator;

import java.io.*;
import java.util.*;


public class DataFrame implements Cloneable {
    DataFrame() {
        tab = new ArrayList<Column>();
        size = 0;
    }

    DataFrame(String[] name, Class<? extends Value>[] type) {
        tab = new ArrayList<Column>();
        size = 0;
        int iter = 0;
        for (String str : name) {
            tab.add(new Column(str, type[iter]));
            iter++;
        }
    }

    //Konstruktory z header jako String- jeśli jest podany to oznacza że nie ma nagłówka,
    // jeśli go nie ma, ocznacza to że csv posiada nagłówek w pierwszym wierszu.
    DataFrame(String inputFile, Class<? extends Value>[] type, String[] headerName) {
        try {
            // Open the file
            FileInputStream fstream = new FileInputStream(inputFile);
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
            String strLine;

            tab = new ArrayList<Column>();
            size = 0;
            int iter = 0;
            for (String str : headerName) {
                tab.add(new Column(str, type[iter]));
                iter++;
            }

            while ((strLine = br.readLine()) != null) {
                String[] row = strLine.split(",");
                int itr = 0;
                for (String s : row) {
                    Value v1 = tab.get(itr).type.newInstance();
                    v1.create(s);
                    tab.get(itr).data.add(v1);
                    itr++;
                }
            }
            br.close();
        } catch (Exception e) {
            System.out.println("something went wrong while reading the file!\n" + e.toString());
            System.exit(71830);

        }
    }

    DataFrame(String inputFile, Class<? extends Value>[] type) {
        try {
            // Open the file
            FileInputStream fstream = new FileInputStream(inputFile);
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
            String strLine;

            //Read File Line By Line
            String[] colName;
            if ((strLine = br.readLine()) != null) {
                colName = strLine.split(",");
                tab = new ArrayList<Column>();
                size = 0;
                int iter = 0;
                for (String str : colName) {
                    tab.add(new Column(str, type[iter]));
                    iter++;
                }
            }

            while ((strLine = br.readLine()) != null) {
                String[] row = strLine.split(",");
                int itr = 0;
                for (String s : row) {
                    Value v1 = tab.get(itr).type.newInstance();
                    v1.create(s);
                    tab.get(itr).data.add(v1);
                    itr++;
                }
            }
            br.close();
        } catch (Exception e) {
            System.out.println("something went wrong while reading the file!\n" + e.toString());
            System.exit(71830);

        }
    }

    //Konstruktor z headerem jako boolean, gdy boolean false kolumny przyjmują nazwe "default".
    DataFrame(String inputFile, Class<? extends Value>[] type, boolean header) {
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
                        Value v1 = tab.get(itr).type.newInstance();
                        v1.create(s);
                        tab.get(itr).data.add(v1);
                        itr++;
                    }
                }
            }

            while ((strLine = br.readLine()) != null) {
                String[] row = strLine.split(",");
                int itr = 0;
                for (String s : row) {
                    Value v1 = tab.get(itr).type.newInstance();
                    v1.create(s);
                    tab.get(itr).data.add(v1);
                    itr++;
                }
            }
            br.close();
        } catch (Exception e) {
            System.out.println("something went wrong while reading the file!\n" + e.toString());
            System.exit(71830);

        }
    }

    public int size() {
        if (!tab.isEmpty()) {
            for (Column cln : tab) {
                if (cln.data != null) {
                    if (size < cln.data.size()) {
                        size = cln.data.size();
                    }
                }
            }
        }
        return size;
    }

    public Column get(String colname) {
        for (Column cln : tab) {
            if (cln.name.equals(colname)) {
                return cln;
            }
        }
        throw new RuntimeException("Column not found");
    }

    public DataFrame get(String[] cols, boolean deepcopy) {
        DataFrame back = new DataFrame();
        for (Column cln : tab) {
            for (String str : cols) {
                if (deepcopy) {
                    if (cln.name.equals(str)) {
                        Column columnToBack = new Column(cln.name, cln.type);
                        for (Value v : cln.data) {
                            columnToBack.data.add(v.clone());
                        }
                        back.tab.add(columnToBack);
                    }
                } else { //shallow copy
                    back = this;
                }
            }
        }
        return back;
    }

    public DataFrame iloc(int indexOfRow) {
        this.adjustSizeOfColumns();
        DataFrame back = new DataFrame();
        for (Column cln : tab) {
            Column copy = new Column(cln.name, cln.type);
            copy.data.add(cln.data.get(indexOfRow));
            back.tab.add(copy);
        }
        return back;
    }

    public DataFrame iloc(int from, int to) {
        this.adjustSizeOfColumns();
        DataFrame back = new DataFrame();
        for (Column cln : tab) {
            Column copy = new Column(cln.name, cln.type);
            for (int i = from; i <= to; i++) {
                copy.data.add(cln.data.get(i));
            }
            back.tab.add(copy);
        }
        return back;
    }

    public DataFrame addAnotherDF(DataFrame fromDF) {
        this.adjustSizeOfColumns();
        if (this.tab.size() != fromDF.tab.size() && !this.tab.isEmpty()) {
            throw new RuntimeException("Number of columns is not equal!");
        }
        if (this.tab.isEmpty()) {
            for (Column clnFromDF : fromDF.tab) {
                this.tab.add(new Column(clnFromDF.name, clnFromDF.type));
            }
        }
        for (Column cln : fromDF.tab) {
            this.get(cln.name).data.addAll(cln.data);
        }
        return this;
    }

    public GroupDataFrame groupBy(String[] keys) {
        LinkedList<DataFrame> grouped = new LinkedList<DataFrame>();
        LinkedList<DataFrame> groupedCopy = new LinkedList<DataFrame>();
        grouped.add(this);
        for(String key : keys) {
            groupedCopy= (LinkedList<DataFrame>) grouped.clone();
            grouped.clear();
            for(DataFrame df : groupedCopy) {
                Map<String, DataFrame> groupMap = new TreeMap<>();
                for (int i = 0; i < df.size(); i++) {
                    DataFrame row = df.iloc(i);
                    //sprawdzamy czy szukane ID w mapie istnieje, jeśli tak to dodajemy do tego Dataframe odczytany wiersz
                    if (groupMap.containsKey(row.get(key).data.get(0).toString())) {
                        groupMap.get(row.get(key).data.get(0).toString()).addAnotherDF(row);
                    }
                    //jeżeli nasza kolumna po której grupujemy nie ma żadnego id to wrzucamy je do DataFrame NoID
                    else if (row.get(key).data.get(0) == null) {
                        if (groupMap.get("NoID") == null) {
                            groupMap.put("NoID", row);
                        } else {
                            groupMap.get("NoID").addAnotherDF(row);
                        }
                    }
                    //jesli nie istnieje szukane ID dodajemy nowy element mapy z tym ID i pierwszym DataFrame
                    else if (!(groupMap.containsKey(row.get(key).data.get(0).toString()))) {
                        groupMap.put(row.get(key).data.get(0).toString(), row);
                    }
                }
                //po rozgrupowaniu DataFrame na mniejsze DataFrame'y zapisane w mapie przeniesienie do GroupDataFrame wpisując je do linkedlist
                for (Map.Entry<String, DataFrame> dfFromMap : groupMap.entrySet()) {
                    grouped.add(dfFromMap.getValue());
                }
            }

        }
        GroupDataFrame ret = new GroupDataFrame();
        ret.data=grouped;
        return ret;
    }


    protected void adjustSizeOfColumns() {
        int size = this.size();
        for (Column cln : tab) {
            while (cln.data.size() < size) {
                cln.data.add(null);
            }
        }

    }

    public void print() {
        this.adjustSizeOfColumns();
        System.out.println("\n\nPRINTED COLUMN:");
        for (Column cln : this.tab) {
            System.out.print("#" + cln.type.getSimpleName() + "#" + getSpacesToPrint(cln.type.getSimpleName() + "##"));
        }
        System.out.println();
        for (Column cln : this.tab) {
            System.out.print(cln.name + ":" + getSpacesToPrint(cln.name + ":"));
        }
        for (int i = 0; i < this.size(); ++i) {
            System.out.println();
            for (Column cln : this.tab) {
                System.out.print(cln.data.get(i).toString() + getSpacesToPrint(cln.data.get(i).toString()));
            }

        }
        System.out.println("\nNumber of rows: " + this.size());
    }

    private String getSpacesToPrint(String from) {
        Integer i = 25;
        String str = new String("                         "); //25 spacji
        if (from.length() > 25) {
            throw new RuntimeException("String to print is too long!");
        }
        return str.substring(0, i - from.length());
    }

    public DataFrame clone() {
        DataFrame newDF = new DataFrame();
        newDF.size = this.size;
        for (Column clnFromDF : tab) {
            newDF.tab.add(new Column(clnFromDF.name, clnFromDF.type));
            for (Value v : clnFromDF.data) {
                newDF.get(clnFromDF.name).data.add(v.clone());
            }
        }
        return newDF;
    }


    public ArrayList<Column> tab;
    protected Integer size;
}
