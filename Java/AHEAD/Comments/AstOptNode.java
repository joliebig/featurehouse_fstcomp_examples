

import java.util.*;
import java.io.*;

public class AstOptNode {

    public  AstToken findToken() {
        if ( arg[0] == null )
            return null;
        return arg[0].findToken();
    }
}
