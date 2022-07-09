package com.tedu.element;

import java.util.Random;
import com.tedu.manager.ElementManager;
import com.tedu.manager.GameElement;
import com.tedu.manager.GameLoad;
import com.tedu.show.GameJFrame;

public class Trap extends ElementObj{

    private static Random ran=new Random(); //随机生成器
    private boolean haveDie=false;//防止重复警告
    private int type=1;//陷阱种类，2是激光，1是导弹
    public Trap() { }

    @Override
    public ElementObj createElement(String kind_str) {

        int x=ran.nextInt(GameJFrame.GameX-80)+41;//横坐标
        int y=10;//纵坐标
        this.setX(x);
        this.setY(y);
        this.setCamp(2);//设置为陷阱的阵营
        this.setKind(String.valueOf(ran.nextInt(2)+1));
        //敌机种类不同属性初始化
        switch(this.getKind())
        {
            case "1":case "2"://导弹,
                this.kindToTrap(90, 90);
                break;
        }
        this.setIcon(GameLoad.imgMap.get("trap"+this.getKind()));
        return this;
    }
    
    //陷阱种类初始化陷阱属性函数(陷阱大小)
    public void kindToTrap(int width,int height)
    {
        this.setW(width);
        this.setH(height);
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
            if(getKind().equals("1"))
                shoot(3, new double[]{this.getX() + this.getW() / 2, this.getY() + this.getH()},
                        new double[]{0, 7});
            else if(getKind().equals("2"))
                shoot(4,new double[] {this.getX()+this.getW()/2,this.getY()+this.getH()},
                        new double[] {0,0});
            this.haveDie=true;
        }
    }

    @Override
    protected void updateImage(long time) {
        this.setIcon(GameLoad.imgMap.get("trap"+this.getType()));
    }

    //发射函数(子弹种类,子弹发射的位置)
    public void shoot(int bulletKind,double[]pos,double[]speed)
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

    //返回陷阱类型
    public int getType(){return  this.type;}
}
