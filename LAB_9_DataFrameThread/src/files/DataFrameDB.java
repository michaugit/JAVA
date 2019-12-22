package files;

import javax.xml.crypto.Data;
import java.io.Serializable;
import java.sql.*;
import java.text.ParseException;
import java.util.*;

public class DataFrameDB implements Serializable {
    protected static final long serialVersionUID = 1112122200L;
    public Connection conn = null;
    public Statement stmt = null;
    public ResultSet rs = null;
    private String dbAdressUrl;
    private String username;
    private String password;

    public DataFrameDB(String dbAdressUrl, String username, String password) {
        this.dbAdressUrl = dbAdressUrl;
        this.username = username;
        this.password = password;
    }

    public void connect() {
        for (int i = 0; i < 3; i++) {
            try {
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                conn = DriverManager.getConnection("jdbc:mysql://" + dbAdressUrl,
                        username, password);
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

    public void freeUpResources() {
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

    public void exportDataFrameToMySQL(DataFrame dataframe, String tableName) {
        if (!dataframe.tab.isEmpty()) {
            try {
                connect();
                stmt = conn.createStatement();
                String sql = "CREATE TABLE IF NOT EXISTS " + tableName + " ( " + createStringHeaderSQL(dataframe) + ");";
//                        " PRIMARY KEY (" + tab.get(0).name + ", " + tab.get(1).name +  ", " + tab.get(2).name +  ", " + tab.get(3).name + "));";

                System.out.println(sql);
                stmt.executeUpdate(sql);

                ArrayList<Class<? extends Value>> numObj = new ArrayList<>();
                numObj.add(IntegerObject.class);
                numObj.add(DoubleObject.class);
                numObj.add(FloatObject.class);
                for (int i = 0; i < dataframe.size(); i++) {
                    String val = new String();
                    for (Column cln : dataframe.tab) {
                        if (!val.isEmpty()) {
                            val = new String(val + ", ");
                        }
                        if (numObj.contains(cln.type)) {
                            val = new String(val + " " + cln.data.get(i).toString());
                        } else {
                            val = new String(val + " '" + cln.data.get(i).toString() + "'");
                        }
                    }
                    stmt.executeUpdate("INSERT INTO " + tableName + "  VALUES (" + val + ")");
                }
                freeUpResources();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public DataFrame loadDataFrameFromMySQL(String tableName) {
        DataFrame dataframe = new DataFrame();
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
                        dataframe.tab.add(cln);
                        break;
                    case 2: //NUMERIC
                    case 8: //DOUBLE
                        cln = new Column(rsmd.getColumnName(i), DoubleObject.class);
                        dataframe.tab.add(cln);
                        break;
                    case 1: //CHAR
                    case 12: //VARCHAR
                        cln = new Column(rsmd.getColumnName(i), StringObject.class);
                        dataframe.tab.add(cln);
                        break;
                    case 91: //DATE
                        cln = new Column(rsmd.getColumnName(i), DateObject.class);
                        dataframe.tab.add(cln);
                        break;
                    default:
                        throw new RuntimeException("Something go wrong while parsing SQL to DF");
                }
            }
            while (rs.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    Value val = dataframe.tab.get(i - 1).type.newInstance();
                    val.create(rs.getString(i));
                    dataframe.tab.get(i - 1).data.add(val);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            freeUpResources();
        }
        return dataframe;
    }

    public DataFrame sqlQueryToDF(String selectQuery) {
        System.out.println(selectQuery); //########################################################################
        DataFrame df = new DataFrame();
        try {
            connect();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(selectQuery);
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                Column cln;
                switch (rsmd.getColumnType(i)) {
                    case -5: //BIGINT do COUNT
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
                    case 93: //timestamp
                        cln = new Column(rsmd.getColumnName(i), DateObject.class);
                        df.tab.add(cln);
                        break;
                    default:
                        throw new RuntimeException("Something go wrong while parsing SQL to DF, Type: " + rsmd.getColumnType(i));
                }
//                    -7 	BIT
//                        -6 	TINYINT
//                        -5 	BIGINT
//                        -4 	LONGVARBINARY
//                        -3 	VARBINARY
//                        -2 	BINARY
//                        -1 	LONGVARCHAR
//                    0 	NULL
//                    1 	CHAR
//                    2 	NUMERIC
//                    3 	DECIMAL
//                    4 	INTEGER
//                    5 	SMALLINT
//                    6 	FLOAT
//                    7 	REAL
//                    8 	DOUBLE
//                    12 	VARCHAR
//                    91 	DATE
//                    92 	TIME
//                    93 	TIMESTAMP
//                    1111  	OTHER
            }
            while (rs.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    Value val = df.tab.get(i - 1).type.newInstance();
                    val.create(rs.getString(i));
                    df.tab.get(i - 1).data.add(val);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            freeUpResources();
        }
        return df;
    }

    public String createStringHeaderSQL(DataFrame dataframe) {
        String header = new String();
        for (Column cln : dataframe.tab) {
            if (!header.isEmpty()) {
                header = new String(header + ", ");
            }
            if (cln.type.equals(StringObject.class)) {
                header = new String(header + cln.name + " VARCHAR(64) ");
            } else if (cln.type.equals(IntegerObject.class)) {
                header = new String(header + cln.name + " INT ");
            } else if (cln.type.equals(DateObject.class)) {
                header = new String(header + cln.name + " DATE ");
            } else if (cln.type.equals(FloatObject.class) || cln.type.equals(DoubleObject.class)) {
                header = new String(header + cln.name + " DOUBLE ");
            }
        }
        return header;
    }

    //DataFrame functions:

    public int size(String tableName) {
        int size = 0;
        try {
            connect();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT COUNT(*) FROM " + tableName);
            while (rs.next()) {
                size = Integer.parseInt(rs.getString(1));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            freeUpResources();
        }
        return size;
    }

    public Column get(String colname, String tableName) {
        DataFrame df = this.sqlQueryToDF("SELECT " + colname + " FROM " + tableName);
        return df.tab.get(0);
    }

    public DataFrame get(String[] cols, String tableName) {
        String colNames = new String();
        for (String str : cols) {
            if (colNames.isEmpty()) {
                colNames = str;
            } else {
                colNames = colNames + ", " + str;
            }
        }
        return this.sqlQueryToDF("SELECT " + colNames + " FROM " + tableName);
    }

    public DataFrame iloc(int indexOfRow, String tableName) {
        return this.sqlQueryToDF("SELECT * FROM " + tableName + " LIMIT " + (indexOfRow) + ", 1");
    }

    public DataFrame iloc(int from, int to, String tableName) {
        int howMany = (to - from);
        return this.sqlQueryToDF("SELECT * FROM " + tableName + " LIMIT " + (from) + ", " + howMany);
    }

//groupBy without class
//    public DataFrame groupBy(String tableName, String[] keys, String function, String[] colsToFunction) {
//        String keystr = new String();
//        for (String str : keys) {
//            if (keystr.isEmpty()) {
//                keystr = str;
//            } else {
//                keystr = keystr + ", " + str;
//            }
//        }
//        String colsWithFunction = new String();
//        for (String str : colsToFunction) {
//            colsWithFunction = colsWithFunction + ", " + function + "( " + str + " ) ";
//        }
//        return this.sqlQueryToDF("SELECT " + keystr + colsWithFunction + " FROM " + tableName + " GROUP BY " + keystr);
//    }

    public groupDB groupBy(String tableName, String[] keys) {
        return new groupDB(tableName, keys);
    }

    private HashMap<String, Integer> getColumnTypeMap(String tableName) {
        HashMap<String, Integer> clnANDtype = new HashMap<>();
        try {
            connect();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM " + tableName + " LIMIT 0;");
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                clnANDtype.put(rsmd.getColumnName(i), rsmd.getColumnType(i));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            freeUpResources();
        }
        return clnANDtype;
    }

    public class groupDB {
        private String[] keys;
        private String tableName;

        private groupDB(String tableName, String[] keys) {
            this.tableName = tableName;
            this.keys = keys;
        }

        public DataFrame max() {
            HashMap<String, Integer> clnANDtype = getColumnTypeMap(tableName);
            String keystr = new String();
            for (String str : keys) {
                if (keystr.isEmpty()) {
                    keystr = str;
                } else {
                    keystr = keystr + ", " + str;
                }
                clnANDtype.remove(str); //usuwanie kluczy z kolumn dla ktorych wywoluje sie funkcje
            }

            Set<String> keysFromMap = clnANDtype.keySet();
            String colsWithFunction = new String();
            for (String str : keysFromMap) {
                colsWithFunction = colsWithFunction + ", MAX( " + str + " ) ";
            }

            return sqlQueryToDF("SELECT " + keystr + colsWithFunction + " FROM " + tableName + " GROUP BY " + keystr);
        }

        public DataFrame min() {
            HashMap<String, Integer> clnANDtype = getColumnTypeMap(tableName);
            String keystr = new String();
            for (String str : keys) {
                if (keystr.isEmpty()) {
                    keystr = str;
                } else {
                    keystr = keystr + ", " + str;
                }
                clnANDtype.remove(str); //usuwanie kluczy z kolumn dla ktorych wywoluje sie funkcje
            }

            Set<String> keysFromMap = clnANDtype.keySet();
            String colsWithFunction = new String();
            for (String str : keysFromMap) {
                colsWithFunction = colsWithFunction + ", MIN( " + str + " ) ";
            }

            return sqlQueryToDF("SELECT " + keystr + colsWithFunction + " FROM " + tableName + " GROUP BY " + keystr);
        }

        public DataFrame mean() {
            HashMap<String, Integer> clnANDtype = getColumnTypeMap(tableName);
            {//delete chars and varchars from column to mean
                while (clnANDtype.values().remove(1));
                while (clnANDtype.values().remove(12));
            }
            String keystr = new String();
            for (String str : keys) {
                if (keystr.isEmpty()) {
                    keystr = str;
                } else {
                    keystr = keystr + ", " + str;
                }
                clnANDtype.remove(str); //usuwanie kluczy po których było grupowanie z kolumn dla ktorych wywoluje sie funkcje
            }

            Set<String> keysFromMap = clnANDtype.keySet();
            String colsWithFunction = new String();
            for (String str : keysFromMap) {
                if(clnANDtype.get(str)==91){ //DATE
                    //from_unixtime(avg(unix_timestamp(dt)))
                    colsWithFunction = colsWithFunction + ", (from_unixtime(avg(unix_timestamp(" + str + ")))) AS  \"avg( " + str + " )\"";
                }
                else{
                    colsWithFunction = colsWithFunction + ", AVG( " + str + " ) ";
                }
            }

            return sqlQueryToDF("SELECT " + keystr + colsWithFunction + " FROM " + tableName + " GROUP BY " + keystr);
        }

        public DataFrame std() {
            HashMap<String, Integer> clnANDtype = getColumnTypeMap(tableName);
            {//delete chars, varchars, time from column to std
                while (clnANDtype.values().remove(1));
                while (clnANDtype.values().remove(12));
                while (clnANDtype.values().remove(91));
            }
            String keystr = new String();
            for (String str : keys) {
                if (keystr.isEmpty()) {
                    keystr = str;
                } else {
                    keystr = keystr + ", " + str;
                }
                clnANDtype.remove(str); //usuwanie kluczy po których było grupowanie z kolumn dla ktorych wywoluje sie funkcje
            }

            Set<String> keysFromMap = clnANDtype.keySet();
            String colsWithFunction = new String();
            for (String str : keysFromMap) {
                    colsWithFunction = colsWithFunction + ", STD( " + str + " ) ";

            }

            return sqlQueryToDF("SELECT " + keystr + colsWithFunction + " FROM " + tableName + " GROUP BY " + keystr);
        }

        public DataFrame sum() {
            HashMap<String, Integer> clnANDtype = getColumnTypeMap(tableName);
            {//delete chars, varchars, time from column to sum
                while (clnANDtype.values().remove(1));
                while (clnANDtype.values().remove(12));
                while (clnANDtype.values().remove(91));
            }
            String keystr = new String();
            for (String str : keys) {
                if (keystr.isEmpty()) {
                    keystr = str;
                } else {
                    keystr = keystr + ", " + str;
                }
                clnANDtype.remove(str); //usuwanie kluczy po których było grupowanie z kolumn dla ktorych wywoluje sie funkcje
            }

            Set<String> keysFromMap = clnANDtype.keySet();
            String colsWithFunction = new String();
            for (String str : keysFromMap) {
                colsWithFunction = colsWithFunction + ", SUM( " + str + " ) ";

            }

            return sqlQueryToDF("SELECT " + keystr + colsWithFunction + " FROM " + tableName + " GROUP BY " + keystr);
        }

        public DataFrame var() {
            HashMap<String, Integer> clnANDtype = getColumnTypeMap(tableName);
            {//delete chars, varchars, time from column to std
                while (clnANDtype.values().remove(1));
                while (clnANDtype.values().remove(12));
                while (clnANDtype.values().remove(91));
            }
            String keystr = new String();
            for (String str : keys) {
                if (keystr.isEmpty()) {
                    keystr = str;
                } else {
                    keystr = keystr + ", " + str;
                }
                clnANDtype.remove(str); //usuwanie kluczy po których było grupowanie z kolumn dla ktorych wywoluje sie funkcje
            }

            Set<String> keysFromMap = clnANDtype.keySet();
            String colsWithFunction = new String();
            for (String str : keysFromMap) {
                colsWithFunction = colsWithFunction + ", VARIANCE( " + str + " ) ";

            }

            return sqlQueryToDF("SELECT " + keystr + colsWithFunction + " FROM " + tableName + " GROUP BY " + keystr);
        }

        public DataFrame applySQLfun(String function, String[] colsToFunction) {
            String keystr = new String();
            for (String str : keys) {
                if (keystr.isEmpty()) {
                    keystr = str;
                } else {
                    keystr = keystr + ", " + str;
                }
            }
            String colsWithFunction = new String();
            for (String str : colsToFunction) {
                colsWithFunction = colsWithFunction + ", " + function + "( " + str + " ) ";
            }
            return sqlQueryToDF("SELECT " + keystr + colsWithFunction + " FROM " + tableName + " GROUP BY " + keystr);
        }


    }
}
