package com.ben.tetric.entities;

import com.ben.tetric.listener.ShapeListener;
import com.ben.tetric.util.Global;

import java.awt.*;
import java.util.TooManyListenersException;

public class Shape {
    /**
     * 变形(旋转)
     */
    public static final int ROTATE = 5;
    /**
     * 上移
     */
    public static final int UP = 1;
    /**
     * 下落
     */
    public static final int DOWN = 2;
    /**
     * 左移
     */
    public static final int LEFT = 3;
    /**
     * 右移
     */
    public static final int RIGHT = 4;

    /**
     * 监听器组
     */
    protected ShapeListener listener;

    /**
     *
     */
    protected int[][] body;

    /**
     * 当前显示的状态
     */
    protected int status;

    /**
     * 图形的真实高度
     */
    protected int height;

    /**
     * 左上角的位置
     */
    protected int left;

    /**
     * 左上角的位置
     */
    protected int top;

    /**
     * 下落的速度
     */
    protected int speed;

    /**
     * 生命
     */
    protected boolean life;

    /**
     * 暂停状态
     */
    protected boolean pause;

    protected boolean swift;

    protected int swiftSpeed = Global.SWIFT_SPEED;

    protected Thread shapeThread, swiftThread;

    /**
     * 颜色
     */
    protected Color color = Color.BLUE;


    public Shape(int[][] body, int status) {
        super();
        this.body = body;
        this.status = status;
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                if (isMember(x, y, true)) {
                    height = y + 1;
                }
            }
        }
        init();
    }

    public void init() {
        life = true;
        pause = false;
        swift = false;
        left = Global.WIDTH / 2 - 2;
        top = 0 - height;
        speed = Global.CURRENT_SPEED;
    }


    public void rotate(){
        status = (status + 1) % body.length;
    }
    
    public void moveUp(){
        top--;
    }
    public void moveDown(){
        top++;
    }
    public void moveLeft(){
        left--;
    }
    public void moveRight(){
        left++;
    }

    protected class ShapeDriver implements Runnable {

        @Override
        public void run() {
            if (listener == null) {
                throw new RuntimeException("请先注册ShapeListener");
            }
            while (life && listener.isShapeMoveDownable(Shape.this)) {
                if (!swift) {
                    if (!pause) {
                        moveDown();
                        listener.shapeMovedDown(Shape.this);
                    }
                }
                try {
                    Thread.sleep(speed);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            life = false;
        }
    }



    public boolean isMember(int x,int y, boolean isRotate) {
        return getFlagByPoint(isRotate ? (status + 1) % body.length : status,
                x, y);
    }

    public void drawMe(Graphics graphics) {
        if (!life) {
            return;
        }
        //graphics.setColor(color);
        graphics.setColor(Color.red);

        for (int x = 0; x < 4; x++)
            for (int y = 0; y < 4; y++)
                if (getFlagByPoint(status, x, y))
                    drawUnit(graphics, (left + x) * Global.CELL_WIDTH, (top + y)
                                    * Global.CELL_HEIGHT, Global.CELL_WIDTH,
                            Global.CELL_HEIGHT);
    }

    private boolean getFlagByPoint(int status, int x, int y) {
        return body[status][y * 4 + x] == 1;
    }

    public void drawUnit(Graphics g, int x, int y, int width, int height) {
        g.fill3DRect(x, y, width, height, true);
    }
    
    public void speedDown(){
        speed += Global.SPEED_STEP;
        Global.CURRENT_SPEED = speed;
    }

    public void speedUp(){
        speed -= Global.SPEED_STEP;
        Global.CURRENT_SPEED = speed;
    }
    public static int getROTATE() {
        return ROTATE;
    }

    public static int getUP() {
        return UP;
    }

    public static int getDOWN() {
        return DOWN;
    }

    public static int getLEFT() {
        return LEFT;
    }

    public static int getRIGHT() {
        return RIGHT;
    }

    public ShapeListener getListener() {
        return listener;
    }

    public void setListener(ShapeListener listener) {
        this.listener = listener;
    }

    public int[][] getBody() {
        return body;
    }

    public void setBody(int[][] body) {
        this.body = body;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getLeft() {
        return left;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public boolean isLife() {
        return life;
    }

    public void setLife(boolean life) {
        this.life = life;
    }

    public boolean isPause() {
        return pause;
    }

    public void setPause(boolean pause) {
        this.pause = pause;
    }

    public void changePause(){
        this.pause = !this.pause;
    }

    public boolean isSwift() {
        return swift;
    }

    public void setSwift(boolean swift) {
        if (this.swift == swift)
            return;

        this.swift = swift;
        if (this.swift) {
            swiftThread = new Thread(new ShapeSwiftDriver());
            swiftThread.start();
        }
    }
    protected class ShapeSwiftDriver implements Runnable {

        public void run() {
            // TODO Auto-generated method stub
            while (swift && life) {
                if (listener == null)
                    throw new RuntimeException("请先注册 ShapeListener");
                if (listener.isShapeMoveDownable(Shape.this)) {
                    if (!pause) {
                        moveDown();
                        listener.shapeMovedDown(Shape.this);
                    }
                    try {
                        Thread.sleep(swiftSpeed);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    life = false;
                }
            }
        }

    }

    public int getSwiftSpeed() {
        return swiftSpeed;
    }

    public void setSwiftSpeed(int swiftSpeed) {
        this.swiftSpeed = swiftSpeed;
    }


    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void addShapeListener(ShapeListener l) {
        if (l == null || this.listener == l)
            return;
        if (this.listener != null)
            throw new RuntimeException(new TooManyListenersException());
        this.listener = l;
        start();
    }

    protected void start() {
        shapeThread = new Thread(new ShapeDriver());
        shapeThread.start();
    }
    public synchronized void die() {
        this.life = false;
    }



}
