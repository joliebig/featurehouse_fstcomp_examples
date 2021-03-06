
public class Missile {

	public void malen() {
		long elapsedTime = System.currentTimeMillis() - currTime;
		currTime += elapsedTime;
		if (tankManager.status == GameManager.PAUSE || tankManager.status == GameManager.EXIT) {
			elapsedTime = 0;
		}
		koordinateAktualisieren(elapsedTime);

		switch (missileRichtung) {
		case 1:
			tankManager.maler.drawImage("missileU.png", x_Koordinate, y_Koordinate);
			break;
		case 3:
			tankManager.maler.drawImage("missileR.png", x_Koordinate, y_Koordinate);
			break;

		case 5:
			tankManager.maler.drawImage("missileD.png", x_Koordinate, y_Koordinate);
			break;
		case 7:
			tankManager.maler.drawImage("missileL.png", x_Koordinate, y_Koordinate);
			break;
		}
	}

}