package com.tedu.element;

import java.awt.Graphics;
import java.util.Random;

import javax.swing.ImageIcon;
import com.tedu.manager.ElementManager;
import com.tedu.manager.GameElement;
import com.tedu.manager.GameLoad;

public class Enemy extends ElementObj{
	
    private static Random ran=new Random(); //随机生成器
    private boolean addStar=false;//防止重复加分
	private int shoot_interval=200;//射击间隔ֵ
	private int moveXNum=0;//水平移动距离
	private int moveYNum=1;//垂直移动距离
	
	public Enemy() { }
	
	@Override
	public void showElement(Graphics g) {
		g.drawImage(this.getIcon().getImage(), 
				this.getX(), this.getY(), 
				this.getW(), this.getH(), null);
	}
	@Override
	public ElementObj createElement(String kind_str) {
		
		int x=ran.nextInt(600);//横坐标
		int y=ran.nextInt(20);//纵坐标
		this.setKind(kind_str);//飞机种类
		this.setX(x);
		this.setY(y);
		this.setW(40);
		this.setH(40);
			this.setIcon(new ImageIcon("image/enemy/"+this.getKind()+".png"));
		//this.setCamp(2);
		return this;
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
	
//		@Override   //发射子弹函数
	public void add(long gameTime) {
		//一定间隔发射子弹
		//System.out.println("time:"+gameTime);
		if((gameTime+4)%this.shoot_interval==0)
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
		
		//向下射
		x+=this.getW()/2-5;y+=this.getH();//位置偏移
		return "x:"+x+",y:"+y+",hv:0,vv:3,c:2";
	}
}
