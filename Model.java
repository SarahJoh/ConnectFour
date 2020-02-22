package de.cau.infprogoo.game;

import java.util.HashSet;
import java.util.Set;
import acm.util.RandomGenerator;
import de.cau.infprogoo.game.View;

public class Model {

	private static final int[][] BOARD = new int[7][7];
	private boolean placed = false;
	private int selected = 0;
	private int player = 1;
	private int offset = 3;
	private int scale = 100;
	private int stoneX = offset * scale + 10;
	private int stoneY = 0;
	RandomGenerator rgen = new RandomGenerator();

	private final Set<View> views = new HashSet<>();

	public void resetAll() {
		for (int i = 1; i < 7; i++) {
			for (int j = 0; j < 7; j++) {
				setStone(i, j, 0);
			}
		}
		setSelected(0);
		setPlayer(1);
		setOffset(3);
		setPlaced(false);
	}

	/** Marks the first row as invalid by initializing its array cells with -1. */
	public void setFirstRow() {
		for (int col = 0; col < 7; col++)
			setStone(0, col, -1);
	}

	public int getRightRow(int col) {
		for (int row = 6; row >= 1; row--)
			if (getStone(row, col) == 0) {
				return row;
			}
		return -1;
	}

	public int getColumn() {
		int col = getStoneX() / getScale();
		return col;
	}

	public int getStone(int row, int col) {
		if (row < 1 || col < 0 || row > 6 || col > 6)
			return -1;
		return BOARD[row][col];
	}

	public void setStone(int x, int y, int value) {
		BOARD[x][y] = value;
	}

	// GETTERS & SETTERS

	public int getWinner() {
		for (int i = 1; i < 7; i++)
			for (int j = 0; j < 7; j++) {
				// Checks rows
				if (getStone(i, j) != 0 && getStone(i, j) == getStone(i, j + 1) && getStone(i, j) == getStone(i, j + 2)
						&& getStone(i, j) == getStone(i, j + 3))
					return getStone(i, j);
				// Checks columns
				else if (getStone(i, j) != 0 && getStone(i, j) == getStone(i + 1, j)
						&& getStone(i, j) == getStone(i + 2, j) && getStone(i, j) == getStone(i + 3, j))
					return getStone(i, j);
				// Checks diagonal high to low
				else if (getStone(i, j) != 0 && getStone(i, j) == getStone(i + 1, j + 1)
						&& getStone(i, j) == getStone(i + 2, j + 2) && getStone(i, j) == getStone(i + 3, j + 3))
					return getStone(i, j);
				// Checks diagonal low to high
				else if (getStone(i, j) != 0 && getStone(i, j) == getStone(i - 1, j + 1)
						&& getStone(i, j) == getStone(i - 2, j + 2) && getStone(i, j) == getStone(i - 3, j + 3))
					return getStone(i, j);
			}
		return -1;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public boolean getPlaced() {
		return placed;
	}

	public void setPlaced(boolean placed) {
		this.placed = placed;
	}

	public int getScale() {
		return scale;
	}

	public void setScale(int scale) {
		this.scale = scale;
	}

	public int getPlayer() {
		return player;
	}

	public void setPlayer(int player) {
		this.player = player;
	}

	public void changePlayer() {
		if (getPlayer() == 1)
			setPlayer(2);
		else
			setPlayer(1);
	}

	public int getSelected() {
		return selected;
	}

	public void setSelected(int selected) {
		this.selected = selected;
	}

	public int getStoneX() {
		return stoneX;
	}

	public void setStoneX(int stoneX) {
		this.stoneX = stoneX;
		updateViews();
	}

	public int getStoneY() {
		return stoneY;
	}

	public void setStoneY(int stoneY) {
		this.stoneY = stoneY;
		updateViews();
	}

	public void registerView(View view) {
		views.add(view);
		view.updateStone(this);
	}

	private void updateViews() {
		for (View view : views) {
			view.updateStone(this);
		}
	}

	public int getRandomNum(int start, int end) {
		int num = rgen.nextInt(start, end);
		return num;
	}
}
