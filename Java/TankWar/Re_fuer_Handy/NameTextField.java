
import java.util.Vector;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

public class NameTextField {

	Maler maler;
	Vector name;
	int backcolorR, backcolorG, backcolorB;
	int textcolorR, textcolorG, textcolorB;
	int width, height;
	int xCenter, yCenter;
	int charwidth;
	int charnummer;
	long time;
	boolean blink = true;

	public NameTextField(Maler maler) {
		this.maler = maler;
		backcolorR = 0;
		backcolorG = 0;
		backcolorB = 0;
		textcolorR = 255;
		textcolorG = 255;
		textcolorB = 255;
		width = 20;
		height = Font.getDefaultFont().getHeight() + 2;
		xCenter = 0;
		yCenter = 0;
		name = new Vector(1, 1);
		name.addElement(new NameChar('A'));
		charwidth = Font.getDefaultFont().charWidth('A');
		time = System.currentTimeMillis();
	}

	public void setCenter(int x, int y) {
		xCenter = x;
		yCenter = y;
	}
	
	public void setBackColor(int R,int G,int B){
		backcolorR=R;
		backcolorG=G;
		backcolorB=B;
	}
	
	public void setTextColor(int R,int G,int B){
		textcolorR=R;
		textcolorG=G;
		textcolorB=B;
	}

	public void setWidth(int x) {
		charnummer = x;
		width = charnummer * charwidth;
	}

	public void erscheinen(Graphics g) {
		int color = g.getColor();

		g.setColor(backcolorR, backcolorG, backcolorB);
		g.fillRect(xCenter - width / 2, yCenter - height / 2, width, height);
		g.setColor(0, 255, 0);
		g.drawRect(xCenter - width / 2 - 1, yCenter - height / 2 - 1, width + 1, height + 1);
		g.setColor(backcolorR, backcolorG, backcolorB);
		g.drawRect(xCenter - width / 2 - 2, yCenter - height / 2 - 2, width + 3, height + 3);

		g.setColor(textcolorR, textcolorG, textcolorB);

		for (int i = 0; i < name.size(); i++) {
			g.drawString(((NameChar) name.elementAt(i)).getString(), xCenter - width / 2 + i
					* charwidth, yCenter - height / 2 + 1, Graphics.TOP | Graphics.LEFT);
		}
		if (System.currentTimeMillis() - time > 300) {
			time = System.currentTimeMillis();
			blink = !blink;
		}
		if (blink) {
			g.fillRect(xCenter - width / 2 + (name.size() - 1) * charwidth, yCenter - height / 2
					+ 1, charwidth, height - 2);
		}
		g.setColor(color);
	}

	public void keyBehandeln(int key) {
		if (key == 38) {
			if (name.size() > 0) {
				((NameChar) name.lastElement()).up();
			}
		}
		if (key == 40) {
			if (name.size() > 0) {
				((NameChar) name.lastElement()).down();
			}
		}
		if (key == 37) {
			if (name.size() > 0) {
				int index = name.size() - 1;
				name.removeElementAt(index);
			}
		}
		if (key == 39) {
			if (name.size() < charnummer) {
				name.addElement(new NameChar('A'));
			}
		}
	}

	public String getText() {
		String text = "";
		for (int i = 0; i < name.size(); i++) {
			text = text + ((NameChar) name.elementAt(i)).getString();
		}
		return text;
	}
}
