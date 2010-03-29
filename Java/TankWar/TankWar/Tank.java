
import java.util.Random;
import java.util.Vector;

public class Tank extends GameObject {

	protected float geschwindigkeit, xf_Koordinate, yf_Koordinate;
	protected long currTime;
	protected long time, time1;
	protected int missileType = 70;
	protected long feuernKontroller;
	protected long feuernHaufigkeit = 700;// ms
	protected int energie;
	protected static Random random = new Random();
	protected boolean aktive = true;
	protected boolean feuern = false;
	protected boolean keyUp = false, keyDown = false, keyLeft = false, keyRight = false;
	protected boolean flachHeit = false;
	protected boolean feindlich;
	protected int beschId;
	protected long elapsedTime;

	/*
	 * U=1 UR=2 R=3 DR=4 D=5 LD=6 L=7 UL=8
	 */
	protected int flachHeitRichtung;
	protected int tankRichtung = 0, fahrRichtung = 1;

	/**
	 * 
	 * @param tankManager
	 * @param x_Koordinate
	 *            -(0-59)
	 * @param y_Koordinate
	 *            -(0-59)
	 * @param tankType
	 */
	public Tank(TankManager tankManager, int x_Koordinate, int y_Koordinate, int tankType) {
		init(tankManager, x_Koordinate, y_Koordinate, tankType);
	}

	protected void init(TankManager tankManager, int x_Koordinate, int y_Koordinate, int tankType) {
		this.id = this.hashCode();
		switch (tankType) {
		case 00:
			super.init(tankManager, x_Koordinate * tankManager.koernigkeit2, y_Koordinate
					* tankManager.koernigkeit2, 255, 0, 210, tankManager.koernigkeit2 * 2,
					tankManager.koernigkeit2 * 2, tankType);
			this.feindlich = false;
			this.geschwindigkeit = 0.00000f;
			this.feuernHaufigkeit = 0;
			this.energie = 1;
			break;
		case 01:
			super.init(tankManager, x_Koordinate * tankManager.koernigkeit2, y_Koordinate
					* tankManager.koernigkeit2, 255, 0, 0, tankManager.koernigkeit2 * 3,
					tankManager.koernigkeit2 * 3, tankType);
			this.feindlich = false;
			this.geschwindigkeit = 0.00020f;
			this.feuernHaufigkeit = 700;
			this.energie = 50;
			break;
		case 02:
			super.init(tankManager, x_Koordinate * tankManager.koernigkeit2, y_Koordinate
					* tankManager.koernigkeit2, 0, 255, 0, tankManager.koernigkeit2 * 3,
					tankManager.koernigkeit2 * 3, tankType);
			this.feindlich = false;
			this.geschwindigkeit = 0.00010f;
			this.feuernHaufigkeit = 700;
			this.energie = 100;
			break;
		case 03:
			super.init(tankManager, x_Koordinate * tankManager.koernigkeit2, y_Koordinate
					* tankManager.koernigkeit2, 0, 0, 255, tankManager.koernigkeit2 * 3,
					tankManager.koernigkeit2 * 3, tankType);
			this.feindlich = false;
			this.geschwindigkeit = 0.00015f;
			this.feuernHaufigkeit = 500;
			this.energie = 70;
			break;
		case 11:
			super.init(tankManager, x_Koordinate * tankManager.koernigkeit2, y_Koordinate
					* tankManager.koernigkeit2, 210, 10, 210, tankManager.koernigkeit2 * 3,
					tankManager.koernigkeit2 * 3, tankType);
			this.feindlich = true;
			this.geschwindigkeit = 0.00010f;
			this.feuernHaufigkeit = 900;
			this.energie = 1;
			break;
		case 12:
			super.init(tankManager, x_Koordinate * tankManager.koernigkeit2, y_Koordinate
					* tankManager.koernigkeit2, 210, 10, 210, tankManager.koernigkeit2 * 3,
					tankManager.koernigkeit2 * 3, tankType);
			this.feindlich = true;
			this.geschwindigkeit = 0.00010f;
			this.feuernHaufigkeit = 900;
			this.energie = 1;
			break;
		case 13:
			super.init(tankManager, x_Koordinate * tankManager.koernigkeit2, y_Koordinate
					* tankManager.koernigkeit2, 210, 10, 210, tankManager.koernigkeit2 * 3,
					tankManager.koernigkeit2 * 3, tankType);
			this.feindlich = true;
			this.geschwindigkeit = 0.00010f;
			this.feuernHaufigkeit = 900;
			this.energie = 1;
			break;
		case 21:
			super.init(tankManager, x_Koordinate * tankManager.koernigkeit2, y_Koordinate
					* tankManager.koernigkeit2, 210, 10, 210, tankManager.koernigkeit2 * 4,
					tankManager.koernigkeit2 * 4, tankType);
			this.feindlich = true;
			this.geschwindigkeit = 0.00005f;
			this.energie = 30;
			break;
		case 22:
			super.init(tankManager, x_Koordinate * tankManager.koernigkeit2, y_Koordinate
					* tankManager.koernigkeit2, 210, 10, 210, tankManager.koernigkeit2 * 4,
					tankManager.koernigkeit2 * 4, tankType);
			this.feindlich = true;
			this.geschwindigkeit = 0.00005f;
			this.energie = 30;
			break;
		case 23:
			super.init(tankManager, x_Koordinate * tankManager.koernigkeit2, y_Koordinate
					* tankManager.koernigkeit2, 210, 10, 210, tankManager.koernigkeit2 * 4,
					tankManager.koernigkeit2 * 4, tankType);
			this.feindlich = true;
			this.geschwindigkeit = 0.00005f;
			this.energie = 30;
			break;
		}

		currTime = System.currentTimeMillis();
		time = currTime;
		time1 = currTime;
		feuernKontroller = currTime;
		this.xf_Koordinate = x_Koordinate * tankManager.koernigkeit2 + 0.0f;
		this.yf_Koordinate = y_Koordinate * tankManager.koernigkeit2 + 0.0f;
	}

	protected void malen() {
		long elapsedTime = System.currentTimeMillis() - currTime;
		currTime += elapsedTime;
		if (tankManager.status == GameManager.PAUSE || tankManager.status == GameManager.EXIT) {
			if (feindlich) {
				time += elapsedTime;
				time1 += elapsedTime;
			}
			elapsedTime = 0;
		}

		if (feuern) {
			feuern();
		}
		if (!aktive) {
			elapsedTime = 0;
		}
		
		koordinateAktualisieren(elapsedTime);
		tankMalen();

	}

	protected void tankMalen() {
		 tankManager.maler.setColor(colorR, colorG, colorB);
		 tankManager.maler.fillOvall(x_Koordinate, y_Koordinate, objWidth,
		 objHeight);
		
		 geschuetzTurm();

		
	}

	
	private void geschuetzTurm() {
		int pW = objWidth / 10, pH = objWidth / 2 + objWidth / 6;
		switch (this.fahrRichtung) {
		case 1:
			tankManager.maler.setColor(255, 165, 0);
			tankManager.maler.fillRect(x_Koordinate + objWidth / 2 - pW / 2, y_Koordinate - pH
					+ objWidth / 2, pW, pH);
			break;
		case 3:
			tankManager.maler.setColor(255, 165, 0);
			tankManager.maler.fillRect(x_Koordinate + objWidth / 2, y_Koordinate + objWidth / 2
					- pW / 2, pH, pW);
			break;

		case 5:
			tankManager.maler.setColor(255, 165, 0);
			tankManager.maler.fillRect(x_Koordinate + objWidth / 2 - pW / 2, y_Koordinate
					+ objWidth / 2, pW, pH);
			break;

		case 7:
			tankManager.maler.setColor(255, 165, 0);
			tankManager.maler.fillRect(x_Koordinate - (pH - objWidth / 2), y_Koordinate + objWidth
					/ 2 - pW / 2, pH, pW);
			break;
		}

	}

	protected void keyPressBehandeln(int key) {
		switch (key) {
		case 38:
			keyUp = true;
			keyDown = false; /* fur vier richtung */
			keyLeft = false; /* fur vier richtung */
			keyRight = false; /* fur vier richtung */
			break;
		case 40:
			keyUp = false;
			keyDown = true;
			keyLeft = false;
			keyRight = false;
			break;
		case 37:
			keyUp = false;
			keyDown = false;
			keyLeft = true;
			keyRight = false;
			break;
		case 39:
			keyUp = false;
			keyDown = false;
			keyLeft = false;
			keyRight = true;
			break;
		case 17: /* key Ctrl */
			feuern = true;
			break;
		}
		richtungErkennen();
	}

	protected void keyReleaseBehandeln(int key) {

		switch (key) {
		case 38:
			keyUp = false;
			break;
		case 40:
			keyDown = false;
			break;
		case 37:
			keyLeft = false;
			break;
		case 39:
			keyRight = false;
			break;
		case 17: /* key Ctrl */
			feuern = false;
			break;
		}
		richtungErkennen();
	}

	protected void richtungErkennen() {
		if (keyLeft && !keyUp && !keyRight && !keyDown)
			tankRichtung = 7;
		else if (keyLeft && keyUp && !keyRight && !keyDown)
			tankRichtung = 8;
		else if (!keyLeft && keyUp && !keyRight && !keyDown)
			tankRichtung = 1;
		else if (!keyLeft && keyUp && keyRight && !keyDown)
			tankRichtung = 2;
		else if (!keyLeft && !keyUp && keyRight && !keyDown)
			tankRichtung = 3;
		else if (!keyLeft && !keyUp && keyRight && keyDown)
			tankRichtung = 4;
		else if (!keyLeft && !keyUp && !keyRight && keyDown)
			tankRichtung = 5;
		else if (keyLeft && !keyUp && !keyRight && keyDown)
			tankRichtung = 6;
		else if (!keyLeft && !keyUp && !keyRight && !keyDown)
			tankRichtung = 0;
	}

	protected void feuern() {
		if (currTime - feuernKontroller > feuernHaufigkeit) {
			this.tankManager.missilesMenge.addElement(new Missile(tankManager, x_Koordinate,
					y_Koordinate, objWidth, objHeight, fahrRichtung, missileType, feindlich, id));
			feuernKontroller = currTime;
		}

	}

	protected void koordinateAktualisieren(long elapsedTime) {
		x_Koe = x_Koordinate / tankManager.koernigkeit2;
		y_Koe = y_Koordinate / tankManager.koernigkeit2;
		int x_KoeRest = x_Koordinate % tankManager.koernigkeit2;
		int y_KoeRest = y_Koordinate % tankManager.koernigkeit2;

		if (feindlich&&elapsedTime!=0) {
			tankFeindKI();
		}

		int alteX_Koordinate, alteY_Koordinate, alteX_Koe, alteY_Koe;
		float alteXf_Koordinate, alteYf_Koordinate;
		alteX_Koordinate = x_Koordinate;
		alteY_Koordinate = y_Koordinate;
		alteXf_Koordinate = xf_Koordinate;
		alteYf_Koordinate = yf_Koordinate;
		alteX_Koe = x_Koe;
		alteY_Koe = y_Koe;

		if ((x_KoeRest != 0 || y_KoeRest != 0) && !flachHeit) {
			flachHeit = true;
			flachHeitRichtung = fahrRichtung;
		}
		if (x_KoeRest == 0 && y_KoeRest == 0) {
			flachHeit = false;
		}
		if (flachHeit) {
			switch (flachHeitRichtung) {
			case 1:
				yf_Koordinate -= geschwindigkeit * tankManager.screenWidth * elapsedTime;
				y_Koordinate = getRund(yf_Koordinate);
				if (tankRichtung == 0) {
					if (y_Koordinate / tankManager.koernigkeit2 < alteY_Koordinate
							/ tankManager.koernigkeit2) {
						y_Koordinate = y_Koe * tankManager.koernigkeit2;
					}
				}
				if (yf_Koordinate < 0) {
					yf_Koordinate = 0;
					y_Koordinate = 0;
				}
				y_Koe = y_Koordinate / tankManager.koernigkeit2;
				break;
			case 3:
				xf_Koordinate += geschwindigkeit * tankManager.screenWidth * elapsedTime;
				x_Koordinate = getRund(xf_Koordinate);
				if (tankRichtung == 0) {
					if (x_Koordinate / tankManager.koernigkeit2 > alteX_Koordinate
							/ tankManager.koernigkeit2) {
						x_Koordinate = (x_Koe + 1) * tankManager.koernigkeit2;
					}
				}
				if (x_Koordinate % tankManager.koernigkeit2 != 0) {
					x_Koe = x_Koordinate / tankManager.koernigkeit2 + 1;
				}
				break;
			case 5:
				yf_Koordinate += geschwindigkeit * tankManager.screenWidth * elapsedTime;
				y_Koordinate = getRund(yf_Koordinate);
				if (tankRichtung == 0) {
					if (y_Koordinate / tankManager.koernigkeit2 > alteY_Koordinate
							/ tankManager.koernigkeit2) {
						y_Koordinate = (y_Koe + 1) * tankManager.koernigkeit2;
					}
				}
				if (y_Koordinate % tankManager.koernigkeit2 != 0) {
					y_Koe = y_Koordinate / tankManager.koernigkeit2 + 1;
				}
				break;
			case 7:
				xf_Koordinate -= geschwindigkeit * tankManager.screenWidth * elapsedTime;

				x_Koordinate = getRund(xf_Koordinate);
				if (tankRichtung == 0) {
					if (x_Koordinate / tankManager.koernigkeit2 < alteX_Koordinate
							/ tankManager.koernigkeit2) {
						x_Koordinate = x_Koe * tankManager.koernigkeit2;
					}
				}
				if (xf_Koordinate < 0) {
					xf_Koordinate = 0;
					x_Koordinate = 0;
				}
				x_Koe = x_Koordinate / tankManager.koernigkeit2;
				break;
			}
		} else {
			switch (tankRichtung) {
			case 1:
				yf_Koordinate -= geschwindigkeit * tankManager.screenWidth * elapsedTime;
				y_Koordinate = getRund(yf_Koordinate);
				break;
			case 3:
				xf_Koordinate += geschwindigkeit * tankManager.screenWidth * elapsedTime;
				x_Koordinate = getRund(xf_Koordinate);
				break;
			case 5:
				yf_Koordinate += geschwindigkeit * tankManager.screenWidth * elapsedTime;
				y_Koordinate = getRund(yf_Koordinate);
				break;
			case 7:
				xf_Koordinate -= geschwindigkeit * tankManager.screenWidth * elapsedTime;
				x_Koordinate = getRund(xf_Koordinate);
				break;
			}
		}

		if (this.tankRichtung != 0 && !flachHeit)
			fahrRichtung = tankRichtung;

		// �߽���
		if (x_Koordinate < 0 || x_Koordinate > tankManager.screenWidth - objWidth
				|| y_Koordinate < 0 || y_Koordinate > tankManager.screenHeight - objHeight) {
			x_Koordinate = alteX_Koordinate;
			y_Koordinate = alteY_Koordinate;
			xf_Koordinate = alteXf_Koordinate;
			yf_Koordinate = alteYf_Koordinate;
		}

		if (stossenGegen(tankManager.tankMenge)) {
			x_Koordinate = alteX_Koe * tankManager.koernigkeit2;
			y_Koordinate = alteY_Koe * tankManager.koernigkeit2;
			xf_Koordinate = alteXf_Koordinate;
			yf_Koordinate = alteYf_Koordinate;

		}
		if (stossenGegen(tankManager.brickwall)) {
			x_Koordinate = alteX_Koordinate;
			y_Koordinate = alteY_Koordinate;
			xf_Koordinate = alteXf_Koordinate;
			yf_Koordinate = alteYf_Koordinate;
		}
		// if (stossenGegen(tankManager.grasswall)) {
		// x_Koordinate = alteX_Koordinate;
		// y_Koordinate = alteY_Koordinate;
		// }
		if (stossenGegen(tankManager.metalwall)) {
			x_Koordinate = alteX_Koordinate;
			y_Koordinate = alteY_Koordinate;
			xf_Koordinate = alteXf_Koordinate;
			yf_Koordinate = alteYf_Koordinate;
		}
		if (stossenGegen(tankManager.waterwall)) {
			x_Koordinate = alteX_Koordinate;
			y_Koordinate = alteY_Koordinate;
			xf_Koordinate = alteXf_Koordinate;
			yf_Koordinate = alteYf_Koordinate;
		}

	}

	protected void tankFeindKI() {
	
	}

	public void beschaedigen(int besch, int beschId) {
		this.beschId = beschId;
		this.energie = this.energie - besch;
			if (this.energie <= 0) {
				explodieren();
			}
	}
	
	protected void explodieren() {
		tankManager.tankMenge.removeElement(this);
		if (tankManager.tank1 != null && this.id == tankManager.tank1.id) {
			tankManager.tank1 = null;
		}
	}
	
	protected void toolBehandeln(int toolType) {
	
	}
	
}
