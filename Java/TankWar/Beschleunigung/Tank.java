
public class Tank {

	protected long beschleunigungTimer;
	protected boolean beschleunigung = false;
	
	protected void toolKontroller(){
		original();
		if (tankManager.status == GameManager.PAUSE || tankManager.status == GameManager.EXIT) {
			if (beschleunigung) {
				beschleunigungTimer += elapsedTime;
			}
		}
		if (beschleunigung && System.currentTimeMillis() - beschleunigungTimer > 10000) {
			geschwindigkeit -=0.00010f;
			beschleunigung = false;
		}
	}
	
	

	protected void toolBehandeln(int toolType) {
		original(toolType);
		switch (toolType) {
		case 370:// 30,144,255
			if (!beschleunigung) {
				this.beschleunigungTimer = System.currentTimeMillis();
				this.beschleunigung = true;
				this.geschwindigkeit += 0.00010f;
			}
			break;
		}
	}

}