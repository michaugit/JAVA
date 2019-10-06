package pkg1;

public class ClassB extends ClassA {
   public ClassB(){
       super(0,"Jestem klasaB");
   }
    protected void decrement(){
       this.number-=2;
    }
    void changeName(String name){
       this.name=name;
    }
    private void increment(){
       this.number+=2;
    }


}
