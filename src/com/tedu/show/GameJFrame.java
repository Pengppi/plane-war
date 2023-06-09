package com.tedu.show;

import com.tedu.controller.GameListener;
import com.tedu.controller.GameThread;
import com.tedu.manager.ElementManager;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

/**
 * @author renjj
 * @说明 游戏窗体 主要实现的功能：关闭，显示，最大最小化
 * @功能说明 需要嵌入面板, 启动主线程等等
 * @窗体说明 swing awt  窗体大小（记录用户上次使用软件的窗体样式）
 * @分析 1.面板绑定到窗体
 * 2.监听绑定
 * 3.游戏主线程启动
 * 4.显示窗体
 */
public class GameJFrame extends JFrame {
    public static int GameX = 1920;//GAMEX
    public static int GameY = 1080;

    public boolean isReady = false;
    public static String GAMETITLE = "狂暴铁血战机";
    private JPanel jPanel = null; //正在现实的面板
    private KeyListener keyListener = null;//键盘监听
    private MouseMotionListener mouseMotionListener = null; //鼠标监听
    private MouseListener mouseListener = null;
    private GameThread thread = null;  //游戏主线程


    public GameJFrame() {
        init();
    }

    public void init() {
        this.setSize(GameX, GameY); //设置窗体大小
        this.setTitle(GAMETITLE);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//设置退出并且关闭
        this.setResizable(false);
        this.setLocationRelativeTo(null);//屏幕居中显示
        this.setUndecorated(true);
//		。。。。
    }

    /*窗体布局: 可以讲 存档，读档。button   给大家扩展的*/
    public void addButton() {
//		this.setLayout(manager);//布局格式，可以添加控件
    }

    /**
     * 启动方法
     */
    public void start() {
        if (jPanel != null) {
//            this.add(jPanel);
            this.setContentPane(jPanel);
        }
        if (keyListener != null) {
            this.addKeyListener(keyListener);
        }
        if (mouseMotionListener != null) {
            this.addMouseMotionListener(mouseMotionListener);
        }
        if (mouseListener != null) {
            this.addMouseListener(mouseListener);
        }
        if (thread != null) {
//            thread.start();//启动线程
        }
//		this.show();
        this.setVisible(true);//显示界面
//		如果jp 是 runnable的 子类实体对象
//		如果这个判定无法进入就是 instanceof判定为 false 那么 jpanel没有实现runnable接口
        if (this.jPanel instanceof Runnable) {
//			已经做类型判定，强制类型转换不会出错
//			new Thread((Runnable)this.jPanel).start();
            Runnable run = (Runnable) this.jPanel;
            Thread th = new Thread(run);
            th.start();//
        }

    }


    /*set注入：等大家学习ssm 通过set方法注入配置文件中读取的数据;讲配置文件
     * 中的数据赋值为类的属性
     * 构造注入：需要配合构造方法
     * spring 中ioc 进行对象的自动生成，管理。
     * */
    public void setjPanel(JPanel jPanel) {
        this.jPanel = jPanel;
    }

    public void setKeyListener(KeyListener keyListener) {
        this.keyListener = keyListener;
    }

    public void setMouseMotionListener(MouseMotionListener mouseMotionListener) {
        this.mouseMotionListener = mouseMotionListener;
    }

    public void setMouseListener(MouseListener mouseListener) {
        this.mouseListener = mouseListener;
    }

    public void setThread(GameThread thread) {
        this.thread = thread;
    }

    /**
     * @description:
     * @method: loadButton
     * @params: [text, jp, x, y, gap]
     * @return: void
     * @author: Neo
     * @date: 2022/7/9/009 19:09:36 下午
     **/
    public void loadButton(String[] text, JPanel jp, int x, int y, int gapy, int height, int col) {
        int btnHeight = height;
        int btnY = 450 + y;
        int gapx = height * 10 / 3;
        GameButton[] btn = new GameButton[text.length];
        for (int i = 0; i < text.length; i += col) {
            int t = i;
            for (int j = 0; j < col && t < text.length; j++, t++) {
                int btnWidth = text[i].length() * btnHeight;
                int btnX = (GameJFrame.GameX - btnWidth) / 2 + x;
                if (i + col > text.length) {
                    btnX += btnWidth / 3;
                }

                btn[t] = new GameButton(text[t], btnX + j * gapx, btnY + gapy * i, btnWidth, btnHeight);
                jp.add(btn[t]);
                btn[t].setFocusable(false);
                btn[t].addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
//                    System.out.println("click");
                        if (e.getButton() == 1) {
                            JButton btn = (JButton) e.getSource();
                            String txt = btn.getText();
                            changePanel(txt);
                        }
                    }
                });
            }
        }
    }


    /**
     * @description: 更换窗体面板
     * @method: changePanel
     * @params: [txt]
     * @return: void
     * @author: Neo
     * @date: 2022/7/9/009 16:02:05 下午
     **/
    public void changePanel(String txt) {
        switch (txt) {
            case "开始游戏":
                this.jPanel.removeAll();
                String text[] = {"第一关", "第二关", "第三关", "第四关", "返回主界面"};
                loadButton(text, this.jPanel, 490, 220, 70, 95, 2);
                this.jPanel.repaint();
                break;
            case "退出游戏":
                System.exit(0);
                break;
            case "游戏记录":
                this.jPanel = null;
                new GameRecordJPanel(this);
                break;
            case "重玩关卡":
                GameMainJPanel jp = new GameMainJPanel();//实例化面板，注入到jframe中
                this.jPanel = null;
                this.setjPanel(jp);
                isReady = true;
                jp.setFocusable(true);
                ElementManager.setBossId(ElementManager.getBossId());
                GameThread.clearGameTime();
                this.start();
                break;
            case "下一关":
                jp = new GameMainJPanel();//实例化面板，注入到jframe中
                GameOverJPanel.GameGate = GameOverJPanel.GameGate == 4? 4 : GameOverJPanel.GameGate + 1;
                this.jPanel = null;
                this.setjPanel(jp);
                isReady = true;
                jp.setFocusable(true);
                ElementManager.setBossId(Integer.toString(Integer.parseInt(ElementManager.getBossId())+1));
                GameThread.clearGameTime();
                this.start();
                break;
            case "返回主界面":
                this.jPanel = null;
                new GameStartJPanel(this);
                break;
            case "第一关":
                jp = new GameMainJPanel();//实例化面板，注入到jframe中
                GameOverJPanel.GameGate = 1;
                this.jPanel = null;
                this.setjPanel(jp);
                isReady = true;
                jp.setFocusable(true);
                ElementManager.setBossId("1");
                GameThread.clearGameTime();
                this.start();
                break;
            case "第二关":
                jp = new GameMainJPanel();//实例化面板，注入到jframe中
                GameOverJPanel.GameGate = 2;
                this.jPanel = null;
                this.setjPanel(jp);
                isReady = true;
                jp.setFocusable(true);
                ElementManager.setBossId("2");
                GameThread.clearGameTime();
                this.start();
                break;
            case "第三关":
                jp = new GameMainJPanel();//实例化面板，注入到jframe中
                GameOverJPanel.GameGate = 3;
                this.jPanel = null;
                this.setjPanel(jp);
                isReady = true;
                jp.setFocusable(true);
                ElementManager.setBossId("3");
                GameThread.clearGameTime();
                this.start();
                break;
            case "第四关":
                jp = new GameMainJPanel();//实例化面板，注入到jframe中
                GameOverJPanel.GameGate = 4;
                this.jPanel = null;
                this.setjPanel(jp);
                isReady = true;
                jp.setFocusable(true);
                ElementManager.setBossId("4");
                GameThread.clearGameTime();
                this.start();
                break;
        }
    }

}





