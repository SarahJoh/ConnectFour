package de.cau.infprogoo.game;

import java.awt.Color;
import java.awt.Image;
import javax.swing.ImageIcon;
import acm.graphics.*;

public class MonitorView extends GCompound implements View {

	private int boardSize = 700;
	private Model model;
	private LighthouseView lView;
	private int stoneSize;
	private GOval stone;
	GImage replay;
	GImage menu;
	boolean enter = false;

	public MonitorView(Model model, LighthouseView lView) {
		this.model = model;
		this.lView = lView;
		stoneSize = model.getScale();
	}

	public void setBackground() {
		GRect emptySpace = new GRect(0, 0, boardSize + 10, stoneSize);
		emptySpace.setFilled(true);
		emptySpace.setFillColor(Color.WHITE);
		emptySpace.setColor(new Color(0, 0, 0, 1));
		add(emptySpace);
		GRect board = new GRect(0, stoneSize, boardSize + 10, boardSize + 20 - stoneSize);
		board.setFilled(true);
		board.setFillColor(Color.BLUE);
		board.setColor(new Color(0, 0, 0, 1));
		add(board);
		for (int i = 0; i < 7; i++) {
			for (int j = 1; j < 7; j++) {
				GOval w = new GOval(i * stoneSize + 10, j * stoneSize + 10, stoneSize - 10, stoneSize - 10);
				w.setFilled(true);
				w.setFillColor(new Color(0, 0, 150));
				w.setColor(new Color(0, 0, 0, 1));
				add(w);
			}
		}
	}

	public void placeStone() {
		stone = new GOval(model.getOffset() * stoneSize + 10, 0, stoneSize - 10, stoneSize - 10);

		if (model.getPlayer() == 1) {
			stone.setFillColor(Color.RED);
		} else if (model.getPlayer() == 2) {
			stone.setFillColor(Color.YELLOW);
		}
		stone.setColor(new Color(0, 0, 0, 1));
		stone.setFilled(true);
		add(stone);
		lView.setStone(model.getOffset(), 0, model.getPlayer());

		while (model.getPlaced() == false) {
			pause(500);
			stone.setVisible(false);
			lView.setInvisible(model.getOffset(), 0);
			pause(500);
			stone.setVisible(true);
			lView.setStone(model.getOffset(), 0, model.getPlayer());
		}

		int column = (int) stone.getX() / stoneSize;
		int rightRow = model.getRightRow(column);

		if (rightRow > 0)
			model.setStone(rightRow, column, model.getPlayer());

		stone.move(0, rightRow * stoneSize + 10);

		for (int i = 0; i < rightRow; i++) {
			lView.setStone(model.getOffset(), i + 1, model.getPlayer());
			lView.setInvisible(model.getOffset(), i);
			pause(60);
		}
	}

	public void winScreen(int winner) {
		removeAll();
		
		GRect block = new GRect(0, 0, boardSize + 10, boardSize + 10);
		block.setFilled(true);
		block.setFillColor(Color.BLACK);
		add(block);

		// WORK IN PROGRESS; HAVE TO USE MULTITHREADING
		/*
		 * while (!enter) { firework(winner); }
		 */

		add(placeImage("winner.png", 1.0, 50));

		if (winner == 1) {
			add(placeImage("p1.png", 1.0, 200));
			replay = placeImage("replay_red.png", 0.7, 425);
			add(replay);
		} else if (winner == 2) {
			add(placeImage("p2.png", 1.0, 200));
			replay = placeImage("replay_yello.png", 0.7, 425);
			add(replay);
		}
		menu = placeImage("menu_white.png", 0.7, 550);
		add(menu);
	}

	public void gameRound() {
		setBackground();
		lView.setBackground();
		model.setFirstRow();
		int counter = 0;
		while (model.getWinner() == -1) {
			counter += 1;
			placeStone();
			model.setOffset(3);
			model.changePlayer();
			model.setPlaced(false);
		}
		if (model.getWinner() > 0) {
			model.setSelected(1);
			winScreen(model.getWinner());
			lView.winScreen(model.getWinner());
		}
		if (counter == 42) {
			System.out.println("It's a draw");
		}
	}

	// ONLY WORKS WITH THESE URLS AT THE MOMENT; NEED TO IMPLEMENT RESOURCE SCANNER
	public void firework(int winner) {
		Model model = new Model();

		int size = model.getRandomNum(50, 300);
		int x = model.getRandomNum(0, 400);
		int y = model.getRandomNum(0, 400);

		if (winner == 1) {
			String url = new String("D:\\_coding\\Workspaces\\ConnectFour\\resources\\firework_yellow.gif");
			Image icon = new ImageIcon(url).getImage();
			GImage img = new GImage(icon, x, y);
			img.setSize(size, size);
			add(img);
			pause(1000);
			remove(img);
		} else if (winner == 2) {
			String url = new String("D:\\_coding\\Workspaces\\ConnectFour\\resources\\firework_red.gif");
			Image icon = new ImageIcon(url).getImage();
			GImage img = new GImage(icon, x, y);
			img.setSize(size, size);
			add(img);
			pause(1000);
			remove(img);
		}
	}

	public GImage placeImage(String source, double scale, int y) {
		GImage img = new GImage(source);
		img.scale(scale, scale);
		img.setLocation(((boardSize + 10) - img.getWidth()) / 2, y);
		return img;
	}

	@Override
	public void updateStone(Model world) {
		stone.setLocation(model.getStoneX(), model.getStoneY());
	}

	public void repeat() {
		enter = false;
		pause(5000);
		gameRound();

	}
}
