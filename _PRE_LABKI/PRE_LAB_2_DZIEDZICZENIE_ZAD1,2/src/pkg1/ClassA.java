package pkg1;

public class ClassA {
    public ClassA(int number, String name){}

    //  int number, String name
    public void callDecrement(){
        decrement();
    }

    public void callChangeName(String name){
        changeName(name);
    }
    public void callIncerement(){
        increment();
    }
    private void increment(){
        number+=1;
    }
    protected void decrement(){
        number-=1;
    }
    void changeName(String name){
        this.name=name;
    }

    int number;
    protected String name;


}
