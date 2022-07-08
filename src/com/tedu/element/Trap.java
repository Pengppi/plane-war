package com.tedu.element;

import java.awt.Graphics;
import java.util.Random;

import com.tedu.manager.ElementManager;
import com.tedu.manager.GameElement;
import com.tedu.manager.GameLoad;
import com.tedu.show.GameJFrame;

public class Trap extends ElementObj{

    private static Random ran=new Random(); //随机生成器
    private boolean haveDie=false;//防止重复警告
    private int shoot_interval=200;//射击间隔ֵ
    private int moveXNum=0;//水平移动距离
    private int moveYNum=1;//垂直移动距离
    //bulletKind子弹种类(1 普通子弹,2 散弹,3 导弹,4 激光, 5 等离子球)
    private int divideBulletTime=0;//分裂者分裂子弹的间隔
    public Trap() { }

    @Override
    public void showElement(Graphics g) {
        g.drawImage(this.getIcon().getImage(),
                this.getX(), this.getY(),
                this.getW(), this.getH(), null);
    }
    @Override
    public ElementObj createElement(String kind_str) {

        int x=ran.nextInt(GameJFrame.GameX-80)+41;//横坐标
        int y=ran.nextInt(20);//纵坐标
        this.setX(x);
        this.setY(y);
        this.setKind(kind_str);//飞机种类
        this.setCamp(2);//设置为敌方的阵营

        //敌机种类不同属性初始化
        switch(this.getKind())
        {
            case "1"://普通敌机(单发)
                this.kindToEnemy(60, 60, 3, 0, 0);
                break;
            case "2"://双发敌机(双发)
                this.kindToEnemy(60, 60, 5, 0, 0);
                break;
            case "3"://巨型敌机(散射)
                this.kindToEnemy(90, 90, 12, 0, 0);
                break;
            case "4"://疾速敌机(单发)
                this.kindToEnemy(60, 60, 3, 0, 0);
                break;
            case "5"://小型敌机(向左，向右随机)
                this.kindToEnemy(40, 40, 2, new Random().nextBoolean()?1:-1, 0);
                break;
            case "6"://分裂者(中间停下，向四个方向发射)
                this.kindToEnemy(60, 60, 4, 0, 0);
                //设置分裂的时间间隔
                this.divideBulletTime=ran.nextInt(2)+4;//[4-5]
                break;
            case "7"://机枪敌机（连射）
                setInterval(50);
            case "8"://激光敌机（发射激光）
                this.kindToEnemy(60, 60, 6, 0, 0);
                break;
            case "9"://自爆敌机（一定几率自爆）
                this.kindToEnemy(60, 60, 5, 0, 0);
                break;
            case "0"://导弹敌机（发射导弹）
                this.kindToEnemy(70, 70, 8, 0, 0);
                break;
        }
        this.setIcon(GameLoad.imgMap.get("enemy"+this.getKind()));
        return this;
    }

    //敌机种类初始化敌机属性函数(敌机大小,血量，运动方式(水平速度，垂直速度)，得分)
    public void kindToEnemy(int width,int height,int density,int moveXNum,int moveYNum)
    {
        this.setW(width);
        this.setH(height);
        this.setDensity(density);
        this.moveXNum=moveXNum;
        this.moveYNum=moveYNum;
    }

    @Override
    protected void move(long gameTime) {

        this.setX(this.getX()+this.moveXNum);
        this.setY(this.getY()+this.moveYNum);
    }

    @Override
    public void setLive(boolean live) {
        super.setLive(live);
        if(!this.isLive())
        {
            this.die();//调用死亡函数(如爆炸)
        }
    }

    @Override
    public void die() {
        if(!this.haveDie)//增加积分
        {
            //陷阱结束
            shoot(4,new int[] {this.getX()+this.getW()/2,this.getY()+this.getH()},
                    new int[] {0,0});
            this.haveDie=true;
        }
    }

    @Override
    protected void updateImage(long time) {
        this.setIcon(GameLoad.imgMap.get("enemy"+this.getKind()));
    }

    //发射函数(子弹种类,子弹发射的位置)
    public void shoot(int bulletKind,int[]pos,int[]speed)
    {
        for(int i=0;i<pos.length;i+=2)//pos[i]为横坐标,pos[i+1]为纵坐标,speed[i]为水平速度,speed[i+1]为垂直速度
        {
            ElementObj obj=GameLoad.getObj("file");
            ElementObj element = obj.createElement(//子弹json数据生成
                    GameLoad.getFileString(pos[i],pos[i+1],
                            speed[i],speed[i+1],this.getCamp(),bulletKind));
            ElementManager.getManager().addElement(element, GameElement.PLAYFILE);
        }
    }

    //设置发射间隔
    public void setInterval(int shoot_interval)
    {
        this.shoot_interval=shoot_interval;
    }

}
