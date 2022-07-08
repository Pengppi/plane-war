package com.tedu.controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.tedu.element.ElementObj;
import com.tedu.manager.ElementManager;
import com.tedu.manager.GameElement;

/**
 * @说明 监听类，用于监听用户的操作 KeyListener
 * @author renjj
 *
 */
public class GameListener implements KeyListener, MouseMotionListener {
	private ElementManager em=ElementManager.getManager();
	
	/*能否通过一个集合来记录所有按下的键，如果重复触发，就直接结束
	 * 同时，第1次按下，记录到集合中，第2次判定集合中否有。
	 *       松开就直接删除集合中的记录。
	 * set集合
	 * */

	@Override
	public void keyTyped(KeyEvent e) {
	}
	/**
	 *键盘事件，按f可以切换武器
	 */
	private Set<Integer>set=new HashSet<Integer>();
	@Override
	public void keyPressed(KeyEvent e) {
		int key=e.getKeyCode();
//		System.out.println("keypress" + key);
		if(set.contains(key))return ;
		set.add(key);
		
		List<ElementObj>play=em.getElementsByKey(GameElement.PLAY);
		for(ElementObj obj:play)
			obj.keyClick(true,e.getKeyCode());
	}
	/**松开*/
	@Override
	public void keyReleased(KeyEvent e) {
		if(!set.contains(e.getKeyCode())) {
			return;
		}
		set.remove(e.getKeyCode());//�Ƴ�����
		List<ElementObj>play=em.getElementsByKey(GameElement.PLAY);
		for(ElementObj obj:play)
			obj.keyClick(false,e.getKeyCode());
	}

	@Override
	public void mouseDragged(MouseEvent e) {
//        System.out.println("dragged");
		List<ElementObj> play = em.getElementsByKey(GameElement.PLAY);
		for (ElementObj obj : play) {
			obj.mouseMove(e.getX(), e.getY());
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {//控制器鼠标控制
		//System.out.println(e.getX() + " " + e.getY());
		List<ElementObj> play = em.getElementsByKey(GameElement.PLAY);
		for (ElementObj obj : play) {
			obj.mouseMove(e.getX(), e.getY());
		}
	}
}
