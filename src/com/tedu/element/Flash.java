package com.tedu.element;

import java.util.List;
import javax.swing.ImageIcon;
import com.tedu.manager.ElementManager;
import com.tedu.manager.GameElement;
import com.tedu.manager.GameLoad;


public class Flash extends ElementObj{
	
	private int deleteTime=12; //闪光时间
	private int nuclear_attack=50; //核弹攻击为50
	private ElementManager em=ElementManager.getManager();
	//闪光道具的种类kind（1 is 核弹, 2 is 脉冲弹）
	
	@Override
	protected void updateImage(long time) {
		if(deleteTime>0)deleteTime--;
		if(deleteTime==0) {
			this.setLive(false);
			return;
		}
		else {
			if(deleteTime%2==5) this.setIcon(GameLoad.imgMap.get("flash1"));
			if(deleteTime%2==4) this.setIcon(GameLoad.imgMap.get("flash2"));
			if(deleteTime%2==3) this.setIcon(GameLoad.imgMap.get("flash3"));
			if(deleteTime%2==2) this.setIcon(GameLoad.imgMap.get("flash2"));
			if(deleteTime%2==1) this.setIcon(GameLoad.imgMap.get("flash1"));
			if(deleteTime%2==0) {
				this.setIcon(GameLoad.imgMap.get("flash0"));
				this.flash_attack();//闪光的攻击
					
			}
		}
	}
	
	//核弹轰炸敌人与boss
	private void flash_attack() {
		List<ElementObj> enemys = em.getElementsByKey(GameElement.ENEMY);
		List<ElementObj> bosses = em.getElementsByKey(GameElement.BOSS);
        for(ElementObj enemy:enemys)
        {
        	if(this.getKind().equals("1"))enemy.deductLive(nuclear_attack);//核弹轰炸敌人
        	else if(this.getKind().equals("2"))enemy.getEMP();//电磁脉冲敌人，无法运动，无法攻击
        }
        //轰炸boss(电磁脉冲对boss没有效果)
        for(ElementObj boss:bosses)
        {
        	if(this.getKind().equals("1"))boss.deductLive(nuclear_attack);//核弹轰炸boss
        }
	}
	
	 public Flash() {}
	 
	//kind表示闪光的种类
	 @Override
	   public ElementObj createElement(String kind) {
		 this.setX(0);
		 this.setY(0);
		 this.setKind(kind);
		 ImageIcon icon2=GameLoad.imgMap.get("flash0");
		 this.setW(icon2.getIconWidth());
		 this.setH(icon2.getIconHeight());
		 this.setIcon(icon2);
	   	 return this;
	   }
	
	//闪光种类函数
	@Override
	public String getKind() {
		return super.getKind();
	}
	
	@Override
	public void setKind(String kind) {
		super.setKind(kind);
	}
}
