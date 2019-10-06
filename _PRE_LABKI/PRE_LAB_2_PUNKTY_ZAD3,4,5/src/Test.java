import files.*;

import java.util.LinkedList;
import java.util.Scanner;

public class Test {

    public static void main(String[] args) {

//        Punkt2D A2=new Punkt2D(0,0);
//        Punkt2D B2=new Punkt2D(3,3);
//	    System.out.println("The distance between two points (type: Punkt2D) is: " + A2.distance(B2));
//
//	    Punkt3D A3=new Punkt3D(0,0, 0);
//        Punkt3D B3=new Punkt3D(3,3, 3);
//        System.out.println("\nThe distance between two points (type: Punkt3D) is: " + A3.distance(B3));
//
        Scanner scanner = new Scanner(System.in);
        LinkedList<Punkt3D> punkty= new LinkedList<Punkt3D>();

        while(true){
            System.out.println("1. Wczytaj punkt 3D");
            System.out.println("2. Wyświetl wszystkie punkty");
            System.out.println("3. Oblicz odległość 3D");
            System.out.println("4. Zakończ");
            int choose= scanner.nextInt();
            switch(choose){
                case 1:
                    System.out.println("\n\nDODWANIE PUNKTU 3D");
                    System.out.println("Podaj współrzędną x: ");
                    double x=scanner.nextDouble();
                    System.out.println("Podaj współrzędną y: ");
                    double y=scanner.nextDouble();
                    System.out.println("Podaj współrzędną z: ");
                    double z=scanner.nextDouble();
                    punkty.add(new Punkt3D(x,y,z));
                    System.out.println("DODANO!\n\n");
                    break;
                case 2:
                    System.out.println("ZAPISANE PUNKTY: ");
                    int itr=1;
                    for(Punkt3D pkt : punkty){
                        System.out.println("Punkt numer: " + itr + "\tx: " + pkt.getX() + "\ty: " + pkt.getY() + "\tz: " + pkt.getZ());
                        itr++;
                    }
                    System.out.print("\n\n");
                    break;
                case 3:
                    int pktOne;
                    int pktTwo;
                    System.out.println("\n\nODLEGŁOŚĆ POMIĘZDZY P1 i P2:");
                        System.out.println("Podaj numer punktu P1: ");
                        pktOne = scanner.nextInt();
                        if(pktOne > punkty.size() || pktOne <= 0){
                            System.out.println("NIE MA TAKIEGO PUNKTU!!!\n");
                            break;
                        }
                        System.out.println("Podaj numer punktu P2: ");
                        pktTwo = scanner.nextInt();
                        if(pktTwo > punkty.size()|| pktTwo <= 0){
                            System.out.println("NIE MA TAKIEGO PUNKTU!!!\n");
                            break;
                        }
                    System.out.println("Odległość pomiędzy punktami P1 i P2 wynosi: " + punkty.get(pktOne-1).distance(punkty.get(pktTwo-1)) + "\n\n");


                    break;
                case 4:
                    System.exit(0);
                    break;
                default:
                    System.out.println("INVALID NUMBER!");
            }
        }
}
}
