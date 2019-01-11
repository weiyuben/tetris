package com.ben.tetric.game;


import com.ben.tetric.entities.Ground;
import com.ben.tetric.listener.GroundAdapter;
import com.ben.tetric.util.Global;

public class MyGroundListener extends GroundAdapter {

	int deletedLineCount = 0;

	@Override
	public void fullLineDeleted(Ground ground, int deletedLineCount) {

		this.deletedLineCount += deletedLineCount;
		if ((deletedLineCount %= 10) == 9 || deletedLineCount > 2)
			for (int y = 0; y < Global.HEIGHT; y++)
				for (int x = 0; x < Global.WIDTH; x++)
					if (ground.isStubbornObstacle(x, y))
						ground.addObstacle(x, y);
	}

}
