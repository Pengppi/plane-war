package com.tedu.game;

import com.tedu.controller.GameListener;
import com.tedu.controller.GameThread;
import com.tedu.element.GameRecord;
import com.tedu.show.GameJFrame;
import com.tedu.show.GameMainJPanel;
import com.tedu.show.GameStartJPanel;

public class GameStart {
    /**
     * 程序的唯一入口
     */
    public static void main(String[] args) {
        GameThread gh = new GameThread();
        gh.start();
        GameJFrame gj = new GameJFrame();
        gj.setThread(gh);
        gh.gj = gj;
        GameListener listener = new GameListener();//实例化监听
        gj.setKeyListener(listener);
        gj.setMouseMotionListener(listener);
        new GameStartJPanel(gj);

    }

}

/**
 * 1.分析游戏，设计游戏的 配置文件格式，文件读取格式（load格式）
 * 2.设计游戏角色，分析游戏需求(抽象基于基类的继承)
 * 3.开发pojo类(Vo)....
 * 4.需要的方法就在父类中重写(如果父类不支持，可以采用修改父类)
 * 5.检查配置，完成对象的 load和add到Manage.
 * 6.碰撞等等细节代码。
 * <p>
 * web网页游戏
 */
















