package com.tedu.element;

import java.awt.Color;
import java.awt.Graphics;

import com.tedu.manager.ElementManager;
import com.tedu.manager.GameElement;
import com.tedu.manager.GameLoad;
import com.tedu.show.GameJFrame;

public class PlayFile extends ElementObj{

	private int moveXNum=0;//水平移动距离
	private int moveYNum=3;//垂直移动距离
	private int deleteTime=0;//消失的时间(只有deleteTime==0与isLive==false时才会消失)
	
	//爆炸参数
	private int explodeOriginRange=20;//爆炸原始范围
	private int explodeExpandRange=8;//爆炸增加范围
	private int explodeTime=7;//爆炸时间
	private int explodeRelayTime=1;//爆炸帧延长时间
	//kind子弹种类(1 子弹,2 散弹, 3 导弹， 4 激光, 5 爆炸, 6等离子) 
	
	@Override
	public void showElement(Graphics g) {	
//		绘画图片
		if(this.getIcon().getImage()!=null)
        g.drawImage(this.getIcon().getImage(),
                this.getX(), this.getY(),
                this.getW(), this.getH(), null);
	}
	
	//子弹显示的更新
	@Override
	protected void updateImage(long time) {
		
		//System.out.println(deleteTime);
		if(deleteTime>0)deleteTime--;
		if(this.getKind().equals("5"))//爆炸膨胀（一共8张图片轮流切换）
		{
			if(deleteTime==0)
			{
				this.setLive(false);
				return;
			}
			else {
			int range=explodeOriginRange+(explodeTime-(deleteTime/explodeRelayTime))*explodeExpandRange;
			if(deleteTime%explodeRelayTime==0)
			this.kindToFile(range,range ,-explodeExpandRange/2,-explodeExpandRange/2,1);
			this.setIcon(GameLoad.imgMap.get("bullet"+this.getKind()+(9-(deleteTime/explodeRelayTime))));
			}
		}
		else if(this.getKind().equals("6"))//等离子电弧特效（一共4张图片轮流切换）
		{
			this.setIcon(GameLoad.imgMap.get("bullet"+this.getKind()+((time%40)/10+(this.getCamp()==1?1:5))));
		}
		else  this.setIcon(GameLoad.imgMap.get("bullet"+this.getKind()+this.getCamp()));
		
	}
	
	public PlayFile() {}
	@Override   //{X:3,y:5,f:up}
	public  ElementObj createElement(String str) {//子弹的初始化
		
		//子弹配置信息的初始化，字符串信息解析
		String[] split = str.split(",");
		for(String str1 : split) {//X:3
			String[] split2 = str1.split(":");
			switch(split2[0]) {
			//子弹的坐标
			case "x": this.setX(Integer.parseInt(split2[1]));break;
			case "y":this.setY(Integer.parseInt(split2[1]));break;
			//子弹的水平与垂直速度
			case "hv":this.moveXNum=Integer.parseInt(split2[1]);break;
			case "vv":this.moveYNum=Integer.parseInt(split2[1]);break;
			//子弹的阵营属性(地方还是我方)
			case "c":this.setCamp(Integer.parseInt(split2[1]));break;
			//子弹的种类(1 普通子弹,2 导弹,3 激光,4 等离子球)kind
			case "k":this.setKind(split2[1]);break;
			}
		}

		this.setIcon(GameLoad.imgMap.get("bullet"+this.getKind()+this.getCamp()));
		//设置大小,攻击力,子弹偏移(让图片显示为发射点的中心)
		switch(this.getKind())
		{
		//子弹
		case "1":
		this.kindToFile(10,16,-5,-8,1);
		break;
		//散弹
		case "2":
		this.kindToFile(10,10,-5,-5,2);
		break;
		//导弹
		case "3":
		this.kindToFile(12,28,-6,-14,0);
		break;
		//激光
		case "4":
		this.deleteTime=3;//激光需要一定时间消失
		if(this.getCamp()==1)kindToFile(10,1000, -5,-1005,1);//主机的激光
		else this.kindToFile(10,1000, -5,5,1);	//敌机的激光
		break;
		//爆炸
		case "5"://当getCamp()为3时，表示飞机坠毁爆炸
		this.deleteTime=7;//爆炸需要一定时间消失
		this.kindToFile(explodeOriginRange,explodeOriginRange,
		-explodeOriginRange/2, -explodeOriginRange/2,this.getCamp()==3?0:1);
		break;
		case "6"://等离子球
		this.deleteTime=10000;
		this.kindToFile(30, 30, -15, -15, 2);
		break;
		}
		return this;
	}
     
	//子弹种类初始化子弹属性函数
	public void kindToFile(int width,int height,int offsetX,int offsetY,int attack)
	{
		this.setW(width);
		this.setH(height);
		this.setX(this.getX()+offsetX);
		this.setY(this.getY()+offsetY);
		this.setAttack(attack);
	}
	
    //子弹移动
	@Override
	protected void move(long gameTime) {
		
		//边界检测
		if(this.getX()<0 || this.getX()+this.getW()>GameJFrame.GameX || 
				this.getY() <0 || this.getY()+this.getH()>GameJFrame.GameY) {
			if(this.getKind().equals("6"))this.deleteTime=0;
			this.setLive(false);
			return;
		}
		this.setX(this.getX()+this.moveXNum);
		this.setY(this.getY()+this.moveYNum);
	}
	
	//子弹消失函数
	@Override
	public void setLive(boolean live) {
		if(this.deleteTime>0)
	    return ;
		
		if(!live&&this.getKind().equals("3"))//导弹爆炸
		{
			ElementObj obj=GameLoad.getObj("file");  		
			ElementObj element = obj.createElement(//子弹json数据生成
            GameLoad.getFileString(this.getX(),this.getY(), 
            		0,0,this.getCamp(),5));//生成爆炸
			ElementManager.getManager().addElement(element, GameElement.PLAYFILE);
		}
		super.setLive(live);	
	}
	
	//爆炸参数设置增加范围(爆炸初始直径范围，爆炸扩散长度（直径差）)默认为(20,8)
	@Override
	public void setExplodeMsg(int explodeOriginRange,int explodeExpandRange,int explodeRelayTime) {
		this.explodeOriginRange = explodeOriginRange;
		this.explodeExpandRange = explodeExpandRange;
		this.explodeRelayTime = explodeRelayTime;
		deleteTime*=explodeRelayTime;//时间的延长
	}
	
}
