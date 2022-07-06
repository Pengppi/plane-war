package com.tedu.element;

import java.awt.Color;
import java.awt.Graphics;

import com.tedu.show.GameJFrame;

public class PlayFile extends ElementObj{

	private int attack=1;//攻击力
	private int moveXNum=0;//水平移动距离
	private int moveYNum=3;//垂直移动距离
	private int bulletKind=1;//子弹种类(1 普通子弹,2 导弹,3 激光,4 等离子球)
	 
	@Override
	public void showElement(Graphics g) {	
		g.setColor(Color.red);// new Color(255,255,255)
		g.fillOval(this.getX(), this.getY(), this.getW(), this.getH());		
	}
	
	public PlayFile() {}
	@Override   //{X:3,y:5,f:up}
	public  ElementObj createElement(String str) {//�����ַ����Ĺ���
		String[] split = str.split(",");
		for(String str1 : split) {//X:3
			String[] split2 = str1.split(":");
			switch(split2[0]) {
			case "x": this.setX(Integer.parseInt(split2[1]));break;
			case "y":this.setY(Integer.parseInt(split2[1]));break;
			case "hv":this.moveXNum=Integer.parseInt(split2[1]);break;
			case "vv":this.moveYNum=Integer.parseInt(split2[1]);break;
			case "c":this.setCamp(Integer.parseInt(split2[1]));break;
			case "k":this.bulletKind=Integer.parseInt(split2[1]);break;
			}
		}
		//设置大小
		this.setW(10);
		this.setH(10);
		return this;
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
}
