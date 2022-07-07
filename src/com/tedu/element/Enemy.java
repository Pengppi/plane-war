package com.tedu.element;

import java.awt.Graphics;
import java.util.Random;

import com.tedu.manager.ElementManager;
import com.tedu.manager.GameElement;
import com.tedu.manager.GameLoad;
import com.tedu.show.GameJFrame;

public class Enemy extends ElementObj{
	
    private static Random ran=new Random(); //随机生成器
    private boolean addStar=false;//防止重复加分
	private int shoot_interval=200;//射击间隔ֵ
	private int moveXNum=0;//水平移动距离
	private int moveYNum=1;//垂直移动距离
	private int bulletKind=1;//子弹种类(1 普通子弹,2 导弹,3 激光, 4 等离子球)
	
	public Enemy() { }
	
	@Override
	public void showElement(Graphics g) {
		g.drawImage(this.getIcon().getImage(), 
				this.getX(), this.getY(), 
				this.getW(), this.getH(), null);
	}
	@Override
	public ElementObj createElement(String kind_str) {
		
		int x=ran.nextInt(GameJFrame.GameX-80)+40;//横坐标
		int y=ran.nextInt(20);//纵坐标
		this.setX(x);
		this.setY(y);
		this.setKind(kind_str);//飞机种类
		this.setCamp(2);//设置为敌方的阵营
		
		//敌机种类不同属性初始化
		switch(this.getKind())
		{
		case "1"://普通敌机(单发)
		this.kindToEnemy(60, 60, 2, 0, 1);
		break;
		case "2"://双发敌机(双发)
		this.kindToEnemy(60, 60, 3, 0, 1);
		break;
		}
		this.setIcon(GameLoad.imgMap.get("enemy"+this.getKind()));
		return this;
	}

	//敌机种类初始化敌机属性函数(敌机大小,血量，运动方式(水平速度，垂直速度))
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
		if(!this.addStar)//增加积分
		{
		//在此位置增加分数
		this.addStar=true;
		}
	}

	@Override
	protected void updateImage(long time) {
	    this.setIcon(GameLoad.imgMap.get("enemy"+this.getKind()));
	}
	
	//发射函数(子弹种类,子弹发射的位置)
	public void shoot(int bulletKind,int[]pos,int[]speed)
	{
		this.bulletKind=bulletKind;
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
		if((gameTime+4)%this.shoot_interval==0)
		{	
			switch(this.getKind())
			{
			case "1":
				this.shoot(bulletKind,new int[]{this.getX()+this.getW()/2,this.getY()+this.getH()}, 
				new int[] {0,3});
				break;
			case "2":
				this.shoot(bulletKind,
				new int[]{this.getX()+this.getW()/2-10,this.getY()+this.getH(),
				this.getX()+this.getW()/2+10,this.getY()+this.getH()}, 
				new int[] {0,3,0,3});
				break;
			}
			
		}
	}
		
		
}
