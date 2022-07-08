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
    //子弹种类(1 普通子弹,2 散弹,3 导弹,4 激光,5 等离子球)
    private int shoot_interval=80;//射击间隔,单发为100
    private int weapon_kind=7;//武器种类
    private int weapon_count=7;//武器种类总数

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
    private int test;
    //键盘事件，按f切换武器
    @Override
    public void keyClick(boolean bl, int key) {
    	if(bl) {
			switch(key)
			{
			case 70://切换武器
			  this.weapon_kind=this.weapon_kind==this.weapon_count?1:this.weapon_kind+1;
			  break;
			case 90: //闪光效果
				ElementObj obj=GameLoad.getObj("flash");  		
				ElementObj element = obj.createElement("flash0");
				ElementManager.getManager().addElement(element, GameElement.DIE);
				break;
			}
		}
    }

    @Override
    public void move(long gameTime) {
        this.setX(tx);
        this.setY(ty);
    }

    protected void updateImage() {
       this.setIcon(GameLoad.imgMap.get("play"+this.getKind()));
    }
    
	//发射函数(子弹种类,子弹发射的位置,子弹速度,发射间隔)
	public void shoot(int bulletKind,int[]pos,int[]speed,int shoot_interval)
	{
		this.setInterval(shoot_interval);
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
    
	@Override   //发射子弹函数
   public void add(long gameTime) {
	//一定间隔发射子弹
	if((gameTime+2)%this.shoot_interval==0)
	{
		switch(this.weapon_kind)
		{
		
		case 1:
			//发射普通子弹
			shoot(1, new int[] {this.getX()+this.getW()/2,this.getY()},
			new int[] {0,-3},80);
			break;
		case 2:
			//发射双发子弹
			this.shoot(1,
			new int[]{this.getX()+this.getW()/2-10,this.getY(),
			this.getX()+this.getW()/2+10,this.getY()}, 
			new int[] {0,-3,0,-3},80);
			break;
		case 3:
			//发射散弹
			this.shoot(2,
			new int[]{this.getX()+this.getW()/2-5,this.getY(),
			this.getX()+this.getW()/2,this.getY(),
			this.getX()+this.getW()/2+5,this.getY()},
			new int[] {-1,-3,0,-3,1,-3},80);
			break;
		case 4://机枪
			shoot(1, new int[] {this.getX()+this.getW()/2,this.getY()},
			new int[] {0,-3},20);
			break;
		case 5://双重机枪
			this.shoot(1,
			new int[]{this.getX()+this.getW()/2-10,this.getY(),
			this.getX()+this.getW()/2+10,this.getY()}, 
			new int[] {0,-3,0,-3},20);
			break;
		case 6:
			//发射激光
			shoot(4,new int[] {this.getX()+this.getW()/2,this.getY()},
					new int[] {0,0},80);
			break;
		case 7:
			//发射导弹
			shoot(3, new int[] {this.getX()+this.getW()/2,this.getY()-10},
			new int[] {0,-7},80);
			break;
		}
		
	}
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
		//爆炸
		ElementObj obj=GameLoad.getObj("file");  		
		ElementObj element = obj.createElement(//子弹json数据生成
        GameLoad.getFileString(this.getX()+this.getW()/2,this.getY()+this.getH()/2, 
           0,0,3,5));//生成爆炸
		element.setExplodeMsg(30,12);
		ElementManager.getManager().addElement(element, GameElement.PLAYFILE);
	}
    
}







