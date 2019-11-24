package files;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

public class DataFrameDB extends DataFrame {
    public Connection conn = null;
    public Statement stmt = null;
    public ResultSet rs = null;

    public DataFrameDB() {
        super();
    }
    public DataFrameDB(String[] name, Class<? extends Value>[] type){
        super(name, type);
    }
    public DataFrameDB(String inputFile, Class<? extends Value>[] type, String[] headerName){
        super(inputFile, type, headerName);
    }
    public DataFrameDB(String inputFile, Class<? extends Value>[] type){
        super(inputFile,type);
    }
    public DataFrameDB(String inputFile, Class<? extends Value>[] type, boolean header) {
        super(inputFile,type,header);
    }

    public void connect() {
        for (int i = 0; i < 3; i++) {
            try {
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                conn = DriverManager.getConnection("jdbc:mysql://mysql.agh.edu.pl/mpieniad",
                        "mpieniad", "W202QfWBGpyZGzWt");
                break;
            } catch (SQLException ex) {
                // handle any errors
                System.out.println("SQLException: " + ex.getMessage());
                System.out.println("SQLState: " + ex.getSQLState());
                System.out.println("VendorError: " + ex.getErrorCode());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void freeUpResources(){
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException sqlEx) {
            } // ignore
            rs = null;
        }
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException sqlEx) {
            } // ignore

            stmt = null;
        }
    }

    public void exportDataFrameToMySQL(String tableName){
        if(!tab.isEmpty()) {
            try {
                connect();
                stmt = conn.createStatement();
                String sql = "CREATE TABLE IF NOT EXISTS "+ tableName + " ( " +createStringHeaderSQL() +
                        " PRIMARY KEY (" + tab.get(0).name + "));";
                System.out.println(sql);
                stmt.executeUpdate(sql);

                ArrayList<Class<? extends Value>> numObj = new ArrayList<>();
                numObj.add(IntegerObject.class);
                numObj.add(DoubleObject.class);
                numObj.add(FloatObject.class);
                for(int i=0; i<size(); i++ ) {
                    String val= new String();
                    for (Column cln : tab){
                        if(!val.isEmpty()){
                            val = new String( val + ", ");
                        }
                        if(numObj.contains(cln.type)){
                            val= new String (val + " " + cln.data.get(i).toString());
                        }
                        else{
                            val= new String (val + " '" + cln.data.get(i).toString() + "'");
                        }
                    }
                    stmt.executeUpdate("INSERT INTO "+ tableName + "  VALUES (" + val + ")");
                }
            freeUpResources();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void loadDataFrameFromMySQL(String tableName) {
        this.tab.clear();
        try {
            connect();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM " + tableName);
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();

            for (int i = 1; i <= columnCount; i++) {
                Column cln;
                switch (rsmd.getColumnType(i)) {
                    case 4: //INT
                        cln = new Column(rsmd.getColumnName(i), IntegerObject.class);
                        tab.add(cln);
                        break;
                    case 2: //NUMERIC
                    case 8: //DOUBLE
                        cln = new Column(rsmd.getColumnName(i), DoubleObject.class);
                        tab.add(cln);
                        break;
                    case 1: //CHAR
                    case 12: //VARCHAR
                        cln = new Column(rsmd.getColumnName(i), StringObject.class);
                        tab.add(cln);
                        break;
                    case 91: //DATE
                        cln = new Column(rsmd.getColumnName(i), DateObject.class);
                        tab.add(cln);
                        break;
                    default:
                        throw new RuntimeException("Something go wrong while parsing SQL to DF");
                }
            }
            while (rs.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    Value val= tab.get(i-1).type.newInstance();
                    val.create(rs.getString(i));
                    tab.get(i-1).data.add(val);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            freeUpResources();
        }
    }

    public DataFrame sqlQueryToDF(String selectQuery){
        DataFrame df= new DataFrame();
        try {
            connect();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(selectQuery);
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();

            for (int i = 1; i <= columnCount; i++) {
                Column cln;
                switch (rsmd.getColumnType(i)) {
                    case 4: //INT
                        cln = new Column(rsmd.getColumnName(i), IntegerObject.class);
                        df.tab.add(cln);
                        break;
                    case 2: //NUMERIC
                    case 8: //DOUBLE
                        cln = new Column(rsmd.getColumnName(i), DoubleObject.class);
                        df.tab.add(cln);
                        break;
                    case 1: //CHAR
                    case 12: //VARCHAR
                        cln = new Column(rsmd.getColumnName(i), StringObject.class);
                        df.tab.add(cln);
                        break;
                    case 91: //DATE
                        cln = new Column(rsmd.getColumnName(i), DateObject.class);
                        df.tab.add(cln);
                        break;
                    default:
                        throw new RuntimeException("Something go wrong while parsing SQL to DF");
                }
            }
            while (rs.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    Value val= tab.get(i-1).type.newInstance();
                    val.create(rs.getString(i));
                    df.tab.get(i-1).data.add(val);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            freeUpResources();
        }
        return df;
    }

    public String createStringHeaderSQL(){
        String header= new String();
        for(Column cln : tab){
            if(cln.type.equals(StringObject.class) ){
                header= new String( header + cln.name + " VARCHAR(64), ");
            }
            else if(cln.type.equals(IntegerObject.class)){
                header= new String( header +cln.name + " INT, ");
            }
            else if(cln.type.equals(DateObject.class)){
                header= new String( header +cln.name + " DATE, ");
            }
            else if(cln.type.equals(FloatObject.class) || cln.type.equals(DoubleObject.class)){
                header= new String( header +cln.name + " DOUBLE, ");
            }
        }
        return header;
    }

}
