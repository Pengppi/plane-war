package com.tedu.element;

import java.awt.Graphics;
import java.util.List;

import javax.swing.ImageIcon;

import com.tedu.controller.GameThread;
import com.tedu.manager.ElementManager;
import com.tedu.manager.GameElement;
import com.tedu.manager.GameLoad;
import com.tedu.show.GameJFrame;





public class Map extends ElementObj{

	 public boolean sflash = false; //是否展示闪光
	 public int mapKind = 1;
	 public Map() {
		 	this.setX(0);
			this.setY(0);
//			System.out.println(GameLoad.imgMap.get("bg1"));
		  	ImageIcon icon2=GameLoad.imgMap.get("bg1");
		   	this.setW(icon2.getIconWidth());
		   	this.setH(icon2.getIconHeight());
		   	this.setIcon(icon2);
	 }
	 public Map(int x, int y, int w, int h, ImageIcon icon) {
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
	



	public int getMapKind() {
		return this.mapKind;
	}
	public void setMapKind(int mapKind) {
		this.mapKind = mapKind;
	}

	// 展示闪光效果 (由某种所需闪光效果子弹调用)
//	public static void showflash() {
//		System.out.println(ElementManager.getManager().getElementsByKey(GameElement.DIE).size());
////		for (int i = 0;i < 5; i++) {
//			ElementObj obj=GameLoad.getObj("map");
//			ElementObj element = obj.createElement("flash" + 0);
//			ElementManager.getManager().addElement(element, GameElement.DIE);
////			try {
////				Thread.sleep(50);
////			} catch (InterruptedException e) {
////				// TODO Auto-generated catch block
////				e.printStackTrace();
////			}
////		}
////		for (GameElement ge : GameElement.values()) {
////			if (ge == GameElement.PLAY || ge == GameElement.DIE || ge==GameElement.MAPS) continue;
////            List<ElementObj> list = ElementManager.getManager().getElementsByKey(ge);
////            for (int i = list.size() - 1; i >= 0; i--) {
////                ElementObj obj = list.get(i);//读取为基类
////                list.remove(i);
////            }
////        }
////		List<ElementObj> dies = ElementManager.getManager().getElementsByKey(GameElement.DIE);
////		System.out.println(ElementManager.getManager().getElementsByKey(GameElement.DIE).size());
////		for (int i = 0; i >= 0; i--) {
////			dies.remove(i);
////			try {
////				Thread.sleep(50);
////			} catch (InterruptedException e) {
////				// TODO Auto-generated catch block
////				e.printStackTrace();
////			}
////		}
////		System.out.println(ElementManager.getManager().getElementsByKey(GameElement.DIE).size());
//	}

}
