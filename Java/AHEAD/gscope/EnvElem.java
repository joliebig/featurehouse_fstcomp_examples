

import java.io.PrintWriter;

public class EnvElem {
    public  EnvElem next;
    public String id;
    public  Environment _environment;
    public  AstNode _alias;

    public EnvElem( String name,  Environment env,  EnvElem n ) {
        id = name;
        next = n;
        _environment = env;
    }

    public String mangleNum() {
        return ( _environment.mangleNum() );
    }
}
