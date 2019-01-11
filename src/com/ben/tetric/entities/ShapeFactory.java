package com.ben.tetric.entities;

import com.ben.tetric.listener.ShapeListener;
import com.ben.tetric.util.Global;

import java.awt.*;
import java.util.Random;

public class ShapeFactory {
    protected static int shapes[][][] = new int[][][] {
            /* 第一种 */{ /** ***** */
            { 1, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 } },

            /* 第二种 */
            { /** ********* */
                    { 1, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0 },

                    { 0, 1, 0, 0, 0, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0 },

                    { 1, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0 },

                    { 1, 1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0 } },
            /* 第三种 */
            { /** ******* */
                    { 1, 1, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },

                    { 1, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0 },

                    { 0, 0, 1, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0 },

                    { 1, 0, 0, 0, 1, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0 } },
            /* 第四种 */
            { /** ******** */
                    { 1, 1, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0 },

                    { 0, 1, 0, 0, 1, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, } },
            /* 第五种 */
            { /** ******** */
                    { 0, 1, 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },

                    { 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0 } },
            /* 第六种 */
            { /** *********** */
                    { 0, 1, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0 },

                    { 1, 0, 0, 0, 1, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0 },

                    { 1, 1, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },

                    { 0, 1, 0, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0 } },

            /* 第七种 */
            { /** ********** */
                    { 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },

                    { 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0 } },

    };

    protected Random random = new Random();
    /**
     * 图形的默认颜色
     */
    public static final Color DEFAULT_SHAPE_COLOR = new Color(0x990066);
    /**
     * 生产的图形的颜色
     */
    protected Color defaultShapeColor = DEFAULT_SHAPE_COLOR;

    /**
     * 是否产生彩色图形
     */
    protected boolean colorfulShape;


    public Shape getShape(ShapeListener shapeListener) {
        int type = random.nextInt(shapes.length);
        Shape shape = new Shape(shapes[type], random.nextInt(shapes[type].length));
        shape.setColor(colorfulShape ? Global.getRandomColor() : defaultShapeColor);
        shape.addShapeListener(shapeListener);
        return shape;
    }

    public Color getDefaultShapeColor() {
        return defaultShapeColor;
    }

    public void setDefaultShapeColor(Color defaultShapeColor) {
        this.defaultShapeColor = defaultShapeColor;
    }

    public boolean isColorfulShape() {
        return colorfulShape;
    }

    public void setColorfulShape(boolean colorfulShape) {
        this.colorfulShape = colorfulShape;
    }
}
