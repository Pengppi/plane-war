package com.tedu.controller;

import java.util.List;
import java.util.Map;
import java.util.Random;

import com.tedu.element.Boss;
import com.tedu.element.ElementObj;
import com.tedu.element.Play;
import com.tedu.element.Tool;
import com.tedu.manager.ElementManager;
import com.tedu.manager.GameElement;
import com.tedu.manager.GameLoad;
import com.tedu.show.GameJFrame;
import com.tedu.show.GameOverJPanel;

/**
 * @author renjj
 * @说明 游戏的主线程，用于控制游戏加载，游戏关卡，游戏运行时自动化
 * 游戏判定；游戏地图切换 资源释放和重新读取。。。
 * @继承 使用继承的方式实现多线程(一般建议使用接口实现)
 */
//碰撞处理接口
interface Collide {
    public void collide(ElementObj obj1, ElementObj obj2);
}

public class GameThread extends Thread {
    private ElementManager em;
    public static Random ran = new Random();//随机生成器
    public boolean on = false;//控制gameRun
    public GameJFrame gj;


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
        //System.out.println("gameLoad阶段");
        GameLoad.loadImg();//加载图片
        GameLoad.loadObj();//加载对象
        GameLoad.wpploadPlay();//加载玩家飞机(种类数量，间隔)
        GameLoad.hzfloadEnemy(new String[]{"1", "4", "2", "4", "3", "4", "4", "4", "5", "4", "6", "4", "7", "4"}, 2000);//加载敌军飞机
        //GameLoad.hzfloadEnemy(new String[] {"e","3"},2000);
        //GameLoad.hzfloadBoss("1");
        GameLoad.loadMap(2);
        //GameLoad.hzfloadBoss("1");
        GameLoad.hzfloadTool(new String[]{"3", "2", "5", "2"}, 1000);
        //GameLoad.hzfloadEnemy(new String[] {"e","3"});
        //GameLoad.hzfloadEnemy(new String[] {"1","4","2","4","3","4","4","4","5","4","6","4","7","4"});//加载敌军飞机
        //GameLoad.zzrloadTrap(new String[]{"1", "20", "2", "20"});//加载陷阱
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
        while (on) {// 预留扩展   true可以变为变量，用于控制管关卡结束等
            //System.out.println("gameRun阶段");
            Map<GameElement, List<ElementObj>> all = em.getGameElements();
            List<ElementObj> enemies = em.getElementsByKey(GameElement.ENEMY);
            List<ElementObj> files = em.getElementsByKey(GameElement.PLAYFILE);
            //List<ElementObj> maps = em.getElementsByKey(GameElement.MAPS);
            List<ElementObj> plays = em.getElementsByKey(GameElement.PLAY);
            List<ElementObj> boss = em.getElementsByKey(GameElement.BOSS);
            List<ElementObj> traps = em.getElementsByKey(GameElement.TRAP);
            List<ElementObj> tools = em.getElementsByKey(GameElement.TOOL);

            moveAndUpdate(all, gameTime);//	游戏元素自动化方法
            reduceTrapTime(traps);//减少陷阱警告时间
            ElementPK(enemies, files, (a, b) -> {//判断我方的子弹与敌人碰撞
                if (a.getCamp() + b.getCamp() == 3) {
                    a.deductLive(b.getAttack());
                    b.setLive(false);
                }
            });
            ElementPK(boss, files, (a, b) -> {//判断我方的子弹与boss碰撞
                if (a.getCamp() + b.getCamp() == 3) {
                    a.deductLive(b.getAttack());
                    b.setLive(false);
                }
            });
            //ElementPK(files, maps, (a,b)->{a.setLive(false); b.setLive(false);});

            ElementPK(plays, files, (a, b) -> {//判断敌人的子弹与我方碰撞
                if (a.getCamp() + b.getCamp() == 3) {
                    a.deductLive(b.getAttack());
                    b.setLive(false);
                }
            });


            ElementPK(plays, enemies, (a, b) -> {//判断敌机与我方碰撞(双方直接死亡,有护盾则护盾失去)
                if (a.getShieldCurrentTime() <= 0)
                    a.setLive(false);
                else a.setShieldCurrentTime(0);
                b.setLive(false);
            });


            ElementPK(plays, boss, (a, b) -> {//判断boss与我方碰撞(我方直接死亡，敌方重创50)
                a.setLive(false);
                b.deductLive(50);
            });

            ElementPK(plays, tools, (a, b) -> {//判断玩家战机与道具的碰撞
            	  //不同类型的道具效果不同
                switch (b.getKind()) {
                    case "1"://医疗包
                        a.setBlood(a.getDensity());
                        break;
                    case "2"://护盾
                        a.setFullShield();
                        break;
                    case "3"://弹药箱(可以获得核弹，脉冲弹)
                        int kind = ran.nextInt(8) + 1;
                    	//int kind = 8;
                        if(kind == 6)//获得核弹
                        Tool.nuclear_count++;
                        else if(kind == 7)//获得脉冲弹
                        Tool.emp_count++;
                        else if(kind == 8)//获得浮游炮
                        a.setTowerTime();
                        else
                        a.setBulletKind(kind);
                        break;
                    case "4"://升级
                        a.setAttackKind(a.getAttackKind() == ElementObj.attack_count ? a.getAttackKind() : a.getAttackKind() + 1);//攻击方式的升级
                        break;
                    case "5"://复活心
                        a.setRebornNum(a.getRebornNum() + 1);
                        break;
                    case "6"://宝石
                    	this.diamondToScore(10);
                    	break;
                }
                b.setLive(false);
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

    // 我方子弹与敌方子弹碰撞都设为死亡
    public void ElementPK(List<ElementObj> listA, List<ElementObj> listB) {
        for (int i = 0; i < listA.size(); i++) {
            ElementObj enemy = listA.get(i);
            for (int j = i + 1; j < listB.size(); j++) {
                ElementObj file = listB.get(j);
                if (enemy.pk(file) && enemy.getCamp() + file.getCamp() == 3) {
                    enemy.setLive(false);
                    file.setLive(false);
                }
            }
        }
    }

    public void ElementPK(List<ElementObj> listA, List<ElementObj> listB, Collide collide) {
//		请大家在这里使用循环，做一对一判定，如果为真，就设置2个对象的死亡状态
        for (int i = 0; i < listA.size(); i++) {
            ElementObj enemy = listA.get(i);
            for (int j = 0; j < listB.size(); j++) {
                ElementObj file = listB.get(j);
                if (enemy.pk(file)) {
                    collide.collide(enemy, file);
                }
            }
        }
    }


    //	游戏元素自动化方法
    public void moveAndUpdate(Map<GameElement, List<ElementObj>> all, long gameTime) {
      //GameElement.values();//隐藏方法  返回值是一个数组,数组的顺序就是定义枚举的顺序
        for (GameElement ge : GameElement.values()) {
            List<ElementObj> list = all.get(ge);
            for (int i = list.size() - 1; i >= 0; i--) {
                ElementObj obj = list.get(i);//读取为基类
                if (!obj.isLive()) {//如果死亡
                    obj.die();
                    if (obj instanceof Play) {
                    	System.out.println("setReborn1:"+obj.getRebornNum());
                    	if(obj.getRebornNum()>0)//有复活心可以复活
                    	{
                    		obj.setRebornNum(obj.getRebornNum()-1);//消耗复活心
                    		list.remove(i);
                    		GameLoad.wpploadPlay();//重新加载玩家飞机
                    		break;//退出循环，不可能退出界面
                    	}
                    	else {//没有复活心
                        on = false;
                        new GameOverJPanel(gj, false, all.get(GameElement.PLAY).get(0).getScore(), all.get(GameElement.MAPS).get(0).getIcon());
                    	}
                    }
                    addScore(all, obj);
                    if (obj instanceof Boss) {
                        on = false;
                        new GameOverJPanel(gj, true, all.get(GameElement.PLAY).get(0).getScore(), all.get(GameElement.MAPS).get(0).getIcon());
                    }
                    list.remove(i);
                    continue;
                }
                obj.model(gameTime);//调用的模板方法 不是move
            }
        }
    }

    /**
     * @description: 将死亡对象的分值加到play中
     * @method: addScore
     * @params: [all, obj]
     * @return: void
     * @author: Neo
     * @date: 2022/7/9/009 11:04:28 上午
     **/
    private void addScore(Map<GameElement, List<ElementObj>> all, ElementObj obj) {
        for (ElementObj play : all.get(GameElement.PLAY)) {
            play.setScore(play.getScore() + obj.getScore());
        }
    }
    
    /**
     * @description: 玩家获得宝石加分
     * @method: diamondToScore
     * @params: [score]
     * @return: void
     * @author: hzf
     * @date: 2022/7/10
     **/
    private void diamondToScore(int score) {
    	List<ElementObj>plays=ElementManager.getManager().getElementsByKey(GameElement.PLAY);
        for (ElementObj play : plays) {
            play.setScore(play.getScore() + score);
        }
    }

    public void reduceTrapTime(List<ElementObj> traps) {
        for (int i = 0; i < traps.size(); i++) {
            ElementObj trap = traps.get(i);
            trap.reduceTime();
            if (trap.getRestTime() == 0) trap.setLive(false);
            if (!trap.isLive()) {//如果警告时间已到
                //若这是陷阱警告死亡，即会产生对应的效果子弹出来
                trap.die();//需要大家自己补充
                traps.remove(i);
                continue;
            }
        }
    }

    /**
     * 游戏切换关卡
     */
    private void gameOver() {
        if (!on) {
            while (!gj.isReady) {   //阻断线程,直到进入游戏
                //System.out.println("gameOver阶段"); //wpp测试游戏阶段
                Map<GameElement, List<ElementObj>> all = em.getGameElements();
                for (GameElement ge : GameElement.values()) {
                    all.get(ge).clear();
                }
            }
            on = true;//启动gameRun()
        }

    }

}





