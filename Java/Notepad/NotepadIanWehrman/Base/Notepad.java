
 
//import the packages for using the classes in them into the program
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;


/**
 *A PUBLIC CLASS FOR NOTEPAD.JAVA
 */
public class Notepad extends JFrame{
    //for using the methods in these classes
    public Actions actions = new Actions(this);
    public Center center = new Center(this);
	

    //for using undo & redo
    //    UndoManager undo = new UndoManager();
    //UndoAction undoAction = new UndoAction(this);
    //RedoAction redoAction = new RedoAction(this);

	
    //declaration of the private variables used in the program
    //create the text area
    JTextArea textArea;
    //create the Menu Bar that contains the JMenu "filE, ediT, vieW, formaT, helP"
    JMenuBar Menubar;
    //Create the menu that contains the items
    JMenu filE, ediT, vieW, formaT, helP;
    //Create the menu items
    JMenuItem neW, opeN, prinT, exiT, abouT,
	cuT, copY, pastE, selectALL;
    JCheckBoxMenuItem lineWraP;
    //Create the Tool Bar that contains the JButton
    JToolBar toolBar;
    //Create the buttons
    JButton newButton, openButton,  printButton, 
	cutButton, copyButton, pasteButton,
	aboutButton;
    //Create Scroll pane (JScrollPane) for the JTextArea
    JScrollPane scrollpane;
	
    //for using lineWrap & textArea @Actions.java
    public JCheckBoxMenuItem getLineWrap(){
        return lineWraP;
    }
    public JTextArea getTextArea(){
        return textArea;
    }



    void setup() {
        actions = new Actions(this);
        center = new Center(this);
    }


    void initMenuBar() {
        //for setting the menu bar
        Menubar = new JMenuBar();
        setJMenuBar(Menubar);
        //adding file, edit, view, format, help to the menu bar
        Menubar.add(filE   = new JMenu("File"));
        Menubar.add(ediT   = new JMenu("Edit"));
        Menubar.add(vieW   = new JMenu("View"));
        Menubar.add(formaT = new JMenu("Format"));
        Menubar.add(helP   = new JMenu("Help"));
    }       

    void initMenuItems() {
	
        /**
         *adding neW, opeN, savE, saveAS, prinT & exiT to the filE Menu,
         *adding a small image icon to the menu item &
         *adding separator between the menu item
         */
        filE.add(neW    = new JMenuItem("New", new ImageIcon(this.getClass().getResource("images/new.gif"))));
        filE.add(opeN   = new JMenuItem("Open", new ImageIcon(this.getClass().getResource("images/open.gif"))));

        filE.add(prinT  = new JMenuItem("Print", new ImageIcon(this.getClass().getResource("images/print.gif"))));
        filE.add(exiT   = new JMenuItem("Exit")); //, new ImageIcon(this.getClass().getResource("images/exit.gif"))));  -- exit.gif missing
        filE.insertSeparator(4);
        filE.insertSeparator(6);
		
        /**
         *adding cuT, copY, pastE, finD, findNexT & selectALL to the ediT Menu,
         *adding a small image icon to the menu item &
         *adding separator between the menu item
         */

        ediT.add(cuT  = new JMenuItem("Cut",  new ImageIcon(this.getClass().getResource("images/cut.gif"))));
        ediT.add(copY = new JMenuItem("Copy", new ImageIcon(this.getClass().getResource("images/copy.gif"))));
        ediT.add(pastE= new JMenuItem("Paste",new ImageIcon(this.getClass().getResource("images/paste.gif"))));

        ediT.addSeparator();

        ediT.add(selectALL= new JMenuItem("Select All"));


		
        /**
         *adding lineWraP & fonT to the formaT Menu,
         *adding abouT to the helP Menu & 
         *adding a samll image icon to the menu item
         */
        formaT.add(lineWraP = new JCheckBoxMenuItem("Line Wrap"));

        helP.add(abouT = new JMenuItem("About Notepad", new ImageIcon(this.getClass().getResource("images/about.gif"))));
                
    }

    void initMnemonics() {
        /**
         *allowing the file   menu to be selected by pressing ALT + F
         *allowing the edit   menu to be selected by pressing ALT + E
         *allowing the view   menu to be selected by pressing ALT + V
         *allowing the format menu to be selected by pressing ALT + O
         *allowing the help   menu to be selected by pressing ALT + H
         */
        filE.setMnemonic('f');
        ediT.setMnemonic('e');
        vieW.setMnemonic('v');
        formaT.setMnemonic('o');
        helP.setMnemonic('h');
    }

    void initAccelerators() {

        /**
         *allowing the neW       menu item to be selected by pressing ALT + N
         *allowing the opeN      menu item to be selected by pressing ALT + O
         *allowing the savE      menu item to be selected by pressing ALT + S
         *allowing the prinT     menu item to be selected by pressing ALT + P
         *allowing the exiT      menu item to be selected by pressing ALT + F4
         *allowing the cuT       menu item to be selected by pressing ALT + X
         *allowing the copY      menu item to be selected by pressing ALT + C
         *allowing the pastE     menu item to be selected by pressing ALT + V
         *allowing the finD      menu item to be selected by pressing ALT + F
         *allowing the findNexT  menu item to be selected by pressing ALT + F3
         *allowing the selectAll menu item to be selected by pressing ALT + A
         */
        neW.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        opeN.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        
        prinT.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.CTRL_MASK));
        exiT.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, ActionEvent.CTRL_MASK));
        cuT.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
        copY.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
        pastE.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK));

        selectALL.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK));
	
    }

    void initToolBar() {
        /**
         *adding newButton, openButton, saveButton, saveAsButton, printButton,
         *cutButton, copyButton, pasteButton, fontButton & aboutButton to tool bar,
         *adding a small image icon to the menu item &
         *adding separator between the button
         */
        toolBar.add(newButton   = new JButton(new ImageIcon(this.getClass().getResource("images/new.gif"))));
        toolBar.add(openButton  = new JButton(new ImageIcon(this.getClass().getResource("images/open.gif"))));

        toolBar.add(printButton = new JButton(new ImageIcon(this.getClass().getResource("images/print.gif"))));

        toolBar.addSeparator();
        toolBar.add(cutButton   = new JButton(new ImageIcon(this.getClass().getResource("images/cut.gif"))));
        toolBar.add(copyButton  = new JButton(new ImageIcon(this.getClass().getResource("images/copy.gif"))));
        toolBar.add(pasteButton = new JButton(new ImageIcon(this.getClass().getResource("images/paste.gif"))));

        toolBar.addSeparator();
        
        toolBar.add(aboutButton = new JButton(new ImageIcon(this.getClass().getResource("images/about.gif"))));

    }

    void initToolTips() {
        //adding a tool tip text to the button for descriping the image icon.
        newButton.setToolTipText("New");
        openButton.setToolTipText("Open");
        
        printButton.setToolTipText("Print");
        cutButton.setToolTipText("Cut");
        copyButton.setToolTipText("Copy");
        pasteButton.setToolTipText("Paste");
        
        
        aboutButton.setToolTipText("About Notepad");
    }

    void initActionListeners() {
        /**
         *adding action listener for menu item: neW, opeN, savE, saveAS, prinT, exiT,
         *copY, cuT, pastE, finD, findNexT, selectALL, lineWraP, fonT & abouT
         *the actions was written @Actions.java
         */
        neW.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent ae){
                    actions.neW();
                }
            });
        opeN.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent ae){
                    actions.opeN();
                }
            });
        
        prinT.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent ae){
                    actions.prinT();
                }
            });
        exiT.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent ae){
                    actions.exiT();
                }
            });
        cuT.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent ae){
                    actions.cuT();
                }
            });
        copY.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent ae){
                    actions.copY();
                }
            });
        pastE.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent ae){
                    actions.pastE();
                }
            });
        selectALL.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent ae){
                    actions.selectALL();
                }
            });
        
        lineWraP.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent ae){
                    actions.lineWraP();
                }
            });
        
        abouT.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent ae){
                    actions.abouT();
                }
            });
		
        /**
         *adding action listener for the button in the tool bar: newButton, 
         *openButton, saveButton, saveAsButton, printButton, cutButton, pasteButton, 
         *findButton, selectALL, lineWraP, fontButton & aboutButton
         *the actions was written @Actions.java
         */
        newButton.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent ae){
                    actions.neW();
                }
            });
        openButton.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent ae){
                    actions.opeN();
                }
            });
        
        printButton.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent ae){
                    actions.prinT();
                }
            });
        cutButton.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent ae){
                    actions.cuT();
                }
            });
        copyButton.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent ae){
                    actions.copY();
                }
            });
        pasteButton.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent ae){
                    actions.pastE();
                }
            });
        
        
        aboutButton.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent ae){
                    actions.abouT();
                }
            });			
        
    }


    //Constructor of Notepad
    public Notepad(){

        setup();

        //set the title for Notepad and set the size for it.
        setTitle("Untitled - JAVA Notepad");
        setSize(800,600);
		
        //get the graphical user interface components display area
        Container cp = getContentPane();
        /**
         *adding the text area,
         *adding the tool bar &
         *adding the scroll pane to the container
         */
        cp.add(textArea = new JTextArea());
        cp.add("North", toolBar = new JToolBar("Tool Bar"));
        cp.add(scrollpane = new JScrollPane(textArea)); 



	initMenuBar();
        initMenuItems();
        initMnemonics();
        initAccelerators();
        initToolBar();
        initToolTips();
		
		
        /**
         *setting the default close operation to false &
         *using own action (exiT action @Actions.java)
         */
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter(){
                public void windowClosing(WindowEvent e){
                    actions.exiT();
                }
            });
	

        initActionListeners();
	
        /**
         *Setting the Line Wrap & Wrap Style Word features are true 
         */
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        /**
         *for making the program at the center, 
         *@see Center.java 
         */
        center.nCenter();
        show();
    }
    //Main Method
    public static void main(String[] args){
        new Notepad();
		
    }
}			
