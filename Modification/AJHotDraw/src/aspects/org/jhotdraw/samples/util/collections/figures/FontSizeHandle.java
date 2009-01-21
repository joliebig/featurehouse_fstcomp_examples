
package org.jhotdraw.figures; 
import org.jhotdraw.framework.*; 
import org.jhotdraw.standard.*; 
import org.jhotdraw.util.Undoable; 
import org.jhotdraw.util.UndoableAdapter; 
import java.awt.*; 
public  class  FontSizeHandle  extends LocatorHandle {
		public FontSizeHandle(Figure owner, Locator l) {	super(owner, l);	}

		public void invokeStart(int x, int y, DrawingView view) {	setUndoActivity(createUndoActivity(view));	getUndoActivity().setAffectedFigures(new SingleFigureEnumerator(owner()));	}

		public void invokeStep (int x, int y, int anchorX, int anchorY, DrawingView view) {	TextFigure textOwner = (TextFigure) owner();	FontSizeHandle.UndoActivity activity = (FontSizeHandle.UndoActivity)getUndoActivity();	int newSize = activity.getFont().getSize() + y-anchorY;	textOwner.setFont(new Font(activity.getFont().getName(), activity.getFont().getStyle(), newSize));	}

		public void invokeEnd(int x, int y, int anchorX, int anchorY, DrawingView view) {	TextFigure textOwner = (TextFigure) owner();	FontSizeHandle.UndoActivity activity = (FontSizeHandle.UndoActivity)getUndoActivity();	if (textOwner.getFont().getSize() == activity.getOldFontSize()) {	setUndoActivity(null);	}	else {	activity.setFont(textOwner.getFont());	}	}

		public void draw(Graphics g) {	Rectangle r = displayBox();	g.setColor(Color.yellow);	g.fillOval(r.x, r.y, r.width, r.height);	g.setColor(Color.black);	g.drawOval(r.x, r.y, r.width, r.height);	}

		protected Undoable createUndoActivity(DrawingView newView) {	TextFigure textOwner = (TextFigure)owner();	return new FontSizeHandle.UndoActivity(newView, textOwner.getFont());	}

		public static  class  UndoActivity  extends UndoableAdapter {
			private Font myFont;

			private int myOldFontSize;

			public UndoActivity(DrawingView newView, Font newFont) {	super(newView);	setFont(newFont);	setOldFontSize(getFont().getSize());	setUndoable(true);	setRedoable(true);	}

			public boolean undo() {	if (!super.undo()) {	return false;	}	swapFont();	return true;	}

			public boolean redo() {	if (!isRedoable()) {	return false;	}	swapFont();	return true;	}

			protected void swapFont() {	setOldFontSize(replaceFontSize());	FigureEnumeration fe = getAffectedFigures();	while (fe.hasNextFigure()) {	((TextFigure)fe.nextFigure()).setFont(getFont());	}	}

			private int replaceFontSize() {	int tempFontSize = getFont().getSize();	setFont(new Font(getFont().getName(), getFont().getStyle(), getOldFontSize()));	return tempFontSize;	}

			protected void setFont(Font newFont) {	myFont = newFont;	}

			public Font getFont() {	return myFont;	}

			protected void setOldFontSize(int newOldFontSize) {	myOldFontSize = newOldFontSize;	}

			public int getOldFontSize() {	return myOldFontSize;	}


	}


}
