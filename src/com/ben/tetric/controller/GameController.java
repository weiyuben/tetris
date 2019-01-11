package com.ben.tetric.controller;

import com.ben.tetric.entities.Ground;
import com.ben.tetric.entities.Shape;
import com.ben.tetric.entities.ShapeFactory;
import com.ben.tetric.listener.GameListener;
import com.ben.tetric.listener.GroundListener;
import com.ben.tetric.listener.ShapeListener;
import com.ben.tetric.util.Global;
import com.ben.tetric.view.GamePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GameController extends KeyAdapter implements ShapeListener, GroundListener {
    protected Set<GameListener> listeners = new HashSet<>();
    protected ShapeFactory shapeFactory;
    protected Shape shape;
    protected GamePanel gamePanel;
    protected JLabel gameInfoLabel;
    protected boolean playing;
    protected Ground ground;


    public GameController(ShapeFactory shapeFactory, Ground ground, GamePanel gamePanel) {
        super();
        this.shapeFactory = shapeFactory;
        this.gamePanel = gamePanel;
        this.ground = ground;
    }

    public GameController(ShapeFactory shapeFactory,  Ground ground, GamePanel gamePanel, JLabel gameInfoLabel) {
        this(shapeFactory, ground, gamePanel);
        this.setGameInfoLabel(gameInfoLabel);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() != KeyEvent.VK_Y && !playing) {
            return;
        }
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                if (isPausingGame()) {
                    this.continueGame();
                }
                shape.setSwift(false);
                if (isPlaying() && ground.isMoveable(shape, Shape.LEFT)) {
                    shape.moveLeft();
                }
                break;
            case KeyEvent.VK_RIGHT:
                if (isPausingGame()) {
                    this.continueGame();
                }
                shape.setSwift(false);

                if (isPlaying() && ground.isMoveable(shape, Shape.RIGHT))
                    shape.moveRight();
                break;
            /**
             * 方向上
             */
            case KeyEvent.VK_UP:

                if (isPlaying()) {
                    if (!shape.isPause()) {
                        if (ground.isMoveable(shape, Shape.ROTATE)) {
                            shape.setSwift(false);
                            shape.rotate();
                        }
                    } else {
                        if (ground.isMoveable(shape, Shape.UP))
                            shape.moveUp();
                        else {
                            shape.die();
                            shape = shapeFactory.getShape(this);
                        }
                    }
                }

                break;
            /**
             * 方向下
             */
            case KeyEvent.VK_DOWN:
                if (isPausingGame()) {
                    this.continueGame();
                }
                if (isPlaying() && isShapeMoveDownable(shape))
                    shape.moveDown();
                break;
            /**
             * PAGE UP
             */
            case KeyEvent.VK_PAGE_UP:
                shape.speedUp();
                break;
            /**
             * PAGE DOWN
             */
            case KeyEvent.VK_PAGE_DOWN:
                shape.speedDown();
                break;
            /**
             * 反引号,换一个图形
             */
            case KeyEvent.VK_BACK_QUOTE:
                if (isPlaying()) {
                    shape.die();
                    shape = shapeFactory.getShape(this);
                }
                break;
            case KeyEvent.VK_ENTER:
                if (isPausingGame())
                    this.continueGame();
                else
                    this.pauseGame();
                break;
            case KeyEvent.VK_Y:
                if (!isPlaying())
                    newGame();
                break;
            case KeyEvent.VK_SPACE:

                if (isPlaying() && !isPausingGame())
                    shape.setSwift(true);
                break;

        }
    }
    public void newGame() {
        playing = true;
        ground.init();
        ground.addGroundListener(this);

        Global.CURRENT_SPEED = Global.DEFAULT_SPEED;
        shape = shapeFactory.getShape(this);

        if (playing)
            gamePanel.redisplay(ground, shape);

        if (gameInfoLabel != null)
            gameInfoLabel.setText(this.getNewInfo());

        for (GameListener l : listeners)
            l.gameStart();
    }

    public void stopGame() {
        if (shape == null)
            return;
        playing = false;
        for (GameListener l : listeners)
            l.gameOver();
    }

    public void pauseGame() {
        if (shape == null) {
            return;
        }
        shape.setPause(true);
        for (GameListener l : listeners) {
            l.gamePause();
        }
    }
    public void continueGame() {
        shape.setPause(false);
        for (GameListener l : listeners) {
            l.gameContinue();
        }
    }

    public String getNewInfo() {
        if (!playing || ground.isFull())
            return " ";// "提示: 按 Y 开始新游戏";
        else
            return new StringBuffer().append("提示: ").append(" 速度 ").append(
                    shape.getSpeed()).append("毫秒/格").toString();
    }
    public Set<GameListener> getListeners() {
        return listeners;
    }

    public void setListeners(Set<GameListener> listeners) {
        this.listeners = listeners;
    }

    public ShapeFactory getShapeFactory() {
        return shapeFactory;
    }

    public void setShapeFactory(ShapeFactory shapeFactory) {
        this.shapeFactory = shapeFactory;
    }

    public Shape getShape() {
        return shape;
    }

    public void setShape(Shape shape) {
        this.shape = shape;
    }

    public GamePanel getGamePanel() {
        return gamePanel;
    }

    public void setGamePanel(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public JLabel getGameInfoLabel() {
        return gameInfoLabel;
    }

    public void setGameInfoLabel(JLabel gameInfoLabel) {
        this.gameInfoLabel = gameInfoLabel;
        this.gameInfoLabel.setSize(Global.WIDTH * Global.CELL_WIDTH, 20);
        this.gameInfoLabel.setFont(new Font("宋体", Font.PLAIN, 12));
        gameInfoLabel.setText(this.getNewInfo());
    }


    public void setPlaying(boolean playing) {
        this.playing = playing;
    }

    public Ground getGround() {
        return ground;
    }

    public void setGround(Ground ground) {
        this.ground = ground;
    }

    @Override
    public void beforeDeleteFullLine(Ground ground, int lineNum) {
        ground.changeFullLineColor(lineNum);
        gamePanel.redisplay(ground, shape);
        try {
            Thread.sleep(Global.STAY_TIME);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void fullLineDeleted(Ground ground, int deleteLineCount) {
        System.out.println("消了 " + deleteLineCount + " 行");
    }
    public boolean isPlaying() {
        if (playing && !ground.isFull())
            return true;
        return false;
    }

    @Override
    public void groundIsFull() {
        if (playing) {
            playing = false;
            for (GameListener l : listeners) {
                l.gameOver();
            }
        }
    }

    @Override
    public synchronized boolean isShapeMoveDownable(Shape s) {

        if (shape == null)
            return true;
        if (!playing || shape != s)
            return false;

        if (ground.isMoveable(shape, Shape.DOWN))
            return true;

        shape.die();
        ground.accept(shape);
        if (playing && !ground.isFull()) {
            shape = shapeFactory.getShape(this);
        }
        gamePanel.redisplay(ground, shape);
        if (gameInfoLabel != null)
            gameInfoLabel.setText(this.getNewInfo());

        return false;
    }

    @Override
    public void shapeMovedDown(Shape shape) {
        if (playing && ground != null && shape != null)
            gamePanel.redisplay(ground, shape);
    }

    public boolean isPausingGame() {
        return shape.isPause();
    }

    public void addGameListener(GameListener l) {
        if (l != null)
            this.listeners.add(l);
    }

    /**
     * 移除监听器
     *
     * @param l
     */
    public void removeGameListener(GameListener l) {
        if (l != null)
            this.listeners.remove(l);
    }
}
