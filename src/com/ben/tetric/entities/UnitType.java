package com.ben.tetric.entities;

import java.awt.*;

public class UnitType {
    /* 定义一些要用到的常量 */

    /**
     * 空白类型的值
     */
    private static final int BLANK_VALUE = 0;

    /**
     * 边框类型的值
     */
    private static final int STUBBORN_OBSTACLE_VALUE = 1;

    /**
     * 障碍物类型的值
     */
    private static final int OBSTACLE_VALUE = 2;

    /**
     * 空白类型
     */
    public static final UnitType BLANK = new UnitType(BLANK_VALUE, Color.WHITE);

    /**
     * 边框类类型
     */
    public static final UnitType STUBBORN_OBSTACLE = new UnitType(
            STUBBORN_OBSTACLE_VALUE, new Color(0x808000));

    /**
     * 障碍物类型
     */
    public static final UnitType OBSTACLE = new UnitType(OBSTACLE_VALUE,
            Color.DARK_GRAY);

    /**
     * 类型值
     */
    private int value;

    /**
     * 颜色
     */
    private Color color;

    /**
     * 得到类型值
     *
     * @return
     */
    public int getValue() {
        return value;
    }

    /**
     * 设置类型值
     *
     * @param value
     */
    public void setValue(int value) {
        this.value = value;
    }

    /**
     * 得到颜色
     *
     * @return
     */
    public Color getColor() {
        return color;
    }

    /**
     * 设置颜色
     *
     * @param color
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * 接受类型的私有的构造方法<BR>
     *
     * @param value
     */
    private UnitType(int value) {
        super();
        this.value = value;
    }

    /**
     * 接受类型和颜色的私有的构造方法
     *
     * @param value
     * @param color
     */
    private UnitType(int value, Color color) {
        super();
        this.value = value;
        this.color = color;
    }

    /**
     * 返回一个新的,和自己有一样的颜色并且类型相同的UnitType
     */
    @Override
    public UnitType clone() {
        // TODO Auto-generated method stub
        return new UnitType(this.value, this.color);
    }

    /**
     * 把自己克隆成和指定的UnitType 相同的 UnitType, value 和 Color 都会被克隆
     *
     * @param ut
     */
    public void cloneProperties(UnitType ut) {
        this.color = ut.color;
        this.value = ut.value;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + value;
        return result;
    }

    /**
     * 相同的类型就是相等, 不比较颜色
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final UnitType other = (UnitType) obj;
        if (value != other.value)
            return false;
        return true;
    }
}
