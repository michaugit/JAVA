package files;

public class InconsistentTypeException extends Exception {
    String colName;
    Integer rowNumber;

    public InconsistentTypeException(String colName, Integer rowNumber){
        this.colName=colName;
        this.rowNumber=rowNumber;
    }

    public String toString(){
        return new String("Inconsistent data type in column: " + colName + " in a row: " + rowNumber);
    }
}
