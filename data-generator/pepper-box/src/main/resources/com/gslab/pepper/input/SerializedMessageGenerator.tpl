import java.util.Iterator;

import static com.gslab.pepper.input.FieldDataFunctions.*;
import static com.gslab.pepper.input.CustomFunctions.*;

public class {{JAVA_CLASS_PLACEHOLDER}} implements Iterator<Object> {

 private static {{OBJ_CLASS}} serializedObj = new {{OBJ_CLASS}}();

 @Override
 public boolean hasNext() {
     return true;
 }

 @Override
 public Object next() {

     {{MESSAGE_PLACE_HOLDER}}

     return serializedObj;
 }

 @Override
 public void remove() {
 }
}