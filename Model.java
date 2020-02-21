package de.cau.infprogoo.mvc;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Arrays;

import javax.imageio.ImageIO;

import acm.graphics.*;
import acm.program.GraphicsProgram;
import acm.util.RandomGenerator;

public class Model extends GraphicsProgram {

	private static final int[][] BOARD = new int[7][7];
	private int scale = 100;
	private int player = 1;
	private boolean placed = false;
	public GOval stone;
	private LighthouseView lv = new LighthouseView();
	private int offset = 3;
	GImage replay;
	GImage menu;
	boolean replaySelect = true;

	public void createBoard() {
		GRect bg = new GRect(0, 0, 710, scale);
		bg.setFilled(true);
		bg.setFillColor(Color.WHITE);
		bg.setColor(new Color(0, 0, 0, 1));
		add(bg);
		GRect board = new GRect(0, scale, 710, 710 - scale);
		board.setFilled(true);
		board.setFillColor(Color.BLUE);
		board.setColor(new Color(0, 0, 0, 1));
		add(board);
		for (int i = 0; i < 7; i++) {
			for (int j = 1; j < 7; j++) {
				GOval w = new GOval(i * scale + 10, j * scale + 10, scale - 10, scale - 10);
				w.setFilled(true);
				w.setFillColor(Color.WHITE);
				w.setColor(new Color(0, 0, 0, 1));
				add(w);
			}
		}
	}

	private int piece(int x, int y) {
		if (x < 1 || y < 0 || x >= 7 || y >= 7) {
			return 0;
		}
		return BOARD[x][y];
	}

	public int winConditions() {
		for (int i = 1; i < 7; i++)
			for (int j = 0; j < 7; j++) {
				// Checks rows
				if (piece(i, j) != 0 && piece(i, j) == piece(i, j + 1) && piece(i, j) == piece(i, j + 2)
						&& piece(i, j) == piece(i, j + 3))
					return piece(i, j);
				// Checks columns
				else if (piece(i, j) != 0 && piece(i, j) == piece(i + 1, j) && piece(i, j) == piece(i + 2, j)
						&& piece(i, j) == piece(i + 3, j))
					return piece(i, j);
				// Checks diagonal high to low
				else if (piece(i, j) != 0 && piece(i, j) == piece(i + 1, j + 1) && piece(i, j) == piece(i + 2, j + 2)
						&& piece(i, j) == piece(i + 3, j + 3))
					return piece(i, j);
				// Checks diagonal low to high
				else if (piece(i, j) != 0 && piece(i, j) == piece(i - 1, j + 1) && piece(i, j) == piece(i - 2, j + 2)
						&& piece(i, j) == piece(i - 3, j + 3))
					return piece(i, j);
			}
		return -1;
	}

	// ------------KEYEVENTS---------------------------------------------------------------------
	public void keyPressed(KeyEvent e) {
		// FOR GAMEPLAY
		if ((player == 1 && e.getKeyCode() == KeyEvent.VK_A) || (player == 2 && e.getKeyCode() == KeyEvent.VK_LEFT))
			if (stone.getX() > 0 && offset > 0) {
				stone.move(-scale, 0);
				lv.resetFirstRow();
				offset -= 1;
				lv.setPiece(offset, 0, player);
			}
		if ((player == 1 && e.getKeyCode() == KeyEvent.VK_D) || (player == 2 && e.getKeyCode() == KeyEvent.VK_RIGHT))
			if (stone.getX() < 6 * scale && offset < 7) {
				stone.move(scale, 0);
				lv.resetFirstRow();
				offset += 1;
				lv.setPiece(offset, 0, player);
			}
		if ((player == 1 && e.getKeyCode() == KeyEvent.VK_S) || (player == 2 && e.getKeyCode() == KeyEvent.VK_DOWN))
			if (findRightRow((int) stone.getX() / scale) != -1) {
				placed = true;
			}
		// FOR WIN-SCREEN
		if (winConditions() > 0 && replaySelect == true
				&& (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S)) {

			remove(replay);
			replay = new GImage("replay_white.png");
			replay.scale(0.7, 0.7);
			add(replay, (710 - replay.getWidth()) / 2, 425);

			remove(menu);
			if (winConditions() == 1) {
				menu = new GImage("menu_red.png");
				lv.imageReader("winscreen_p1_menu.png");
			} else if (winConditions() == 2) {
				menu = new GImage("menu_yellow.png");
				lv.imageReader("winscreen_p2_menu.png");
			}
			menu.scale(0.7, 0.7);
			add(menu, (710 - menu.getWidth()) / 2, 550);
			replaySelect = false;
		}
		if (winConditions() > 0 && replaySelect == false
				&& (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W)) {

			remove(menu);
			menu = new GImage("menu_white.png");
			menu.scale(0.7, 0.7);
			add(menu, (710 - menu.getWidth()) / 2, 550);

			remove(replay);
			if (winConditions() == 1) {
				replay = new GImage("replay_red.png");
				lv.imageReader("winscreen_p1_replay.png");
			} else if (winConditions() == 2) {
				replay = new GImage("replay_yello.png");
				lv.imageReader("winscreen_p2_replay.png");
			}

			replay.scale(0.7, 0.7);
			add(replay, (710 - replay.getWidth()) / 2, 425);
			replaySelect = true;
		}
		if (winConditions() > 0 && e.getKeyCode() == KeyEvent.VK_ENTER && replaySelect) {
			removeAll();
			player = 1;
			placed = false;
			for (int i = 0; i < 7; i++)
				for (int j = 0; j < 7; j++)
					BOARD[i][j] = 0;
			createBoard();
		}
	}
	// --------------------------------------------------------------------------------------------

	public int findRightRow(int col) {
		for (int row = 6; row >= 1; row--)
			if (BOARD[row][col] == 0) {
				return row;
			}
		return -1;
	}

	public void placeStone() {
		stone = new GOval(3 * scale + 10, 0, scale - 10, scale - 10);

		for (int col = 0; col < 7; col++)
			BOARD[0][col] = -1;

		if (player == 1) {
			stone.setFillColor(Color.RED);
		} else if (player == 2) {
			stone.setFillColor(Color.YELLOW);
		}
		stone.setColor(new Color(0, 0, 0, 1));
		stone.setFilled(true);
		add(stone);
		lv.setPiece(offset, 0, player);

		while (!placed) {
			pause(500);
			stone.setVisible(false);
			lv.setInvisible(offset, 0);
			pause(500);
			stone.setVisible(true);
			lv.setPiece(offset, 0, player);
		}

		int column = (int) stone.getX() / scale;
		int rightRow = findRightRow(column);

		if (rightRow > 0)
			BOARD[rightRow][column] = player;

		stone.move(0, rightRow * 100 + 10);

		for (int i = 0; i < rightRow; i++) {
			lv.setPiece(offset, i + 1, player);
			lv.setInvisible(offset, i);
			pause(60);
		}
		offset = 3;
	}

	public void gameRound() {
		createBoard();
		lv.setBackground();
		int counter = 0;
		while (winConditions() == -1) {
			counter += 1;
			placeStone();
			if (player == 1) {
				player = 2;
			} else {
				player = 1;
			}
			placed = false;
		}
		if (winConditions() > 0) {
			lv.winScreen(winConditions());
			winScreen(winConditions());
		}
		if (counter == 42) {
			println("It's a draw");
		}
	}

	public void winScreen(int player) {
		GRect block = new GRect(0, 0, 710, 710);
		block.setFilled(true);
		block.setFillColor(Color.BLACK);
		add(block);

		GImage winner = new GImage("winner.png");
		add(winner, (710 - winner.getWidth()) / 2, 50);

		if (player == 1) {
			GImage p1 = new GImage("p1.png");
			add(p1, (710 - p1.getWidth()) / 2, 200);
			replay = new GImage("replay_red.png");
			replay.scale(0.7, 0.7);
		} else if (player == 2) {
			GImage p1 = new GImage("p2.png");
			add(p1, (710 - p1.getWidth()) / 2, 200);
			replay = new GImage("replay_yello.png");
			replay.scale(0.7, 0.7);
		}
		add(replay, (710 - replay.getWidth()) / 2, 425);

		menu = new GImage("menu_white.png");
		menu.scale(0.7, 0.7);
		add(menu, (710 - menu.getWidth()) / 2, 550);
	}

	public void run() {
		setSize(800, 800);
		pause(10);
		addKeyListeners();
		gameRound();
		//lv.end();
	}

	public static void main(String[] args) {
		new Model().start();
	}
}
