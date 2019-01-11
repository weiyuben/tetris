package com.ben.tetric.listener;

import com.ben.tetric.entities.Shape;

public interface ShapeListener {
    boolean isShapeMoveDownable(Shape shape);

    void shapeMovedDown(Shape shape);
}
