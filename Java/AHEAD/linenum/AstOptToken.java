

import java.util.*;
import Jakarta.util.FixDosOutputStream;
import java.io.*;

public class AstOptToken
          implements AstTokenInterface {
    public int lineNum() {
        return ( tok[0] != null ) ? tok[0].lineNum() : -1;
    }
}
