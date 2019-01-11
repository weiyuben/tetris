package com.ben.tetric.util;

import java.awt.*;
import java.util.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class Global {
    private static Properties properties = new Properties();

    /**
     * 配置文件的路径(默认为当前目录下的 snake.ini文件)
     */
    private static String CONFIG_FILE = "tetris.ini";

    /**
     * 一个格子的宽度, 单位:像素 <BR>
     * 对应的配置文件中的关键字为: cell_width, 或用 cell_size指定<BR>
     * 范围1 至 100<BR>
     * 缺省值为 23
     */
    public static final int CELL_WIDTH;

    /**
     * 一个格子的高度, 单位:像素 <BR>
     * 对应的配置文件中的关键字为: cell_width, 或用 cell_size指定<BR>
     * 范围1 至 100<BR>
     * 缺省值为 23
     */
    public static final int CELL_HEIGHT;
    /**
     * 用格子表示的宽度, (宽度为多少个格子) 单位:格 <BR>
     * 对应的配置文件中的关键字为: width<BR>
     * 范围10 至 80<BR>
     * 缺省值为 15
     */
    public static final int WIDTH;

    /**
     * 用格子表示的高度, (高度为多少个格子), 单位:格<BR>
     * 对应的配置文件中的关键字为: height<BR>
     * 范围10 至 60<BR>
     * 缺省值为20
     */
    public static final int HEIGHT;

    /**
     * 图形下落的初始速度 (单位: 毫秒/格)<BR>
     * 对应的配置文件中的关键字为: speed<BR>
     * 范围10 至 无限大<BR>
     * 缺省值为 200
     */
    public static final int DEFAULT_SPEED;

    /**
     * 保存当前游戏中图形的下落速度
     */
    public static int CURRENT_SPEED;

    /**
     * 图形快速下落的速度 (单位: 毫秒/格)<BR>
     * 对应的配置文件中的关键字为: swift_speed<BR>
     * 范围0 至 无限大<BR>
     * 缺省值为 15
     */
    public static final int SWIFT_SPEED;

    /**
     * 每次加速或减速的幅度 (单位: 毫秒/格)<BR>
     * 对应的配置文件的关键字为: speed_step<BR>
     * 范围1 至 无限大<BR>
     * 缺省值为 25
     */
    public static final int SPEED_STEP;

    /**
     * 消除满行前暂停效果的时间(单位: 毫秒)<BR>
     * 对应的配置文件的关键字为: stay_time<BR>
     * 范围0 至 无限大<BR>
     * 缺省值为 200
     */
    public static final int DEFAULT_STAY_TIME;

    /**
     * 消除满行前暂停效果的时间
     */
    public static int STAY_TIME;

    private static Random random = new Random();

    public static final String TITLE_LABEL_TEXT;

    public static final String INFO_LABEL_TEXT;

    private static final Color[] DEFAULT_COLORS = new Color[] {
            new Color(0x990066), new Color(0x990099), new Color(0x330099),
            new Color(0x663300), new Color(0x009966), new Color(0x003333) };

    public static final List<Color> COMMON_COLORS;

    /**
     * 返回一个随机的颜色
     *
     * @return
     */
    public static Color getRandomColor() {
        return DEFAULT_COLORS[random.nextInt(DEFAULT_COLORS.length)];
    }

    /**
     * 默认的构造器, 私有的, 不能生成这个类的实例
     */
    private Global() {
    }

    /**
     * 初始化常量
     */
    static {
        InputStream inputStream = null;

        try {
            inputStream = new FileInputStream(CONFIG_FILE);
            properties.load(inputStream);
        } catch (FileNotFoundException e) {
            System.out.println("没有配置文件");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null)
                    inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Integer temp = null;
        WIDTH = (temp = getIntValue("width")) != null && temp <= 80
                && temp >= 10 ? temp : 15;
        HEIGHT = (temp = getIntValue("height")) != null && temp <= 60
                && temp >= 10 ? temp : 20;
        DEFAULT_SPEED = CURRENT_SPEED = (temp = getIntValue("speed")) != null
                && temp >= 10 ? temp : 300;
        SWIFT_SPEED = (temp = getIntValue("swift_speed")) != null && temp >= 0 ? temp
                : 15;
        SPEED_STEP = (temp = getIntValue("speed_step")) != null && temp >= 1 ? temp
                : 25;
        DEFAULT_STAY_TIME = STAY_TIME = (temp = getIntValue("stay_time")) != null
                && temp >= 0 ? temp : 200;
        int defaultCellSize = (temp = getIntValue("cell_size")) != null
                && temp > 0 && temp <= 100 ? temp : 23;
        CELL_WIDTH = (temp = getIntValue("cell_width")) != null && temp > 0
                && temp <= 100 ? temp : defaultCellSize;
        CELL_HEIGHT = (temp = getIntValue("cell_height")) != null && temp > 0
                && temp <= 100 ? temp : defaultCellSize;

        String tempStr = null;
        TITLE_LABEL_TEXT = (tempStr = getValue("title")) == null ? "说明："
                : tempStr;
        INFO_LABEL_TEXT = (tempStr = getValue("info")) == null ? "方向键控制方向, 回车键暂停/继续\nPAGE UP, PAGE DOWN 加速或减速\n\n"
                : tempStr;

        COMMON_COLORS = loadColors();
    }

    /**
     * 要考虑多种情况<BR>
     * 1. 没有这个key和value<BR>
     * 2 有key 没有 value
     */
    private static Integer getIntValue(String key) {
        if (key == null)
            throw new RuntimeException("key 不能为空");
        try {
            return new Integer(properties.getProperty(key));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static String getValue(String key) {
        try {
            return new String(properties.getProperty(key).getBytes("iso8859-1"));
        } catch (Exception e) {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    private static List<Color> loadColors() {

        List<Color> l = new ArrayList<Color>(7);
        for (int i = 0; i < 7; i++)
            l.add(null);

        Set set = properties.keySet();
        Iterator iter = set.iterator();
        while (iter.hasNext()) {
            String key = (String) iter.next();
            if ("1".equals(key.trim()))
                addColor(l, 0, getValue(key));
            else if ("2".equals(key.trim()))
                addColor(l, 1, getValue(key));
            else if ("3".equals(key.trim()))
                addColor(l, 2, getValue(key));
            else if ("4".equals(key.trim()))
                addColor(l, 3, getValue(key));
            else if ("5".equals(key.trim()))
                addColor(l, 4, getValue(key));
            else if ("6".equals(key.trim()))
                addColor(l, 5, getValue(key));
            else if ("7".equals(key.trim()))
                addColor(l, 6, getValue(key));
        }

        for (int i = 0; i < 7; i++) {
            l.remove(null);
        }

        if (l.size() < 1) {
            for (int i = 0; i < DEFAULT_COLORS.length; i++) {
                l.add(DEFAULT_COLORS[i]);
            }
        } else {
            if (l.size() != 7)
                System.out.println("您一共设置了 " + l.size() + " 种有效颜色， 建议设置七种");

            return l.subList(0, l.size() > 7 ? 7 : l.size());
        }
        return l;
    }

    @SuppressWarnings("unchecked")
    private static void addColor(List l, int index, String str) {
        str = str.trim();
        if (!str.startsWith("0x") || str.length() < 3) {
            System.out.println("颜色设置有误，请检查 : " + str + " (key)");
            return;
        }

        try {
            String strRGB = str.substring(2, str.length() >= 8 ? 8 : str
                    .length());
            int rgb = Integer.valueOf(strRGB, 16);
            Color c = new Color(rgb);
            if (c != null) {
                l.add(index, c);
            }
        } catch (Exception e) {
            System.out.println("(e)颜色设置有误，请检查:" + str + "(key)");
            e.printStackTrace();
            return;
        }
    }
}
