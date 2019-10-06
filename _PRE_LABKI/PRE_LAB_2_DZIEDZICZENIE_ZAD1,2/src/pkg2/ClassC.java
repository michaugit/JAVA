package pkg2;

import pkg1.ClassB;

public class ClassC extends ClassB {
void changeName(){
    this.callChangeName("package method from ClassC");
}
}
