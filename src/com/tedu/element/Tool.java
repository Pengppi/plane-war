package com.tedu.element;

import java.util.Random;

import com.tedu.manager.GameLoad;
import com.tedu.show.GameJFrame;

//道具类
public class Tool extends ElementObj {

	 //private int deleteTime=0;//消失的时间(只有deleteTime==0与isLive==false时才会消失)
    //kind道具种类(1 医疗包,2 护盾, 3 弹药箱(6 核弹, 7 脉冲弹, 8 浮游炮), 4 升级, 5 复活心, 6宝石)
    public Random ran = new Random();
    public static int nuclear_count = 0;//核弹的数量
    public static int emp_count = 0;//电磁脉冲的数量

    //道具显示的更新
    @Override
    protected void updateImage(long time) {
        this.setIcon(GameLoad.imgMap.get("tool" + this.getKind()));
    }

    public Tool() {}

    @Override
    public ElementObj createElement(String kind_str) {//道具的初始化

        //道具信息初始化
        int x = ran.nextInt(GameJFrame.GameX - 80) + 41;//横坐标
        int y = ran.nextInt(20);//纵坐标
        this.setX(x);
        this.setY(y);
        this.setSpeed(0, ran.nextDouble() * 2 + 2);
        //道具种类(1 医疗包,2 护盾, 3 弹药箱， 4 升级, 5 复活心, 6宝石)kind
        this.setKind(kind_str);
        //设置大小,道具偏移(让图片显示为发射点的中心)
        this.setW(50);
        this.setH(50);
        this.setX(this.getX() - this.getW() / 2);
        this.setY(this.getY() - this.getH() / 2);
        this.setIcon(GameLoad.imgMap.get("tool" + this.getKind()));
        return this;
    }

    //道具移动
    @Override
    protected void move(long gameTime) {
        this.setX(this.getX() + this.getXSpeed());
        this.setY(this.getY() + this.getYSpeed());
    }

    //道具消失函数
    @Override
    public void setLive(boolean live) {
        //if(this.deleteTime>0)
        //return ;
        super.setLive(live);
    }

}
