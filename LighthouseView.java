package de.cau.infprogoo.mvc;

import java.io.IOException;

import acm.graphics.GImage;
import de.cau.infprogoo.lighthouse.LighthouseDisplay;

public class LighthouseView {

	private LighthouseDisplay display;
	private static final byte[] BG1 = { (byte) 255, (byte) 255, (byte) 255 };
	private static final byte[] BG2 = { 0, 0, (byte) 255 };
	private static final byte[] PLAYER1_COLOR = { (byte) 255, 0, 0 };
	private static final byte[] PLAYER2_COLOR = { (byte) 255, (byte) 255, 0 };
	private final byte[] data = new byte[14 * 28 * 3];

	public LighthouseView() {
		try {
			display = LighthouseDisplay.getDisplay();
			display.setUsername("stu222507");
			display.setToken("API-TOK_yC3N-Y+xD-INr2-phnH-NvZC");
		} catch (Exception e) {
			System.out.println("Connection failed: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void setBackground() {
		try {
			for (int rows = 0; rows < 2; rows++) {
				for (int cols = 0; cols < 28; cols++) {
					data[rows * 28 * 3 + cols * 3] = BG1[0];
					data[rows * 28 * 3 + cols * 3 + 1] = BG1[1];
					data[rows * 28 * 3 + cols * 3 + 2] = BG1[2];
				}
			}
			for (int rows = 2; rows < 14; rows++) {
				for (int cols = 0; cols < 28; cols++) {
					data[rows * 28 * 3 + cols * 3] = BG2[0];
					data[rows * 28 * 3 + cols * 3 + 1] = BG2[1];
					data[rows * 28 * 3 + cols * 3 + 2] = BG2[2];
				}
			}
			display.sendImage(data);
		} catch (IOException e) {
			System.out.println("Connection failed: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void resetFirstRow() {
		try {
			for (int rows = 0; rows < 2; rows++) {
				for (int cols = 0; cols < 28; cols++) {
					data[rows * 28 * 3 + cols * 3] = BG1[0];
					data[rows * 28 * 3 + cols * 3 + 1] = BG1[1];
					data[rows * 28 * 3 + cols * 3 + 2] = BG1[2];
				}
			}
			display.sendImage(data);
		} catch (IOException e) {
			System.out.println("Connection failed: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void setPiece(int column, int row, int player) {

		int offset = 2 * row * 28 * 3 + 4 * column * 3;

		try {
			if (player == 1) {
				for (int rows = 0; rows < 2; rows++) {
					for (int cols = 0; cols < 4; cols++) {
						data[rows * 28 * 3 + cols * 3 + offset] = PLAYER1_COLOR[0];
						data[rows * 28 * 3 + cols * 3 + offset + 1] = PLAYER1_COLOR[1];
						data[rows * 28 * 3 + cols * 3 + offset + 2] = PLAYER1_COLOR[2];
					}
				}
			} else {
				for (int rows = 0; rows < 2; rows++) {
					for (int cols = 0; cols < 4; cols++) {
						data[rows * 28 * 3 + cols * 3 + offset] = PLAYER2_COLOR[0];
						data[rows * 28 * 3 + cols * 3 + offset + 1] = PLAYER2_COLOR[1];
						data[rows * 28 * 3 + cols * 3 + offset + 2] = PLAYER2_COLOR[2];
					}
				}
			}
			display.sendImage(data);
		} catch (IOException e) {
			System.out.println("Connection failed: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void setInvisible(int column, int row) {
		int offset = 2 * row * 28 * 3 + 4 * column * 3;

		try {
			if (row == 0) {
				for (int rows = 0; rows < 2; rows++) {
					for (int cols = 0; cols < 4; cols++) {
						data[rows * 28 * 3 + cols * 3 + offset] = BG1[0];
						data[rows * 28 * 3 + cols * 3 + offset + 1] = BG1[1];
						data[rows * 28 * 3 + cols * 3 + offset + 2] = BG1[2];
					}
				}
			} else {
				for (int rows = 0; rows < 2; rows++) {
					for (int cols = 0; cols < 4; cols++) {
						data[rows * 28 * 3 + cols * 3 + offset] = BG2[0];
						data[rows * 28 * 3 + cols * 3 + offset + 1] = BG2[1];
						data[rows * 28 * 3 + cols * 3 + offset + 2] = BG2[2];
					}
				}
			}
			display.sendImage(data);
		} catch (IOException e) {
			System.out.println("Connection failed: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void winScreen(int player) {
		try {
			if (player == 1)
				imageReader("winscreen_p1_replay.png");
			if (player == 2)
				imageReader("winscreen_p2_replay.png");

			display.sendImage(data);
		} catch (IOException e) {
			System.out.println("Connection failed: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void imageReader(String image) {
		try {
			GImage img = new GImage(image);
			int[][] pixels = img.getPixelArray();

			for (int rows = 0; rows < pixels.length; rows++) {
				for (int cols = 0; cols < pixels[rows].length; cols++) {

					int red = GImage.getRed(pixels[rows][cols]);
					int green = GImage.getGreen(pixels[rows][cols]);
					int blue = GImage.getBlue(pixels[rows][cols]);

					data[rows * 28 * 3 + cols * 3] = (byte) red;
					data[rows * 28 * 3 + cols * 3 + 1] = (byte) green;
					data[rows * 28 * 3 + cols * 3 + 2] = (byte) blue;
				}
			}
			display.sendImage(data);
		} catch (IOException e) {
			System.out.println("Connection failed: " + e.getMessage());
			e.printStackTrace();
		}

	}

	public void end() {
		display.close();
	}
}
