
package org.jhotdraw.applet; 
import javax.swing.*; 
import java.awt.*; 
import java.awt.event.*; 
import java.util.*; 
import java.io.*; 
import java.net.*; 
import org.jhotdraw.framework.*; 
import org.jhotdraw.standard.*; 
import org.jhotdraw.figures.*; 
import org.jhotdraw.util.*; 
public  class  DrawApplet 	extends JApplet 	implements DrawingEditor, PaletteListener, VersionRequester {
		private transient Drawing fDrawing;

		private transient Tool fTool;

		private transient DrawingView fView;

		private transient ToolButton fDefaultToolButton;

		private transient ToolButton fSelectedToolButton;

		private transient boolean fSimpleUpdate;

		private transient JButton fUpdateButton;

		private transient JComboBox fFrameColor;

		private transient JComboBox fFillColor;

		private transient JComboBox fTextColor;

		private transient JComboBox fArrowChoice;

		private transient JComboBox fFontChoice;

		private transient	UndoManager myUndoManager;

		static String fgUntitled = "untitled";

		private static final String fgDrawPath = "/org/jhotdraw/";

		public static final String IMAGES = fgDrawPath+"images/";

		public void init() {	createIconkit();	getVersionControlStrategy().assertCompatibleVersion();	setUndoManager(new UndoManager());	getContentPane().setLayout(new BorderLayout());	fView = createDrawingView();	JPanel attributes = createAttributesPanel();	createAttributeChoices(attributes);	getContentPane().add("North", attributes);	JPanel toolPanel = createToolPalette();	createTools(toolPanel);	getContentPane().add("West", toolPanel);	getContentPane().add("Center", (Component)view());	JPanel buttonPalette = createButtonPanel();	createButtons(buttonPalette);	getContentPane().add("South", buttonPalette);	initDrawing();	setupAttributes();	}

		public void addViewChangeListener(ViewChangeListener vsl) {	}

		public void removeViewChangeListener(ViewChangeListener vsl) {	}

		protected Iconkit createIconkit() {	return new Iconkit(this);	}

		protected JPanel createAttributesPanel() {	JPanel panel = new JPanel();	panel.setLayout(new PaletteLayout(2, new Point(2,2), false));	return panel;	}

		protected void createAttributeChoices(JPanel panel) {	panel.add(new JLabel("Fill"));	fFillColor = createColorChoice(FigureAttributeConstant.FILL_COLOR);	panel.add(fFillColor);	panel.add(new JLabel("Text"));	fTextColor = createColorChoice(FigureAttributeConstant.TEXT_COLOR);	panel.add(fTextColor);	panel.add(new JLabel("Pen"));	fFrameColor = createColorChoice(FigureAttributeConstant.FRAME_COLOR);	panel.add(fFrameColor);	panel.add(new JLabel("Arrow"));	CommandChoice choice = new CommandChoice();	fArrowChoice = choice;	FigureAttributeConstant arrowMode = FigureAttributeConstant.ARROW_MODE;	choice.addItem(new ChangeAttributeCommand("none", arrowMode, new Integer(PolyLineFigure.ARROW_TIP_NONE), this));	choice.addItem(new ChangeAttributeCommand("at Start", arrowMode, new Integer(PolyLineFigure.ARROW_TIP_START), this));	choice.addItem(new ChangeAttributeCommand("at End", arrowMode, new Integer(PolyLineFigure.ARROW_TIP_END), this));	choice.addItem(new ChangeAttributeCommand("at Both", arrowMode, new Integer(PolyLineFigure.ARROW_TIP_BOTH), this));	panel.add(fArrowChoice);	panel.add(new JLabel("Font"));	fFontChoice = createFontChoice();	panel.add(fFontChoice);	}

		protected JComboBox createColorChoice(FigureAttributeConstant attribute) {	CommandChoice choice = new CommandChoice();	for (int i = 0; i < ColorMap.size(); i++)	choice.addItem(	new ChangeAttributeCommand(	ColorMap.name(i),	attribute,	ColorMap.color(i),	this	)	);	return choice;	}

		protected JComboBox createFontChoice() {	CommandChoice choice = new CommandChoice();	String fonts[] = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();	for (int i = 0; i < fonts.length; i++) {	choice.addItem(new ChangeAttributeCommand(fonts[i], FigureAttributeConstant.FONT_NAME, fonts[i], this));	}	return choice;	}

		protected JPanel createButtonPanel() {	JPanel panel = new JPanel();	panel.setLayout(new PaletteLayout(2, new Point(2,2), false));	return panel;	}

		protected void createButtons(JPanel panel) {	panel.add(new Filler(24,20));	JComboBox drawingChoice = new JComboBox();	drawingChoice.addItem(fgUntitled);	String param = getParameter("DRAWINGS");	if (param == null) {	param = "";	}	StringTokenizer st = new StringTokenizer(param);	while (st.hasMoreTokens()) {	drawingChoice.addItem(st.nextToken());	}	if (drawingChoice.getItemCount() > 1) {	panel.add(drawingChoice);	}	else {	panel.add(new JLabel(fgUntitled));	}	drawingChoice.addItemListener(	new ItemListener() {	public void itemStateChanged(ItemEvent e) {	if (e.getStateChange() == ItemEvent.SELECTED) {	loadDrawing((String)e.getItem());	}	}	}	);	panel.add(new Filler(6,20));	JButton button;	button = new CommandButton(new DeleteCommand("Delete", this));	panel.add(button);	button = new CommandButton(new DuplicateCommand("Duplicate", this));	panel.add(button);	button = new CommandButton(new GroupCommand("Group", this));	panel.add(button);	button = new CommandButton(new UngroupCommand("Ungroup", this));	panel.add(button);	button = new JButton("Help");	button.addActionListener(	new ActionListener() {	public void actionPerformed(ActionEvent event) {	showHelp();	}	}	);	panel.add(button);	fUpdateButton = new JButton("Simple Update");	fUpdateButton.addActionListener(	new ActionListener() {	public void actionPerformed(ActionEvent event) {	if (fSimpleUpdate) {	setBufferedDisplayUpdate();	}	else {	setSimpleDisplayUpdate();	}	}	}	);	}

		protected JPanel createToolPalette() {	JPanel palette = new JPanel();	palette.setLayout(new PaletteLayout(2,new Point(2,2)));	return palette;	}

		protected void createTools(JPanel palette) {	Tool tool = createSelectionTool();	fDefaultToolButton = createToolButton(IMAGES + "SEL", "Selection Tool", tool);	palette.add(fDefaultToolButton);	}

		protected Tool createSelectionTool() {	return new SelectionTool(this);	}

		protected ToolButton createToolButton(String iconName, String toolName, Tool tool) {	return new ToolButton(this, iconName, toolName, tool);	}

		protected Drawing createDrawing() {	return new StandardDrawing();	}

		protected DrawingView createDrawingView() {	return new StandardDrawingView(this, 410, 370);	}

		public void paletteUserSelected(PaletteButton button) {	ToolButton toolButton = (ToolButton) button;	setTool(toolButton.tool(), toolButton.name());	setSelected(toolButton);	}

		public void paletteUserOver(PaletteButton button, boolean inside) {	if (inside) {	showStatus(button.name());	}	else if (fSelectedToolButton != null) {	showStatus(fSelectedToolButton.name());	}	}

		public Drawing drawing() {	return fDrawing;	}

		public Tool tool() {	return fTool;	}

		public DrawingView view() {	return fView;	}

		public DrawingView[] views() {	return new DrawingView[] { view() } ;	}

		public void toolDone() {	setTool(fDefaultToolButton.tool(), fDefaultToolButton.name());	setSelected(fDefaultToolButton);	}

		public void viewSelectionChanged(DrawingView oldView, DrawingView newView) {	}

		private void initDrawing() {	fDrawing = createDrawing();	view().setDrawing(fDrawing);	toolDone();	}

		private void setTool(Tool t, String name) {	if (fTool != null) {	fTool.deactivate();	}	fTool = t;	if (fTool != null) {	showStatus(name);	fTool.activate();	}	}

		private void setSelected(ToolButton button) {	if (fSelectedToolButton != null) {	fSelectedToolButton.reset();	}	fSelectedToolButton = button;	if (fSelectedToolButton != null) {	fSelectedToolButton.select();	}	}

		protected void loadDrawing(String param) {	if (param == fgUntitled) {	fDrawing.release();	initDrawing();	return;	}	String filename = getParameter(param);	if (filename != null) {	readDrawing(filename);	}	}

		private void readDrawing(String filename) {	toolDone();	String type = guessType(filename);	if (type.equals("storable")) {	readFromStorableInput(filename);	}	else if (type.equals("serialized")) {	readFromObjectInput(filename);	}	else {	showStatus("Unknown file type");	}	}

		private void readFromStorableInput(String filename) {	try {	URL url = new URL(getCodeBase(), filename);	InputStream stream = url.openStream();	StorableInput input = new StorableInput(stream);	fDrawing.release();	fDrawing = (Drawing)input.readStorable();	view().setDrawing(fDrawing);	}	catch (IOException e) {	initDrawing();	showStatus("Error:" + e);	}	catch (org.aspectj.lang.SoftException e) {	showStatus("Error: " + e.getWrappedThrowable());	}	}

		private void readFromObjectInput(String filename) {	try {	URL url = new URL(getCodeBase(), filename);	InputStream stream = url.openStream();	ObjectInput input = new ObjectInputStream(stream);	fDrawing.release();	fDrawing = (Drawing)input.readObject();	view().setDrawing(fDrawing);	}	catch (IOException e) {	initDrawing();	showStatus("Error: " + e);	}	catch (ClassNotFoundException e) {	initDrawing();	showStatus("Class not found: " + e);	}	}

		private String guessType(String file) {	if (file.endsWith(".draw")) {	return "storable";	}	if (file.endsWith(".ser")) {	return "serialized";	}	return "unknown";	}

		private void setupAttributes() {	Color frameColor = (Color) AttributeFigure.getDefaultAttribute(FigureAttributeConstant.FRAME_COLOR);	Color fillColor = (Color) AttributeFigure.getDefaultAttribute(FigureAttributeConstant.FILL_COLOR);	Integer arrowMode = (Integer) AttributeFigure.getDefaultAttribute(FigureAttributeConstant.ARROW_MODE);	String fontName = (String) AttributeFigure.getDefaultAttribute(FigureAttributeConstant.FONT_NAME);	FigureEnumeration fe = view().selection();	while (fe.hasNextFigure()) {	Figure f = fe.nextFigure();	frameColor = (Color) f.getAttribute(FigureAttributeConstant.FRAME_COLOR);	fillColor = (Color) f.getAttribute(FigureAttributeConstant.FILL_COLOR);	arrowMode = (Integer) f.getAttribute(FigureAttributeConstant.ARROW_MODE);	fontName = (String) f.getAttribute(FigureAttributeConstant.FONT_NAME);	}	fFrameColor.setSelectedIndex(ColorMap.colorIndex(frameColor));	fFillColor.setSelectedIndex(ColorMap.colorIndex(fillColor));	if (arrowMode != null) {	fArrowChoice.setSelectedIndex(arrowMode.intValue());	}	if (fontName != null) {	fFontChoice.setSelectedItem(fontName);	}	}

		protected void setSimpleDisplayUpdate() {	view().setDisplayUpdate(new SimpleUpdateStrategy());	fUpdateButton.setText("Simple Update");	fSimpleUpdate = true;	}

		protected void setBufferedDisplayUpdate() {	view().setDisplayUpdate(new BufferedUpdateStrategy());	fUpdateButton.setText("Buffered Update");	fSimpleUpdate = false;	}

		protected void showHelp() {	try {	String appletPath = getClass().getName().replace('.', '/');	URL url = new URL(getCodeBase(), appletPath + "Help.html");	getAppletContext().showDocument(url, "Help");	}	catch (IOException e) {	showStatus("Help file not found");	}	}

		protected void setUndoManager(UndoManager newUndoManager) {	myUndoManager = newUndoManager;	}

		public UndoManager getUndoManager() {	return myUndoManager;	}

		protected VersionControlStrategy getVersionControlStrategy() {	return new StandardVersionControlStrategy(this);	}

		public String[] getRequiredVersions() {	String[] requiredVersions = new String[1];	requiredVersions[0] = VersionManagement.getPackageVersion(DrawApplet.class.getPackage());	return requiredVersions;	}


}
