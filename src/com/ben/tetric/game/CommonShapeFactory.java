package com.ben.tetric.game;


import com.ben.tetric.entities.Shape;
import com.ben.tetric.entities.ShapeFactory;
import com.ben.tetric.listener.ShapeListener;
import com.ben.tetric.util.Global;

import java.awt.Color;

public class CommonShapeFactory extends ShapeFactory {

//	@Override
//	public Shape getShape(ShapeListener shapeListener) {
//		int type = random.nextInt(shapes.length);
//		int status = random.nextInt(shapes[type].length);
//		Shape shape = new Shape(shapes[type], status);
//		// System.out.println(type+"\t" + status);
//		shape.setColor(isColorfulShape() ? getColorByType(type)
//				: getDefaultShapeColor());
//		shape.addShapeListener(shapeListener);
//		return shape;
//	}
//
//	private Color getColorByType(int type) {
//		if (type < 0 || type >= Global.COMMON_COLORS.size())
//			return getDefaultShapeColor();
//		return Global.COMMON_COLORS.get(type);
//	}
}
