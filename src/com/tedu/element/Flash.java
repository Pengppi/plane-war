package com.tedu.element;

import java.awt.Graphics;
import java.util.List;

import javax.swing.ImageIcon;

import com.tedu.manager.ElementManager;
import com.tedu.manager.GameElement;
import com.tedu.manager.GameLoad;


public class Flash extends ElementObj{
	
	private int deleteTime=6; //闪光时间
	@Override
	protected void updateImage(long time) {
		if(deleteTime>0)deleteTime--;
		if(deleteTime==0) {
			this.setLive(false);
			return;
		}
		else {
			clear();
//			if(deleteTime==7) this.setIcon(GameLoad.imgMap.get("flash0"));
			if(deleteTime==6) this.setIcon(GameLoad.imgMap.get("flash1"));
			if(deleteTime==5) this.setIcon(GameLoad.imgMap.get("flash2"));
			if(deleteTime==4) this.setIcon(GameLoad.imgMap.get("flash3"));
			if(deleteTime==3) this.setIcon(GameLoad.imgMap.get("flash2"));
			if(deleteTime==2) this.setIcon(GameLoad.imgMap.get("flash1"));
			if(deleteTime==1) this.setIcon(GameLoad.imgMap.get("flash0"));
		}
	}
	
	//清除屏幕中的所有怪
	private void clear() {
		for (GameElement ge : GameElement.values()) {
		if (ge == GameElement.PLAY || ge == GameElement.DIE || ge==GameElement.MAPS) continue;
        List<ElementObj> list = ElementManager.getManager().getElementsByKey(ge);
        for (int i = list.size() - 1; i >= 0; i--) {
            ElementObj obj = list.get(i);//读取为基类
            list.remove(i);
        }
    }
	}
	
	
	 public Flash() {}
	 
	 public Flash(int x, int y, int w, int h, ImageIcon icon) {
	    super(x, y, w, h, icon);
	 }
	
	 @Override
	   public ElementObj createElement(String str) {
		 this.setX(0);
		 this.setY(0);
//		System.out.println(GameLoad.imgMap.get("bg1"));
		 ImageIcon icon2=GameLoad.imgMap.get(str);
		 this.setW(icon2.getIconWidth());
		 this.setH(icon2.getIconHeight());
		 this.setIcon(icon2);
	   	 return this;
	   }
	 
	@Override
	public void showElement(Graphics g) {
		// TODO Auto-generated method stub
		g.drawImage(this.getIcon().getImage(),
             this.getX(), this.getY(),
             this.getW(), this.getH(), null);
	}
	
}
