package com.ben.tetric.entities;

import com.ben.tetric.controller.GameController;
import com.ben.tetric.listener.GameListener;
import com.ben.tetric.listener.GroundListener;
import com.ben.tetric.util.Global;
import sun.tools.jconsole.Plotter;

import java.awt.*;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Ground {
    protected Set<GroundListener> listeners = new HashSet<>();
    protected UnitType[][] obstacles = new UnitType[Global.WIDTH][Global.HEIGHT];
    protected Color stubbornObstacleColor = UnitType.STUBBORN_OBSTACLE
            .getColor();

    public static final Color DEFAULT_GRIDDING_COLOR = Color.LIGHT_GRAY;
    protected Color griddingColor = DEFAULT_GRIDDING_COLOR;

    public static final Color DEFAULT_OBSTACLE_COLOR = UnitType.OBSTACLE
            .getColor();
    /**
     * 障碍物的颜色
     */
    protected Color obstacleColor = DEFAULT_OBSTACLE_COLOR;

    public static final Color DEFAULT_FULL_LINE_COLOR = Color.DARK_GRAY;
    /**
     * 满行的颜色
     */
    protected Color fullLineColor = DEFAULT_FULL_LINE_COLOR;

    /**
     * 是否画网格 的开关
     */
    protected boolean drawGridding;

    /**
     * 是否支持彩色石头
     */
    protected boolean colorfulSupport;

    /**
     * 是否还能接受石头
     */
    protected boolean full;

    protected Random random = new Random();


    public Ground() {
        init();
    }

    public void init() {
        clear();
        full = false;
    }

    public void clear() {
        for (int i = 0; i < Global.WIDTH; i++)
            for (int j = 0; j < Global.HEIGHT; j++)
                obstacles[i][j] = UnitType.BLANK.clone();
    }

    public void generateAStubbornStochasticObstacle() {
        Random r = new Random();
        if (Global.HEIGHT < 5) {
            return;
        }
        int y = r.nextInt(5) + Global.HEIGHT - 5;
        int x = r.nextInt(Global.WIDTH);
        addStubbornObstacle(x, y);
    }

    public void generateSomeStochasticObstacle(int amount, int lineNum) {
        if (lineNum < 1)
            return;
        if (lineNum > Global.HEIGHT)
            lineNum = Global.HEIGHT;
        for (int i = 0; i < amount; i++) {
            int x = random.nextInt(Global.WIDTH);
            int y = random.nextInt(5) + Global.HEIGHT - lineNum;
            obstacles[x][y] = UnitType.OBSTACLE.clone();
            obstacles[x][y].setColor(Global.getRandomColor());
        }
    }

    public void accept(Shape shape) {
        int left = shape.getLeft();
        int top = shape.getTop();

        
        for (int x = 0; x < 4; x++)
            for (int y = 0; y < 4; y++)
                if (left + x < Global.WIDTH && top + y < Global.HEIGHT) {
                    if (shape.isMember(x, y, false))
                        /**
                         * 如果超出上边界了, 就是放满了
                         */
                        if (top + y < 0) {
                            full = true;
                            for (GroundListener l : listeners)
                                l.groundIsFull(this);
                        } else {
                            /**
                             * 先变成障碍物
                             */
                            obstacles[left + x][top + y]
                                    .cloneProperties(UnitType.OBSTACLE);
                            obstacles[left + x][top + y]
                                    .setColor(colorfulSupport ? shape
                                            .getColor() : obstacleColor);
                        }
                }
        /**
         * 扫描并删除满行
         */
        deleteFullLine();
    }

    private void deleteFullLine() {
        int deleteLineNumber = 0;
        for (int y = Global.HEIGHT - 1; y >= 0; y--) {
            boolean isFull = true;
            for (int x = 0; x < Global.WIDTH; x++) {
                if (obstacles[x][y].equals(UnitType.BLANK)) {
                    isFull = false;
                }
            }

            if (isFull) {
                deleteLine(y++);
                deleteLineNumber++;
            }
        }

        if (deleteLineNumber > 0) {
            for (GroundListener l : listeners) {
                l.fullLineDeleted(this, deleteLineNumber);
            }
        }

    }

    private void deleteLine(int lineNum) {
        for (GroundListener l : listeners) {
            l.beforeDeleteFullLine(this, lineNum);
        }

        for (int y = lineNum; y > 0; y--) {
            for (int x = 0; x < Global.WIDTH; x++){
                if (!obstacles[x][y].equals(UnitType.STUBBORN_OBSTACLE)) {
                    if (obstacles[x][y - 1].equals(UnitType.STUBBORN_OBSTACLE)) {
                        obstacles[x][y].cloneProperties(UnitType.BLANK);
                        obstacles[x][y].setColor(this.griddingColor);
                    } else {
                        obstacles[x][y].cloneProperties(obstacles[x][y - 1]);
                    }
                }
            }
        }
        for (int x = 0; x < Global.WIDTH; x++)
            if (!obstacles[x][0].equals(UnitType.STUBBORN_OBSTACLE))
                obstacles[x][0] = UnitType.BLANK.clone();
    }


    public boolean isFull() {
        return full;
    }

    public synchronized boolean isMoveable(Shape shape, int action){
        int left = shape.getLeft();
        int top = shape.getTop();

        switch (action) {
            case Shape.UP:
                top--;
                break;
            case Shape.DOWN:
                top++;
                break;
            case Shape.LEFT:
                left--;
                break;
            case Shape.RIGHT:
                left++;
                break;
        }
        if (top < 0 - shape.getHeight()) {
            return false;
        }
        for (int x = 0; x < 4; x++)
            for (int y = 0; y < 4; y++)

            /**
             * 如果这个位置超出边界又是图形的一部分
             */
                if ((left + x < 0 || left + x >= Global.WIDTH || top + y >= Global.HEIGHT)
                        && shape.isMember(x, y, action == Shape.ROTATE))
                    return false;
                else if (top + y < 0)
                    continue;
                else {
                    /**
                     * 或者位置不是空白（是障碍物或不可消除的障碍物）又是图形的一部分
                     */
                    if (shape.isMember(x, y, action == Shape.ROTATE))
                        if (!obstacles[left + x][top + y]
                                .equals(UnitType.BLANK))
                            return false;
                }
        return true;
    }

    public void changeFullLineColor(int lineNum) {
        for (int x = 0; x < Global.WIDTH; x++) {
            obstacles[x][lineNum].setColor(fullLineColor);
        }
    }

    public void addObstacle(int x, int y) {
        if (x < 0 || x >= Global.WIDTH || y < 0 || y >= Global.HEIGHT)
            throw new RuntimeException("这个位置超出了显示区域 (x:" + x + "  y:" + y + ")");
        obstacles[x][y].cloneProperties(UnitType.OBSTACLE);

    }
    public void addStubbornObstacle(int x, int y) {
        if (x < 0 || x >= Global.WIDTH || y < 0 || y >= Global.HEIGHT)
            throw new RuntimeException("这个位置超出了显示区域 (x:" + x + "  y:" + y + ")");
        obstacles[x][y].cloneProperties(UnitType.STUBBORN_OBSTACLE);
    }

    public void drawMe(Graphics graphics) {
        for (int x = 0; x < Global.WIDTH; x++) {
            for (int y = 0; y < Global.HEIGHT; y++) {
                if (drawGridding && obstacles[x][y].equals(UnitType.BLANK)) {
                    graphics.setColor(griddingColor);
                    drawGridding(graphics, x * Global.CELL_WIDTH, y
                                    * Global.CELL_HEIGHT, Global.CELL_WIDTH,
                            Global.CELL_HEIGHT);
                } else if (obstacles[x][y].equals(UnitType.STUBBORN_OBSTACLE)) {
                    graphics.setColor(stubbornObstacleColor);
                    drawStubbornObstacle(graphics, x * Global.CELL_WIDTH, y
                                    * Global.CELL_HEIGHT, Global.CELL_WIDTH,
                            Global.CELL_HEIGHT);
                } else if (obstacles[x][y].equals(UnitType.OBSTACLE)) {
                    graphics.setColor(obstacles[x][y].getColor());
                    drawObstacle(graphics, x * Global.CELL_WIDTH, y
                                    * Global.CELL_HEIGHT, Global.CELL_WIDTH,
                            Global.CELL_HEIGHT);
                }
            }
        }
    }

    public void drawGridding(Graphics g, int x, int y, int width, int height) {
        g.drawRect(x, y, width, height);
    }

    public void drawStubbornObstacle(Graphics g, int x, int y, int width,
                                     int height) {
        g.fill3DRect(x, y, width, height, true);
    }

    public void drawObstacle(Graphics graphics, int x, int y, int width, int height) {
        graphics.fill3DRect(x,y,width,height,true);
    }
    public Color getStubbornObstacleColor() {
        return stubbornObstacleColor;
    }

    public void setStubbornObstacleColor(Color stubbornObstacleColor) {
        this.stubbornObstacleColor = stubbornObstacleColor;
    }

    public Color getGriddingColor() {
        return griddingColor;
    }

    public void setGriddingColor(Color griddingColor) {
        this.griddingColor = griddingColor;
    }

    public Color getObstacleColor() {
        return obstacleColor;
    }

    public void setObstacleColor(Color obstacleColor) {
        this.obstacleColor = obstacleColor;
    }

    public Color getFullLineColor() {
        return fullLineColor;
    }

    public void setFullLineColor(Color fullLineColor) {
        this.fullLineColor = fullLineColor;
    }

    public boolean isDrawGridding() {
        return drawGridding;
    }

    public void setDrawGridding(boolean drawGridding) {
        this.drawGridding = drawGridding;
    }

    public boolean isColorfulSupport() {
        return colorfulSupport;
    }

    public void setColorfulSupport(boolean colorfulSupport) {
        this.colorfulSupport = colorfulSupport;
    }


    public void addGroundListener(GroundListener listener) {
        if (listener != null) {
            this.listeners.add(listener);
        }
    }
    public void removeGroundListener(GroundListener l) {
        if (l != null)
            this.listeners.remove(l);
    }
    public boolean isStubbornObstacle(int x, int y) {
        if (x >= 0 && x < Global.WIDTH && y >= 0 && y < Global.HEIGHT)
            return obstacles[x][y].equals(UnitType.STUBBORN_OBSTACLE);
        else
            throw new RuntimeException("这个坐标超出了显示区域: (x:" + x + " y:" + y + ")");
    }

    /**
     * 指定位置是否是障碍物
     *
     * @param x
     * @param y
     * @return
     */
    public boolean isObstacle(int x, int y) {
        if (x >= 0 && x < Global.WIDTH && y >= 0 && y < Global.HEIGHT)
            return obstacles[x][y].equals(UnitType.OBSTACLE);
        else
            throw new RuntimeException("这个坐标超出了显示区域: (x:" + x + " y:" + y + ")");
    }

    /**
     * 指定位置是否是空白
     *
     * @param x
     * @param y
     * @return
     */
    public boolean isBlank(int x, int y) {
        if (x >= 0 && x < Global.WIDTH && y >= 0 && y < Global.HEIGHT)
            return obstacles[x][y].equals(UnitType.BLANK);
        else
            throw new RuntimeException("这个坐标超出了显示区域: (x:" + x + " y:" + y + ")");
    }
}
