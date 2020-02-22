package de.cau.infprogoo.game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Controller implements KeyListener {

	Model model;
	MonitorView mView;
	LighthouseView lView;

	public Controller(Model model, MonitorView mView, LighthouseView lView) {
		this.model = model;
		this.mView = mView;
		this.lView = lView;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// GAMEPLAY
		if (model.getWinner() < 0 && (model.getPlayer() == 1 && e.getKeyCode() == KeyEvent.VK_A)
				|| (model.getPlayer() == 2 && e.getKeyCode() == KeyEvent.VK_LEFT))
			if (model.getStoneX() > 0 && model.getOffset() > 0) {

				model.setStoneX(model.getOffset() * model.getScale() - model.getScale() + 10);
				model.setOffset(model.getOffset() - 1);
				mView.updateStone(model);
				lView.resetFirstRow();
				lView.setStone(model.getOffset(), 0, model.getPlayer());
			}
		if (model.getWinner() < 0 && (model.getPlayer() == 1 && e.getKeyCode() == KeyEvent.VK_D)
				|| (model.getPlayer() == 2 && e.getKeyCode() == KeyEvent.VK_RIGHT))
			if (model.getStoneX() < 6 * model.getScale() && model.getOffset() < 7) {

				model.setStoneX(model.getOffset() * model.getScale() + model.getScale() + 10);
				model.setOffset(model.getOffset() + 1);
				mView.updateStone(model);

				lView.resetFirstRow();
				lView.setStone(model.getOffset(), 0, model.getPlayer());
			}
		if (model.getWinner() < 0 && (model.getPlayer() == 1 && e.getKeyCode() == KeyEvent.VK_S)
				|| (model.getPlayer() == 2 && e.getKeyCode() == KeyEvent.VK_DOWN))
			if (model.getRightRow(model.getColumn()) != -1) {
				model.setPlaced(true);
				model.setStoneX(model.getOffset() * model.getScale() + 10);
				mView.updateStone(model);
			}

		// WINSCREEN
		// DOWN & S
		if (model.getWinner() > 0 && model.getSelected() == 1
				&& (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S)) {

			mView.remove(mView.replay);
			mView.replay = mView.placeImage("replay_white.png", 0.7, 425);
			mView.add(mView.replay);

			mView.remove(mView.menu);
			if (model.getWinner() == 1) {
				mView.menu = mView.placeImage("menu_red.png", 0.7, 550);
				lView.imageReader("winscreen_p1_menu.png");
				mView.add(mView.menu);
			} else if (model.getWinner() == 2) {
				mView.menu = mView.placeImage("menu_yellow.png", 0.7, 550);
				lView.imageReader("winscreen_p2_menu.png");
				mView.add(mView.menu);
			}
			model.setSelected(2);
		}
		// UP & W
		if (model.getWinner() > 0 && model.getSelected() == 2
				&& (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W)) {

			mView.remove(mView.menu);
			mView.menu = mView.placeImage("menu_white.png", 0.7, 550);
			mView.add(mView.menu);

			mView.remove(mView.replay);
			if (model.getWinner() == 1) {
				mView.replay = mView.placeImage("replay_red.png", 0.7, 425);
				lView.imageReader("winscreen_p1_replay.png");
				mView.add(mView.replay);
			} else if (model.getWinner() == 2) {
				mView.replay = mView.placeImage("replay_yello.png", 0.7, 425);
				lView.imageReader("winscreen_p2_replay.png");
				mView.add(mView.replay);
			}
			model.setSelected(1);
		}
		// ENTER
		if (model.getWinner() > 0 && e.getKeyCode() == KeyEvent.VK_ENTER && model.getSelected() == 1) {
			mView.enter = true;
			model.resetAll();
			mView.removeAll();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}
}
