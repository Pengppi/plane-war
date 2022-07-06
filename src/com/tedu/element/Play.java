package com.tedu.element;

import java.awt.Graphics;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;

import com.tedu.manager.ElementManager;
import com.tedu.manager.GameElement;
import com.tedu.manager.GameLoad;
import com.tedu.show.GameJFrame;

public class Play extends ElementObj /* implements Comparable<Play>*/ {
    /**
     * 移动属性:
     * 1.单属性  配合  方向枚举类型使用; 一次只能移动一个方向
     * 2.双属性  上下 和 左右    配合boolean值使用 例如： true代表上 false 为下 不动？？
     * 需要另外一个变来确定是否按下方向键
     * 约定    0 代表不动   1代表上   2代表下
     * 3.4属性  上下左右都可以  boolean配合使用  true 代表移动 false 不移动
     * 同时按上和下 怎么办？后按的会重置先按的
     * 说明：以上3种方式 是代码编写和判定方式 不一样
     * 说明：游戏中非常多的判定，建议灵活使用 判定属性；很多状态值也使用判定属性
     * 多状态 可以使用map<泛型,boolean>;set<判定对象> 判定对象中有时间
     *
     * @问题 1.图片要读取到内存中： 加载器  临时处理方式，手动编写存储到内存中
     * 2.什么时候进行修改图片(因为图片是在父类中的属性存储)
     * 3.图片应该使用什么集合进行存储
     */
    private int tx;
    private int ty;
    private int bulletKind=1;//子弹种类(1 普通子弹,2 导弹,3 激光, 4 等离子球)
    private int shoot_interval=100;//射击间隔

    public Play() { }
    public Play(int x, int y, int w, int h, ImageIcon icon) {
        super(x, y, w, h, icon);
    }
    
    
    @Override
   public ElementObj createElement(String str) {
    	//玩家飞机信息格式解析：（水平位置，垂直位置，飞机种类）
   	String[] split=str.split(",");
   	this.setX(new Integer(split[0]));
   	this.setY(new Integer(split[1]));
   	this.setKind(split[2]);
   	this.setCamp(1);//设置为我方的阵营
   	ImageIcon icon2=GameLoad.imgMap.get("play"+this.getKind());
   	this.setW(icon2.getIconWidth());
   	this.setH(icon2.getIconHeight());
   	this.setIcon(icon2);
   	//设置防御力
   	this.setDensity(3);
   	return this;
   }
    /**
     * 面向对象中第1个思想： 对象自己的事情自己做
     */
    @Override
    public void showElement(Graphics g) {
//		绘画图片
        g.drawImage(this.getIcon().getImage(),
                this.getX(), this.getY(),
                this.getW(), this.getH(), null);

    }

    /*
     * @说明 重写方法： 重写的要求：方法名称 和参数类型序列 必须和父类的方法一样
     * @重点 监听的数据需要改变状态值
     */
    @Override   // 注解 通过反射机制，为类或者方法或者属性 添加的注释(相当于身份证判定)
    public void mouseMove(int tx, int ty) {
        int x = +tx - this.getW()+45;
        int y = ty - this.getH();
        if (x < 0) {
            x = 0;
        }
        if (x > GameJFrame.GameX - this.getW()) {
            x = GameJFrame.GameX - this.getW();
        }
        if (y < 0) {
            y = 0;
        }
        if (y > GameJFrame.GameY - this.getH()) {
            y = GameJFrame.GameY - this.getH();
        }
        this.tx = x;
        this.ty = y;
    }



    @Override
    public void move(long gameTime) {
        this.setX(tx);
        this.setY(ty);
    }

    protected void updateImage() {
       this.setIcon(GameLoad.imgMap.get("play"+this.getKind()));
    }
    
	@Override   //发射子弹函数
public void add(long gameTime) {
	//一定间隔发射子弹
	if((gameTime+2)%this.shoot_interval==0)
	{
		//System.out.println("attack");
		ElementObj obj=GameLoad.getObj("file");  		
		ElementObj element = obj.createElement(this.toString());
		ElementManager.getManager().addElement(element, GameElement.PLAYFILE);
	}
}

//子弹json数据生成
@Override
public String toString() {
//生成子弹数据(位置，速度，阵营)(x:水平位置,y:垂直位置,hv:水平速度,vv:垂直速度,c:[1|2]:1 is play,2 is enemy)
	int x=this.getX();
	int y=this.getY();
	
	//向上射位置
	x+=this.getW()/2-5;
	return "x:"+x+",y:"+y+",hv:0,vv:-3,c:1,k:"+bulletKind;
}
    
}







