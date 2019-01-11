package com.ben.tetric.view;

import com.ben.tetric.entities.Ground;
import com.ben.tetric.util.Global;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import com.ben.tetric.entities.Shape;

public class GamePanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private Image oimg;

    private Graphics og;

    public static final Color DEFAULT_BACKGROUND_COLOR = new Color(0xcfcfcf);
    /**
     * 背景颜色
     */
    protected Color backgroundColor = DEFAULT_BACKGROUND_COLOR;

    public GamePanel() {
        /* 设置大小和布局 */
        this.setSize(Global.WIDTH * Global.CELL_WIDTH, Global.HEIGHT
                * Global.CELL_HEIGHT);
        this.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
        this.setFocusable(true);
    }

    /**
     * 重新显示 Ground, Shape
     *
     * @param ground
     * @param shape
     */
    public synchronized void redisplay(Ground ground, Shape shape) {

        /* 重新显示 */
        if (og == null) {
            oimg = createImage(getSize().width, getSize().height);
            if (oimg != null)
                og = oimg.getGraphics();
        }
        if (og != null) {
            og.setColor(backgroundColor);
            og.fillRect(0, 0, Global.WIDTH * Global.CELL_WIDTH, Global.HEIGHT
                    * Global.CELL_HEIGHT);
            ground.drawMe(og);
            if (shape != null)
                shape.drawMe(og);
            this.paint(this.getGraphics());

        }
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(oimg, 0, 0, this);
    }

    /**
     * 得到当前的背景颜色
     *
     * @return
     */
    public Color getBackgroundColor() {
        return backgroundColor;
    }

    /**
     * 设置当前的背景颜色
     *
     * @param backgroundColor
     */
    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
}
