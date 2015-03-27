package ch.epfl.imhof.testUtilities;

import java.util.List;

public class ListNonMutableTestUtility {
    public static <T> boolean nonMutableFieldListTest(List<T> argumentInitialList, List<T> fieldInsideList){
        if (!argumentInitialList.isEmpty()){
            argumentInitialList.clear();
            return !fieldInsideList.isEmpty();
        } else {
            argumentInitialList.add(null);
            return fieldInsideList.isEmpty();
            
        }
    }

}
