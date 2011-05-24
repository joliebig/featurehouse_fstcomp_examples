
import java.io.*;
import sun.audio.AudioPlayer;

public class SoundPlayer {
	private static SoundPlayer instance;
	private InputStream inputStream = null;

	static SoundPlayer getInstance() {
		if (instance == null) {
			instance = new SoundPlayer();
		}
		return instance;
	}

	public void playBgSound() throws IOException {
		inputStream = Tank.class.getClassLoader().getResourceAsStream("bg.wav");
		AudioPlayer.player.start(inputStream);
	}

	public void playFireSound() throws IOException {
		inputStream = Tank.class.getClassLoader().getResourceAsStream(
				"fire.wav");
		AudioPlayer.player.start(inputStream);
	}

	public void playExplodeSound() throws IOException {
		inputStream = Tank.class.getClassLoader().getResourceAsStream(
				"explode.wav");
		AudioPlayer.player.start(inputStream);
	}

	public void playMoveSound() throws IOException {
		inputStream = Tank.class.getClassLoader().getResourceAsStream(
				"move.wav");
		AudioPlayer.player.start(inputStream);
	}

	public void playRogerSound() throws IOException {
		inputStream = Tank.class.getClassLoader().getResourceAsStream(
				"roger.wav");
		AudioPlayer.player.start(inputStream);
	}

	public void playYessirSound() throws IOException {
		inputStream = Tank.class.getClassLoader().getResourceAsStream(
				"yessir.wav");
		AudioPlayer.player.start(inputStream);
	}

	public void playYeahSound() throws IOException {
		inputStream = Tank.class.getClassLoader().getResourceAsStream(
				"yeah.wav");
		AudioPlayer.player.start(inputStream);
	}

	public void playEatSound() throws IOException {
		inputStream = Tank.class.getClassLoader()
				.getResourceAsStream("eat.wav");
		AudioPlayer.player.start(inputStream);
	}

	public void stopSound() {
		AudioPlayer.player.stop(inputStream);
	}

}