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
	//bulletKind子弹种类(1 普通子弹,2 散弹,3 导弹,4 激光, 5 等离子球)
	private int divideBulletTime=0;//分裂者分裂子弹的间隔
	
	public Enemy() { }
	
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
		this.kindToEnemy(60, 60, 3, 0, 1);
		break;
		case "2"://双发敌机(双发)
		this.kindToEnemy(60, 60, 5, 0, 1);
		break;
		case "3"://巨型敌机(散射)
		this.kindToEnemy(90, 90, 12, 0, 1);
		break;
		case "4"://疾速敌机(单发)
		this.kindToEnemy(60, 60, 3, 0, 2);
		break;
		case "5"://小型敌机(向左，向右随机)
		this.kindToEnemy(40, 40, 2, new Random().nextBoolean()?1:-1, 1);
		break;
		case "6"://分裂者(中间停下，向四个方向发射)
		this.kindToEnemy(60, 60, 4, 0, 1);
		//设置分裂的时间间隔
		this.divideBulletTime=ran.nextInt(2)+4;//[4-5]
		break;
		case "7"://机枪敌机（连射）
		setInterval(80);
		case "8"://激光敌机（发射激光）
		this.kindToEnemy(60, 60, 6, 0, 1);
		break;
		case "9"://自爆敌机（一定几率自爆）
		this.kindToEnemy(60, 60, 4, 0, 4);
		break;
		case "0"://导弹敌机（发射导弹）
		this.kindToEnemy(70, 70, 8, 0, 1);
		break;
		case "a":
		setInterval(80);
		case "c"://双重机枪精英敌机,双重激光精英敌机
		this.kindToEnemy(80, 80, 10, 0, 1);
		break;
		case "b"://三重重机枪敌机
		setInterval(100);
		this.kindToEnemy(80, 80, 12, 0, 1);
		break;
		case "e"://等离子敌机
		setInterval(300);
		case "d"://双重导弹敌机
		this.kindToEnemy(80, 80, 15, 0, 1);
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
		//爆炸
		ElementObj obj=GameLoad.getObj("file");  		
		ElementObj element = obj.createElement(//子弹json数据生成
        GameLoad.getFileString(this.getX()+this.getW()/2,this.getY()+this.getH()/2, 
           0,0,this.getKind().equals("9")?this.getCamp():3,5));//生成爆炸
		element.setExplodeMsg(30, 12,2);
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
		if((gameTime+4)%this.shoot_interval==0)
		{	
			switch(this.getKind())
			{
			case "1":case "4":case "5":case "7"://普通敌机,疾速敌机,小型敌机,机枪敌机
				this.shoot(1,new int[]{this.getX()+this.getW()/2,this.getY()+this.getH()}, 
				new int[] {0,3});
				break;
			case "2":case "a"://敌机队长,双重机枪精英敌机
				this.shoot(1,
				new int[]{this.getX()+this.getW()/2-10,this.getY()+this.getH(),
				this.getX()+this.getW()/2+10,this.getY()+this.getH()}, 
				new int[] {0,3,0,3});
				break;
			case "3"://巨型敌机
				this.shoot(2,
				new int[]{this.getX()+this.getW()/2-5,this.getY()+this.getH(),
				this.getX()+this.getW()/2,this.getY()+this.getH(),
				this.getX()+this.getW()/2+5,this.getY()+this.getH()},
				new int[] {-1,3,0,3,1,3});
				break;
			case "6"://分裂者
				//随机分裂子弹
				if((gameTime+4)%(this.shoot_interval*(this.divideBulletTime-1))==0)//飞机停止飞行
					this.moveYNum=0;
				else if((gameTime+4)%(this.shoot_interval*this.divideBulletTime)==0)//分裂子弹
				{
					this.shoot(1,
					new int[]{this.getX()+this.getW()/2,this.getY(),
					this.getX()+this.getW(),this.getY()+this.getH()/2,
					this.getX()+this.getW()/2,this.getY()+this.getH(),
					this.getX(),this.getY()+getH()/2},
					new int[] {0,-3,3,0,0,3,-3,0});
				}
				else if((gameTime+4)%(this.shoot_interval*(this.divideBulletTime+1))==0)//飞机继续飞行
					this.moveYNum=1;
				else //飞机射击
					this.shoot(1,new int[]{this.getX()+this.getW()/2,this.getY()+this.getH()}, 
					new int[] {0,3});
				break;
			case "8"://激光敌机
				    shoot(4,new int[] {this.getX()+this.getW()/2,this.getY()+this.getH()},
				    new int[] {0,0});
				  break;
			case "0"://导弹战机
				//发射导弹
				shoot(3, new int[] {this.getX()+this.getW()/2,this.getY()+this.getH()+10},
				new int[] {0,7});
				break;
			case "b"://三重重机枪精英敌机
				this.shoot(2,
				new int[]{this.getX()+this.getW()/2-15,this.getY()+this.getH(),
				this.getX()+this.getW()/2,this.getY()+this.getH(),
				this.getX()+this.getW()/2+15,this.getY()+this.getH()},
				new int[] {0,3,0,3,0,3});
				break;
			case "c"://双激光敌机
				shoot(4,new int[] {this.getX()+this.getW()/2-10,this.getY()+this.getH(),
				this.getX()+this.getW()/2+10,this.getY()+this.getH()},
				new int[] {0,0,0,0});
				break;
			case "d"://双重导弹敌机
				shoot(3, new int[] {this.getX()+this.getW()/2-10,this.getY()+this.getH()+10,
				this.getX()+this.getW()/2+10,this.getY()+this.getH()+10},
				new int[] {0,7,0,7});
				break;
			case "e"://等离子敌机
				shoot(6, new int[] {this.getX()+this.getW()/2,this.getY()+this.getH()+10},
				new int[] {0,2});
				break;
			}
			
		}
	}
		
		
}
