package com.tedu.element;

import java.awt.Graphics;
import java.util.Random;

import com.tedu.manager.ElementManager;
import com.tedu.manager.GameElement;
import com.tedu.manager.GameLoad;
import com.tedu.show.GameJFrame;

public class Boss extends ElementObj{

	//种类：海妖终结者，巨霸魔牛，剧毒金蜥，紫魔君主，爆裂金刚，
	
	private static Random ran=new Random(); //随机生成器
    private boolean addStar=false;//防止重复加分
	private int shoot_interval=200;//射击间隔ֵ
	private int moveXNum=0;//水平移动距离
	private int moveYNum=1;//垂直移动距离
	private int bossMoveTime=0;//boss移动时间,用于控制boss的移动
	private int bossMovePeriod=2000;//boss移动，攻击的周期
	//bulletKind子弹种类(1 普通子弹,2 散弹,3 导弹,4 激光, 5 爆炸, 6等离子球)
	
	public Boss() { }
	
	@Override
	public void showElement(Graphics g) {
		g.drawImage(this.getIcon().getImage(), 
		this.getX(), this.getY(), 
		this.getW(), this.getH(), null);
	}
	@Override
	public ElementObj createElement(String kind_str) {
		
		int x=GameJFrame.GameX/2;//横坐标
		int y=0;//纵坐标
		this.setX(x);
		this.setY(y);
		this.setKind(kind_str);//飞机种类
		this.setCamp(2);//设置为敌方的阵营
		
		//敌机种类不同属性初始化
		switch(this.getKind())
		{
		case "1"://海妖终结者
		this.kindToBoss(150, 150, 120, 0, 1, 2000);
		break;
		case "2"://巨霸魔牛
		this.kindToBoss(150, 150, 120, 0, 1, 2000);
		break;
		case "3"://剧毒金蜥
		this.kindToBoss(150, 150, 120, 0, 1, 2000);
		break;
		case "4"://疾速敌机(单发)
		this.kindToBoss(150, 150, 150, 0, 1, 2000);
		break;
		case "5"://小型敌机(向左，向右随机)
		this.kindToBoss(180, 180, 200, 0, 1, 2000);
		break;
		}
		this.setX(this.getX()-this.getW()/2);//使得boss在正中间出现
		this.setIcon(GameLoad.imgMap.get("boss"+this.getKind()));
		return this;
	}

	//boss种类初始化敌机属性函数(敌机大小,血量，运动方式(水平速度，垂直速度),运动周期设置)
		public void kindToBoss(int width,int height,int density,int moveXNum,int moveYNum,int period)
		{
			this.setW(width);
			this.setH(height);
			this.setDensity(density);
			this.moveXNum=moveXNum;
			this.moveYNum=moveYNum;
			this.bossMovePeriod=period;
		}		
	
      @Override
      protected void move(long gameTime) {
    	    this.bossMoveTime=this.bossMoveTime==this.bossMovePeriod?0:this.bossMoveTime+1;
    	    if(this.bossMoveTime>100)
    	    this.moveYNum=0;
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
		if(!this.addStar)//增加积分
		{
		//爆炸
		ElementObj obj=GameLoad.getObj("file");  		
		ElementObj element = obj.createElement(//子弹json数据生成
        GameLoad.getFileString(this.getX()+this.getW()/2,this.getY()+this.getH()/2, 
           0,0,this.getKind().equals("9")?this.getCamp():3,5));//生成爆炸
		element.setExplodeMsg(40, 24, 3);
		ElementManager.getManager().addElement(element, GameElement.PLAYFILE);
		//在此位置增加分数
		this.addStar=true;
		}
	}

	@Override
	protected void updateImage(long time) {
	    this.setIcon(GameLoad.imgMap.get("enemy"+this.getKind()));
	    if(this.getKind().equals("9")&&(time)%(shoot_interval*2)==0)//自爆战机随机自爆
	    	this.setLive(false);
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
	
		@Override   //发射子弹函数
	public void add(long gameTime) {
		//一定间隔发射子弹
			switch(this.getKind())
			{
			case "1"://海妖终结者
			this.shoot(1,new int[]{this.getX()+this.getW()/2,this.getY()+this.getH()}, 
			new int[] {0,3});
			break;
			}
	}
		
	
}
