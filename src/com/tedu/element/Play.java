package com.tedu.element;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;

import com.tedu.manager.ElementManager;
import com.tedu.manager.GameElement;
import com.tedu.manager.GameLoad;
import com.tedu.show.GameJFrame;

public class Play extends ElementObj /* implements Comparable<Play>*/ {
    /**
     * @问题 1.图片要读取到内存中： 加载器  临时处理方式，手动编写存储到内存中
     * 2.什么时候进行修改图片(因为图片是在父类中的属性存储)
     * 3.图片应该使用什么集合进行存储
     */
    private int tx;
    private int ty;
    //子弹种类(1 普通子弹,2 散弹,3 导弹,4 激光,5 等离子球)
    private int shoot_interval = 80;//射击间隔,单发为100
    private int weapon_kind = 8;//武器种类
    private int weapon_count = 8;//武器种类总数

    private int full_blood = 10;//满血值

    private int rank = 1;//等级，玩家等级会因吃到升级道具而改变

    public Play() {
    }

    public Play(int x, int y, int w, int h, ImageIcon icon) {
        super(x, y, w, h, icon);
    }


    @Override
    public ElementObj createElement(String str) {
        //玩家飞机信息格式解析：（水平位置，垂直位置，飞机种类）
        String[] split = str.split(",");
        this.setX(Integer.parseInt(split[0]));
        this.setY(Integer.parseInt(split[1]));
        this.setKind(split[2]);
        this.setCamp(1);//设置为我方的阵营
        ImageIcon icon2 = GameLoad.imgMap.get("play" + this.getKind());
        this.setW(icon2.getIconWidth());
        this.setH(icon2.getIconHeight());
        this.setIcon(icon2);
        //设置防御力
        this.setDensity(3);
        //设置玩家初始血量为满血值
        this.setBlood(full_blood);
        //设置玩家初始等级为1
        this.setRank(1);
        return this;
    }

    /**
     * @description: 绘制飞机图片和血条以及得分
     * @method: showElement
     * @params: [g]
     * @return: void
     * @author: Neo
     * @date: 2022/7/9/009 7:52:50 上午
     **/
    @Override
    public void showElement(Graphics g) {
//		绘画图片
        g.drawImage(this.getIcon().getImage(),
                this.getX(), this.getY(),
                this.getW(), this.getH(), null);
        g.setFont(new Font("微软雅黑",Font.PLAIN+Font.BOLD,60));
        g.drawString("当前得分：" + this.getScore(),0,60);
        int bloodBarWidth = this.getW() / 4 * 3;
        int bloodBarHeight = 10;
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.RED);
        g2.fillRect(this.getX() + this.getW() / 8, this.getY() + this.getH(), bloodBarWidth * this.getBlood() / this.full_blood, bloodBarHeight);
        g2.setStroke(new BasicStroke(2.0f));
        g2.setColor(Color.BLACK);
        g2.drawRect(this.getX() + this.getW() / 8, this.getY() + this.getH(), bloodBarWidth, bloodBarHeight);

    }

    /*
     * @说明 重写方法： 重写的要求：方法名称 和参数类型序列 必须和父类的方法一样
     * @重点 监听的数据需要改变状态值
     */
    @Override   // 注解 通过反射机制，为类或者方法或者属性 添加的注释(相当于身份证判定)
    public void mouseMove(int tx, int ty) {
        int x = tx - this.getW() / 2;
        int y = ty - this.getH() / 2;
        if (x < 0) {
            x = 0;
        }
        if (x > GameJFrame.GameX - this.getW()) {
            x = GameJFrame.GameX - this.getW();
        }
        if (y < 0) {
            y = 0;
        }
        if (y > GameJFrame.GameY - this.getH()) {
            y = GameJFrame.GameY - this.getH();
        }
        this.tx = x;
        this.ty = y;
    }

    //玩家等级会因吃到升级道具而升级,而因等级改变至一定程度又会改变武器种类
    public void changeKind() {
        if (rank % 5 == 0)
            //因等级上张，而子弹种类会变得更加高级，直到达到最高级的子弹种类后不变
            this.weapon_kind = this.weapon_kind == this.weapon_count ? this.weapon_kind : this.weapon_kind + 1;
    }

    //private int test;
    //键盘事件，按f切换武器
    @Override
    public void keyClick(boolean bl, int key) {
        if (bl) {
            switch (key) {
                case 70://切换武器f键
                    this.weapon_kind = this.weapon_kind == this.weapon_count ? 1 : this.weapon_kind + 1;
                    break;
                case 90: //闪光道具的使用z键
                    ElementObj obj = GameLoad.getObj("flash");
                    ElementObj element = obj.createElement("1");
                    ElementManager.getManager().addElement(element, GameElement.DIE);
                    break;
            }
        }
    }

    @Override
    public void move(long gameTime) {
        this.setX(tx);
        this.setY(ty);
    }

    protected void updateImage() {
        this.setIcon(GameLoad.imgMap.get("play" + this.getKind()));
    }

    //发射函数(子弹种类,子弹发射的位置,子弹速度,发射间隔)
    public void shoot(int bulletKind, int[] pos, int[] speed, int shoot_interval) {
        this.setInterval(shoot_interval);
        for (int i = 0; i < pos.length; i += 2)//pos[i]为横坐标,pos[i+1]为纵坐标,speed[i]为水平速度,speed[i+1]为垂直速度
        {
            ElementObj obj = GameLoad.getObj("file");
            ElementObj element = obj.createElement(//子弹json数据生成
                    GameLoad.getFileString(pos[i], pos[i + 1],
                            speed[i], speed[i + 1], this.getCamp(), bulletKind));
            ElementManager.getManager().addElement(element, GameElement.PLAYFILE);
        }
    }

    //设置发射间隔
    public void setInterval(int shoot_interval) {
        this.shoot_interval = shoot_interval;
    }

    @Override   //发射子弹函数
    public void add(long gameTime) {
        //一定间隔发射子弹
        if ((gameTime + 2) % this.shoot_interval == 0) {
            switch (this.weapon_kind) {

                case 1:
                    //发射普通子弹
                    shoot(1, new int[]{this.getX() + this.getW() / 2, this.getY()},
                            new int[]{0, -3}, 80);
                    break;
                case 2:
                    //发射双发子弹
                    this.shoot(1,
                            new int[]{this.getX() + this.getW() / 2 - 10, this.getY(),
                                    this.getX() + this.getW() / 2 + 10, this.getY()},
                            new int[]{0, -3, 0, -3}, 80);
                    break;
                case 3:
                    //发射散弹
                    this.shoot(2,
                            new int[]{this.getX() + this.getW() / 2 - 5, this.getY(),
                                    this.getX() + this.getW() / 2, this.getY(),
                                    this.getX() + this.getW() / 2 + 5, this.getY()},
                            new int[]{-1, -3, 0, -3, 1, -3}, 80);
                    break;
                case 4://机枪
                    shoot(1, new int[]{this.getX() + this.getW() / 2, this.getY()},
                            new int[]{0, -3}, 20);
                    break;
                case 5://双重机枪
                    this.shoot(1,
                            new int[]{this.getX() + this.getW() / 2 - 10, this.getY(),
                                    this.getX() + this.getW() / 2 + 10, this.getY()},
                            new int[]{0, -3, 0, -3}, 20);
                    break;
                case 6:
                    //发射激光
                    shoot(4, new int[]{this.getX() + this.getW() / 2, this.getY()},
                            new int[]{0, 0}, 80);
                    break;
                case 7:
                    //发射导弹
                    shoot(3, new int[]{this.getX() + this.getW() / 2, this.getY() - 10},
                            new int[]{0, -7}, 80);
                    break;
                case 8://等离子球(发射间隔较长，为120ms)
                    shoot(6, new int[]{this.getX() + this.getW() / 2, this.getY()},
                            new int[]{0, -2}, 120);
                    break;
            }

        }
    }

    @Override
    public void setLive(boolean live) {
        super.setLive(live);
        if (!this.isLive()) {
            this.die();//调用死亡函数(如爆炸)
        }
    }

    @Override
    public void die() {
        //爆炸
        ElementObj obj = GameLoad.getObj("file");
        ElementObj element = obj.createElement(//子弹json数据生成
                GameLoad.getFileString(this.getX() + this.getW() / 2, this.getY() + this.getH() / 2,
                        0, 0, 3, 5));//生成爆炸
        element.setExplodeMsg(30, 12, 2);
        ElementManager.getManager().addElement(element, GameElement.PLAYFILE);
    }


    public void setRank(int rank) {
        this.rank = rank;
    }

}







