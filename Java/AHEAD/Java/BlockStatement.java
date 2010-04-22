

/** superclass of all Modifiers 
 * @layer<Java>
 */

public class BlockStatement {

    /** return name of modifier 
     * @layer<Java>
     */

    public AST_Stmt toAST_Stmt() {
        return (AST_Stmt) new AST_Stmt().add( 
		                    (new AST_StmtElem()).setParms(this));
    }
}
