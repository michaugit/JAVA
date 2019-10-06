
import pkg1.*;
import pkg2.*;

public class Main {

    public static void main(String[] args) {
        ClassA ObjA = new ClassA(123, "Jestem klasaA");
        ClassB ObjB = new ClassB();
        ClassC ObjC = new ClassC();

        //podsumowujac pola o dostepie pakietowym sa widzialne w klasach pochodnych tylko i wylacznie jesli klasa pochodnych jest z tego samego pakietu
        //tzn. ClassB widzi oba pola protected i package, ClassC z racji ze jest z innego pakeitu widzi tylko pole protected

        System.out.println(ObjA.toString());
    }
}
