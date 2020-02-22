package de.cau.infprogoo.game;

import acm.program.GraphicsProgram;

public class ConnectFour extends GraphicsProgram {

	@Override
	public void init() {

		setSize(725, 750);
		pause(10);

		Model model = new Model();
		
		LighthouseView lView = new LighthouseView();
		
		MonitorView mView = new MonitorView(model, lView);
		add(mView);
		
		Controller cntrl = new Controller(model, mView, lView);
		addKeyListeners(cntrl);

		mView.gameRound();

		// TO DO: PREVENT GAME CRASH WHEN CHOOSING REPLAY
		/*
		if (model.getSelected() == 1 && mView.enter) {
			remove(mView);
			mView = new MonitorView(model, lView);
			add(mView);
			
			mView.gameRound();
		}*/
	}

	public static void main(String[] args) {
		new ConnectFour().start();
	}
}
