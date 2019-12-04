package files;

public class SizeOfColumnsException extends Exception {
    String colNames[];
    public SizeOfColumnsException(String colName1, String colName2){
        colNames= new String[2];
        colNames[0]=colName1;
        colNames[1]=colName2;
    }

    public String toString(){
        return new String("Sizes of columns: " + colNames[0] + " and " +  colNames[1] + " are not equal!" );
    }
}
