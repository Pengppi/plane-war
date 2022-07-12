package com.tedu.controller;

import java.util.List;
import java.util.Map;
import java.util.Random;

import com.tedu.element.Boss;
import com.tedu.element.ElementObj;
import com.tedu.element.Enemy;
import com.tedu.element.Play;
import com.tedu.element.Tool;
import com.tedu.element.Trap;
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
    private static ElementManager em;
    public static Random ran = new Random();//随机生成器
    public boolean on = false;//控制gameRun
    public GameJFrame gj;
    public static long gameTime = 0L;//游戏运行的时间


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
        GameLoad.loadMap(GameOverJPanel.GameGate);//不同的关卡设置不同的地图
        Play.isRank = new boolean[]{false, false, false, false};//升级初始化
        gameTime = 0L;//时间重启
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

            //敌人，道具，boss,陷阱产生函数
            hzfCallTool(String.valueOf(ran.nextInt(6) + 1), 100, 10000, 700);
            //hzfCallTool(String.valueOf(3), 100, 10000, 700);
            hzfCallBoss(ElementManager.getBossId(), 10100);
            //陷阱
            zzrCallTrap(ElementManager.getBossId().equals("1")?"1":String.valueOf(ran.nextInt(2)+1),
            		3500-Integer.parseInt(ElementManager.getBossId())*500,
            		6500+Integer.parseInt(ElementManager.getBossId())*500,
            		1500-Integer.parseInt(ElementManager.getBossId())*100);
            //设置boss预警时间
            zzrCallTrap("3",10000,10100,30);
            switch (ElementManager.getBossId()) {
                case "1":
                    hzfCallEnemy("1", 100, 1200, 250);
                    hzfCallEnemy("4", 800, 10000, 400);
                    hzfCallEnemy("7", 7000, 10000, 500);
                    break;
                case "2":
                    hzfCallEnemy("2", 100, 1200, 250);
                    hzfCallEnemy("6", 800, 10000, 400);
                    hzfCallEnemy("9", 4000, 10000, 500);
                    hzfCallEnemy("a", 7000, 10000, 500);
                    break;
                case "3":
                    hzfCallEnemy("3", 100, 1200, 250);
                    hzfCallEnemy("5", 800, 10000, 400);
                    hzfCallEnemy("0", 4000, 10000, 500);
                    hzfCallEnemy("b", 7000, 10000, 500);
                    break;
                case "4":
                    hzfCallEnemy("8", 100, 1200, 250);
                    hzfCallEnemy("c", 800, 10000, 400);
                    hzfCallEnemy("d", 4000, 10000, 500);
                    hzfCallEnemy("e", 7000, 10000, 500);
                    break;
                default:
                    break;
            }


            //***********************碰撞检测*********************************
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
                if (a.getCurrentGodTime() <= 0) {//无敌时间忽略碰撞
                    if (a.getCamp() + b.getCamp() == 3) {
                        a.deductLive(b.getAttack());
                        b.setLive(false);
                    }
                }
            });


            ElementPK(plays, enemies, (a, b) -> {//判断敌机与我方碰撞(双方直接死亡,有护盾则护盾失去)
                if (a.getCurrentGodTime() <= 0) {//无敌时间忽略碰撞
                    if (a.getShieldCurrentTime() <= 0)
                        a.setLive(false);
                    else a.setShield_time(null, 0);
                    b.setLive(false);
                }
            });


            ElementPK(plays, boss, (a, b) -> {//判断boss与我方碰撞(我方直接死亡，敌方重创50)
                if (a.getCurrentGodTime() <= 0) {//无敌时间忽略碰撞
                    a.setLive(false);
                    b.deductLive(50);
                }
            });

            ElementPK(plays, tools, (a, b) -> {//判断玩家战机与道具的碰撞
                //不同类型的道具效果不同
                switch (b.getKind()) {
                    case "1"://医疗包
                        a.setBlood(a.getDensity());
                        break;
                    case "2"://护盾
                        a.setShield_time(new Stopwatch(), 0);
                        break;
                    case "3"://弹药箱(可以获得核弹，脉冲弹)
                        int kind = ran.nextInt(7) + 2;
                        //int kind = 6;
                        if (kind == 6)//获得核弹
                            Tool.nuclear_count++;
                        else if (kind == 7)//获得脉冲弹
                            Tool.emp_count++;
                        else if (kind == 8)//获得浮游炮
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
                        this.diamondToScore(5);
                        break;
                }
                b.setLive(false);
            });
            //*************************************************************************

            try {
                sleep(10);//默认理解为 1秒刷新100次
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            gameTime++;
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
                    if (obj instanceof Play) {
                        if (obj.getCurrentGodTime() > 0) {
                            obj.setBlood(obj.getDensity());
                            obj.setLive(true);
                            break;
                        }
                        System.out.println("setReborn1:" + obj.getRebornNum());
                        if (obj.getRebornNum() > 0)//有复活心可以复活
                        {
                            obj.reborn();
                            break;//退出循环，不可能退出界面
                        } else {//没有复活心
                            on = false;
                            new GameOverJPanel(gj, false, all.get(GameElement.PLAY).get(0).getScore(), all.get(GameElement.MAPS).get(0).getIcon());
                        }
                    }
                    obj.die();
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
        List<ElementObj> plays = ElementManager.getManager().getElementsByKey(GameElement.PLAY);
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

    /**
     * 测试用的加载方法
     * 产生敌军飞机
     *
     * @param kind(飞机种类编号) leftTime rightTime(飞机出现的时间区间） interval(飞机出现的间隔)
     * @return
     */
    public static void hzfCallEnemy(String kind, int leftTime, int rightTime, int interval) {
        if (GameThread.gameTime >= leftTime && GameThread.gameTime < rightTime && GameThread.gameTime % interval == 0) {
            ElementObj obj = new Enemy().createElement(kind);
            em.addElement(obj, GameElement.ENEMY);
        }
    }

    //产生道具的函数
    public static void hzfCallTool(String kind, int leftTime, int rightTime, int interval) {
        if (GameThread.gameTime >= leftTime && GameThread.gameTime < rightTime && GameThread.gameTime % interval == 0) {
            ElementObj obj = new Tool().createElement(kind);
            em.addElement(obj, GameElement.TOOL);
        }
    }

    //产生boss的函数
    /*
     * @param bossKind(boss种类) time(boss出现的间隔)
     */
    public static void hzfCallBoss(String bossKind, int time) {
        if (GameThread.gameTime == time) {
            ElementObj obj = new Boss().createElement(bossKind);
            em.addElement(obj, GameElement.BOSS);
        }
    }
    
    /**
     * 产生陷阱
     * @param kind(陷阱编号)
     * @return
     */
    public static void zzrCallTrap(String kind, int leftTime, int rightTime, int interval) {
    	if (GameThread.gameTime >= leftTime && GameThread.gameTime < rightTime && GameThread.gameTime % interval == 0) {
            ElementObj obj = new Trap().createElement(kind);
            em.addElement(obj, GameElement.TRAP);
    	}
    }

    //清空游戏时间，开启新关卡
    public static void clearGameTime() {
        gameTime = 0;
    }

}





