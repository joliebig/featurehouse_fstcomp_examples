
public class Wall {

	protected void malen() {
		for (int i = 0; i < wallTeile.size(); i++) {
			int x = ((GameObject) wallTeile.elementAt(i)).x_Koordinate;
			int y = ((GameObject) wallTeile.elementAt(i)).y_Koordinate;
			int wallTypeImage = ((GameObject) wallTeile.elementAt(i)).objectType;
			tankManager.maler.drawImage(wallTypeImage + ".png", x, y);
		}

	}
	

}