package com.ben.tetric.listener;

import com.ben.tetric.entities.Ground;

public interface GroundListener {
    void beforeDeleteFullLine(Ground ground, int lineNum);


    void fullLineDeleted(Ground ground, int deleteLineCount);

    void groundIsFull();
}
