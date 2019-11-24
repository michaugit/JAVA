package files;

import java.util.Comparator;

public class CustomComparator implements Comparator<Value> {
    @Override
    public int compare(Value o1, Value o2) {
        if(o1.eq(o2)){
            return 0;
        }
        else if(o1.lte(o2)){
            return -1;
        }
        else if(o1.gte(o2)){
            return 1;
        }

        else{
            throw new RuntimeException("Wrong compare!");
        }
    }
}
