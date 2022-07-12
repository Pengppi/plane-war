package com.tedu.element;

import java.util.List;
import com.tedu.manager.ElementManager;
import com.tedu.manager.GameElement;
import com.tedu.manager.GameLoad;
import com.tedu.show.GameJFrame;

public class PlayFile extends ElementObj{

	private int deleteTime=0;//消失的时间(只有deleteTime==0与isLive==false时才会消失)
	
	//爆炸参数
	private int explodeOriginRange=20;//爆炸原始范围
	private int explodeExpandRange=8;//爆炸增加范围
	private int explodeTime=7;//爆炸时间
	private int explodeRelayTime=1;//爆炸帧延长时间
	//kind子弹种类(1 子弹,2 散弹, 3 导弹， 4 激光, 5 爆炸, 6等离子, 7boss红色子弹, 8boss蓝色右边子弹, 9boss蓝色左边子弹, 0boss跟踪弹) 
	
	//子弹显示的更新
	@Override
	protected void updateImage(long time) {

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
			case "x": this.setX(Double.parseDouble(split2[1]));break;
			case "y":this.setY(Double.parseDouble(split2[1]));break;
			//子弹的水平与垂直速度
			case "hv":this.setSpeed(Double.parseDouble(split2[1]),this.getYSpeed());break;
			case "vv":this.setSpeed(this.getXSpeed(),Double.parseDouble(split2[1]));break;
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
		this.kindToFile(16,24,-8,-12,1);
		break;
		//散弹
		case "2":
		this.kindToFile(16,16,-8,-8,2);
		break;
		//导弹
		case "3":
		this.kindToFile(16,48,-8,-24,0);
		break;
		//激光
		case "4":
		this.deleteTime=3;//激光需要一定时间消失
		if(this.getCamp()==1)kindToFile(16,1500, -8,-1505,1);//主机的激光
		else this.kindToFile(16,1500, -8,5,1);	//敌机的激光
		break;
		//爆炸
		case "5"://当getCamp()为3时，表示飞机坠毁爆炸
		this.deleteTime=7;//爆炸需要一定时间消失
		this.kindToFile(explodeOriginRange,explodeOriginRange,
		-explodeOriginRange/2, -explodeOriginRange/2,this.getCamp()==3?0:1);
		break;
		case "6"://等离子球
		this.deleteTime=10000;
		this.kindToFile(40, 40, -20, -20, 1);
		break;
		case "7"://boss红色子弹
		this.kindToFile(20, 50, -10, -25, 2);
		break;
		case "8"://boss右边激光
		case "9"://boss左边激光
		this.kindToFile(60, 60, -30, -30, 3);
		case "0"://boss跟踪弹
		this.kindToFile(22, 22, -11, -11, 4);
		break;
		}
		return this;
	}
     
	//子弹种类初始化子弹属性函数
	public void kindToFile(int width,int height,double offsetX,double offsetY,int attack)
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
		
		if(this.getKind().equals("8")&&this.getX()+this.getW()>=GameJFrame.GameX)//右边子弹反弹
		{
			this.setSpeed(-1, this.getYSpeed());
			this.setKind("9");
			return;
		}
		if(this.getKind().equals("9")&&this.getX()<=0)//左边子弹反弹
		{
			this.setSpeed(1, this.getYSpeed());
			this.setKind("8");
			return;
		}
		//边界检测
		if(this.getX()<0 || this.getX()+this.getW()>GameJFrame.GameX || 
				this.getY() <0 || this.getY()+this.getH()>GameJFrame.GameY) {
			if(this.getKind().equals("6"))this.deleteTime=0;
			this.setLive(false);
			return;
		}
		//跟踪弹跟踪
		if(this.getKind().equals("0"))
		{
			List<ElementObj>plays = ElementManager.getManager().getElementsByKey(GameElement.PLAY);
			for(ElementObj play:plays)
			{
				if(play.getX()<this.getX())this.setSpeed(-3, this.getYSpeed());
				else if(play.getX()>this.getX())this.setSpeed(3, this.getYSpeed());
				break;
			}
		}
		
		this.setX(this.getX()+this.getXSpeed());
		this.setY(this.getY()+this.getYSpeed());
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
            GameLoad.getFileString(this.getX()+this.getW()/2,this.getY()+(this.getCamp()==1?0:this.getH()), 
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
