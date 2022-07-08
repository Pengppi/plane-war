package com.tedu.controller;

import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;

import com.tedu.element.ElementObj;
import com.tedu.element.Play;
import com.tedu.manager.ElementManager;
import com.tedu.manager.GameElement;
import com.tedu.manager.GameLoad;

/**
 * @author renjj
 * @说明 游戏的主线程，用于控制游戏加载，游戏关卡，游戏运行时自动化
 * 游戏判定；游戏地图切换 资源释放和重新读取。。。
 * @继承 使用继承的方式实现多线程(一般建议使用接口实现)
 */
//碰撞处理接口
interface Collide
{
	public void collide(ElementObj obj1,ElementObj obj2);
}

public class GameThread extends Thread {
    private ElementManager em;

    public GameThread() {
        em = ElementManager.getManager();
    }

    @Override
    public void run() {//游戏的run方法  主线程
        while (true) { //扩展,可以讲true变为一个变量用于控制结束
//		游戏开始前   读进度条，加载游戏资源(场景资源)
            gameLoad();
//		游戏进行时   游戏过程中
            gameRun();
//		游戏场景结束  游戏资源回收(场景资源)
            gameOver();
            try {
                sleep(50);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /**
     * 游戏的加载
     */
    private void gameLoad() {
        /**
         * wpp为了测试飞机移动暂时而加的loadPlay不属于最终的生成方式
         */
    	GameLoad.loadImg();//加载图片
    	GameLoad.loadObj();//加载对象
        GameLoad.wpploadPlay();//加载玩家飞机
        //GameLoad.hzfloadEnemey(new String[] {"1","4","2","4","3","4","4","4","5","4","6","4","7","4"});//加载敌军飞机
        GameLoad.hzfloadEnemey(new String[] {"e","3"});


//		GameLoad.loadImg(); //加载图片
//		GameLoad.MapLoad(5);//可以变为 变量，每一关重新加载  加载地图
//		加载主角
//		GameLoad.loadPlay();//也可以带参数，单机还是2人
//		加载敌人NPC等

//		全部加载完成，游戏启动
    }



    /**
     * @说明 游戏进行时
     * @任务说明 游戏过程中需要做的事情：1.自动化玩家的移动，碰撞，死亡
     * 2.新元素的增加(NPC死亡后出现道具)
     * 3.暂停等等。。。。。
     * 先实现主角的移动
     */

    private void gameRun() {
        long gameTime = 0L;//给int类型就可以啦
        while (true) {// 预留扩展   true可以变为变量，用于控制管关卡结束等
            Map<GameElement, List<ElementObj>> all = em.getGameElements();
            List<ElementObj> enemys = em.getElementsByKey(GameElement.ENEMY);
            List<ElementObj> files = em.getElementsByKey(GameElement.PLAYFILE);
            //List<ElementObj> maps = em.getElementsByKey(GameElement.MAPS);
            List<ElementObj> plays = em.getElementsByKey(GameElement.PLAY);
            moveAndUpdate(all, gameTime);//	游戏元素自动化方法

            ElementPK(enemys, files, (a,b)->{//判断我方的子弹与敌人碰撞
            	if(a.getCamp()+b.getCamp()==3) 
                 {a.deductLive(b.getAttack()); b.setLive(false);}
            	});
            //ElementPK(files, maps, (a,b)->{a.setLive(false); b.setLive(false);});
            
            ElementPK(plays, files, (a,b)->{//判断敌人的子弹与我方碰撞
            	if(a.getCamp()+b.getCamp()==3)
            	{a.deductLive(b.getAttack()); b.setLive(false);}
            	});
            
            ElementPK(plays, enemys, (a,b)->{//判断敌机与我方碰撞(双方直接死亡)
            	a.setLive(false); b.setLive(false);
            	});
            try {
                sleep(10);//默认理解为 1秒刷新100次
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            gameTime++;
        }
    }

    public void ElementPK(List<ElementObj> listA, List<ElementObj> listB,Collide collide) {
//		请大家在这里使用循环，做一对一判定，如果为真，就设置2个对象的死亡状态
        for (int i = 0; i < listA.size(); i++) {
            ElementObj enemy = listA.get(i);
            for (int j = 0; j < listB.size(); j++) {
                ElementObj file = listB.get(j);
                if (enemy.pk(file)) {
 
                	collide.collide(enemy,file);
 
                }
            }
        }
    }


    //	游戏元素自动化方法
    public void moveAndUpdate(Map<GameElement, List<ElementObj>> all, long gameTime) {
//		GameElement.values();//隐藏方法  返回值是一个数组,数组的顺序就是定义枚举的顺序
        for (GameElement ge : GameElement.values()) {
            List<ElementObj> list = all.get(ge);
            for (int i = list.size() - 1; i >= 0; i--) {
                ElementObj obj = list.get(i);//读取为基类
                if (!obj.isLive()) {//死亡
                    list.remove(i);
                    continue;
                }
                obj.model(gameTime);//调用的模板方法 不是move
            }
        }
    }


    /**
     * 游戏切换关卡
     */
    private void gameOver() {

    }

}





