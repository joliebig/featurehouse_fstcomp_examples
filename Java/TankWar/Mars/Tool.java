
public class Tool {

	protected void init(TankManager tankManager, int x_Koordinate, int y_Koordinate, int toolType) {
		original(tankManager, x_Koordinate, y_Koordinate, toolType);
		switch (toolType) {
		case 375:
			original(tankManager, x_Koordinate * tankManager.koernigkeit2, y_Koordinate
					* tankManager.koernigkeit2, 255, 255, 255, tankManager.koernigkeit,
					tankManager.koernigkeit, toolType);
			break;
		}
	}

}