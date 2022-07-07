package com.tedu.element;

import java.awt.Color;
import java.awt.Graphics;

import com.tedu.manager.GameLoad;
import com.tedu.show.GameJFrame;

public class PlayFile extends ElementObj{

	private int moveXNum=0;//水平移动距离
	private int moveYNum=3;//垂直移动距离
	private int deleteTime=0;//消失的时间(只有deleteTime==0与isLive==false时才会消失)
	 
	@Override
	public void showElement(Graphics g) {	
//		绘画图片
		if(this.getIcon().getImage()!=null)
        g.drawImage(this.getIcon().getImage(),
                this.getX(), this.getY(),
                this.getW(), this.getH(), null);
	}
	
	@Override
	protected void updateImage(long time) {
		this.setIcon(GameLoad.imgMap.get("bullet"+this.getKind()+this.getCamp()));
		if(deleteTime>0)deleteTime--;
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
		this.setW(15);
		this.setH(25);
		break;
		//激光
		case "4":
		this.deleteTime=3;//激光需要一定时间消失
		this.kindToFile(10,1000, -5,-1005,1);	
		break;
		//等离子
		case "5":break;
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
		if(this.getX()<0 || this.getX() >GameJFrame.GameX || 
				this.getY() <0 || this.getY()>GameJFrame.GameY) {
			this.setLive(false);
			return;
		}
		this.setX(this.getX()+this.moveXNum);
		this.setY(this.getY()+this.moveYNum);
	}
	
	@Override
	public void setLive(boolean live) {
		if(this.deleteTime>0)
	    return ;
		super.setLive(live);
	}
	
}
