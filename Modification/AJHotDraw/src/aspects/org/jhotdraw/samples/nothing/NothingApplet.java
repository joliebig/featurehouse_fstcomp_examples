
package org.jhotdraw.samples.nothing; 
import javax.swing.JPanel; 
import org.jhotdraw.framework.*; 
import org.jhotdraw.figures.*; 
import org.jhotdraw.standard.*; 
import org.jhotdraw.applet.*; 
import org.jhotdraw.contrib.*; 
public  class  NothingApplet  extends DrawApplet {
		protected void createTools(JPanel palette) {	super.createTools(palette);	Tool tool = new TextTool(this, new TextFigure());	palette.add(createToolButton(IMAGES + "TEXT", "Text Tool", tool));	tool = new CreationTool(this, new RectangleFigure());	palette.add(createToolButton(IMAGES + "RECT", "Rectangle Tool", tool));	tool = new CreationTool(this, new RoundRectangleFigure());	palette.add(createToolButton(IMAGES + "RRECT", "Round Rectangle Tool", tool));	tool = new CreationTool(this, new EllipseFigure());	palette.add(createToolButton(IMAGES + "ELLIPSE", "Ellipse Tool", tool));	tool = new CreationTool(this, new LineFigure());	palette.add(createToolButton(IMAGES + "LINE", "Line Tool", tool));	tool = new PolygonTool(this);	palette.add(createToolButton(IMAGES + "POLYGON", "Polygon Tool", tool));	tool = new ConnectionTool(this, new LineConnection());	palette.add(createToolButton(IMAGES + "CONN", "Connection Tool", tool));	tool = new ConnectionTool(this, new ElbowConnection());	palette.add(createToolButton(IMAGES + "OCONN", "Elbow Connection Tool", tool));	}


}
