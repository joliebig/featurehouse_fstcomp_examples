

import java.util.*;
import Jakarta.util.FixDosOutputStream;
import java.io.*;

// this layer harvests line numbers from tokens.
// it extends the token interface and astnodes

public interface AstTokenInterface{
    int lineNum();
}
