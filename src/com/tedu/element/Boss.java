package com.tedu.element;

import com.tedu.controller.Stopwatch;
import com.tedu.manager.ElementManager;
import com.tedu.manager.GameElement;
import com.tedu.manager.GameLoad;
import com.tedu.show.GameJFrame;

import java.awt.*;

//boss攻击方式（子弹，数量，方向等等）
interface Boss_shoot {
    public void boss_shoot();//boss 攻击方式
}

public class Boss extends ElementObj {

    //种类：海妖终结者，巨霸魔牛，剧毒金蜥，紫魔君主，爆裂金刚，

    //private static Random ran=new Random(); //随机生成器
    private int bossMoveTime = 0;//boss移动时间,用于控制boss的移动
    private int bossMovePeriod = 2000;//boss移动，攻击的周期
    private int bossMoveRemainder;//boss移动时间周期余数
    //bulletKind子弹种类(1 普通子弹,2 散弹,3 导弹,4 激光, 5 爆炸, 6等离子球, 7boss红色子弹, 8boss蓝色子弹)

    public Boss() {
    }

    @Override
    public void showElement(Graphics g) {
        if (isShow) {
            super.showElement(g);
            showAttribute((Graphics2D) g);
        } else if (this.getCurrentGodTime() < 0) {
            isShow = true;
        }
        if (this.getCurrentGodTime() > 0 && (this.god_timer.sinceLast(0.2))) {
            isShow = !isShow;
        }
    }

    private void showAttribute(Graphics2D g) {
        int barWidth = this.getW() * 4 / 5;
        int barHeight = 25;
        int barX = (int) (this.getX() + this.getW() / 10);
        int barY = (int) (this.getY() - barHeight / 2);
        switch (this.getKind()) {
            case "1"://海妖终结者
                barY += 30;
                break;
            case "2"://巨霸魔牛
                break;
            case "3"://剧毒金蜥
                barY -= 5;
                break;
            case "4"://紫魔君主
                barY += 20;
                break;
        }
        Graphics2D g2 = g;
        for (int i = 0; i <= this.getRebornNum(); i++) {
            switch (i) {
                case 0:
                    g2.setColor(Color.RED);
                    break;
                case 1:
                    g2.setColor(Color.GREEN);
                    break;
                case 2:
                    g2.setColor(Color.ORANGE);
                    break;
                case 3:
                    g2.setColor(Color.magenta);
                    break;
            }
            if (i == this.getRebornNum()) {
                g2.fillRect(barX, barY, barWidth * this.getBlood() / this.getDensity(), barHeight);
            } else {
                g2.fillRect(barX, barY, barWidth, barHeight);
            }
        }
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(4.0f));
        g2.drawRect(barX, barY, barWidth, barHeight);
    }

    @Override
    public ElementObj createElement(String kind_str) {
        this.setKind(kind_str);//飞机种类
        this.setCamp(2);//设置为敌方的阵营


        //敌机种类不同属性初始化
        switch (this.getKind()) {
            case "1"://海妖终结者
                this.kindToBoss(320, 320, 200, 0, 1, 2400, 80, 0);
                break;
            case "2"://巨霸魔牛
                this.kindToBoss(320, 320, 250, 0, 1, 2000, 120, 1);
                break;
            case "3"://剧毒金蜥
                this.kindToBoss(320, 320, 300, 0, 1, 2400, 200, 2);
                break;
            case "4"://紫魔君主
                this.kindToBoss(320, 320, 400, 0, 1, 2400, 320, 3);
                break;
        }
        this.setX(GameJFrame.GameX / 2 - this.getW() / 2);//使得boss在正中间出现
        this.setY(0);
        this.setIcon(GameLoad.imgMap.get("boss" + this.getKind()));
        this.god_time = 1;//boss的无敌时间
        return this;
    }

    //boss种类初始化敌机属性函数(敌机大小,血量，运动方式(水平速度，垂直速度),运动周期设置)
    public void kindToBoss(int width, int height, int density, int moveXNum, int moveYNum, int period, int score, int rebornNum) {
        this.setW(width);
        this.setH(height);
        this.setDensity(density);
        this.setSpeed(moveXNum, moveYNum);
        this.bossMovePeriod = period;
        this.setScore(score);
        this.setRebornNum(rebornNum);
    }

    @Override
    protected void move(long gameTime) {
        this.bossMoveTime = this.bossMoveTime == this.bossMovePeriod ? 0 : this.bossMoveTime + 1;
        this.bossMoveRemainder = this.bossMoveTime % this.bossMovePeriod;//boss移动周期余数的计算
        if (this.bossMoveTime == 20)//出场之后不会向前移动
            this.setSpeed(this.getXSpeed(), 0);
        switch (this.getKind())//控制移动
        {
            case "1":
            case "3"://海妖终结者 //剧毒金蜥
                this.setBossSpeed(new int[]{150, 300, 400, 1300, 2300}, 0, 0);
                this.setBossSpeed(new int[]{100, 350}, -4, 0);
                this.setBossSpeed(new int[]{200}, 4, 0);
                this.setBossSpeed(new int[]{500, 1100}, -2, 0);
                this.setBossSpeed(new int[]{700}, 2, 0);
                this.setBossSpeed(new int[]{1500, 2100}, -1, 0);
                this.setBossSpeed(new int[]{1700}, 1, 0);
                break;
            case "2"://巨霸魔牛
                this.setBossSpeed(new int[]{150, 300, 400, 1300, 1900}, 0, 0);
                this.setBossSpeed(new int[]{100, 350, 1500, 1800}, -4, 0);
                this.setBossSpeed(new int[]{200, 1600}, 4, 0);
                this.setBossSpeed(new int[]{500, 1100}, -2, 0);
                this.setBossSpeed(new int[]{700}, 2, 0);
                break;
            case "4"://紫魔君主
                this.setBossSpeed(new int[]{400, 1100, 1500, 1700, 2000, 2200}, 0, 0);
                this.setBossSpeed(new int[]{100, 1200}, -3, 0);
                this.setBossSpeed(new int[]{500,}, 3, 0);
                this.setBossSpeed(new int[]{1600, 2100}, -5, 0);
                this.setBossSpeed(new int[]{1800,}, 5, 0);
        }

        this.setX(this.getX() + this.getXSpeed());
        this.setY(this.getY() + this.getYSpeed());
    }

    //在具体的时间设置boss速度
    public void setBossSpeed(int[] bossMoveRemainders, double moveXNum, double moveYNum) {
        for (int i = 0; i < bossMoveRemainders.length; i++)
            if (this.bossMoveRemainder == bossMoveRemainders[i]) {
                this.setSpeed(moveXNum, moveYNum);
                break;
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
                GameLoad.getFileString((int) (this.getX() + this.getW() / 2), (int) (this.getY() + this.getH() / 2),
                        0, 0, this.getKind().equals("9") ? this.getCamp() : 3, 5));//生成爆炸
        element.setExplodeMsg(40, 24, 3);
        ElementManager.getManager().addElement(element, GameElement.PLAYFILE);
        //在此位置增加分数

    }

    @Override
    protected void updateImage(long time) {
        this.setIcon(GameLoad.imgMap.get("boss" + this.getKind()));
    }

    @Override   //发射子弹函数
    public void add(long gameTime) {
        switch (this.getKind()) {
            case "1"://海妖终结者
                this.sea_destroyer_attack();
                break;
            case "2"://巨霸魔牛
                this.yellow_bull_attack();
                break;
            case "3"://剧毒金蜥
                this.golden_lizard_attack();
                break;
            case "4"://紫魔君主
                this.purple_sovereign_attack();
                break;
        }
    }

    //发射函数(子弹种类,子弹发射的位置)
    public void shoot(int bulletKind, double[] pos, double[] speed) {
        for (int i = 0; i < pos.length; i += 2)//pos[i]为横坐标,pos[i+1]为纵坐标,speed[i]为水平速度,speed[i+1]为垂直速度
        {
            ElementObj obj = GameLoad.getObj("file");
            ElementObj element = obj.createElement(//子弹json数据生成
                    GameLoad.getFileString(pos[i], pos[i + 1],
                            speed[i], speed[i + 1], this.getCamp(), bulletKind));
            ElementManager.getManager().addElement(element, GameElement.PLAYFILE);
        }
    }

    //boss发射函数，包含多个shoot,可以设置攻击时间的区间与间隔,以及攻击方式
    public void boss_shoot(int[] left_time, int[] right_time, int[] shoot_interval, Boss_shoot boss_shoot) {
        for (int i = 0; i < left_time.length; i++) {
            if (this.bossMoveRemainder >= left_time[i] && this.bossMoveTime < right_time[i]//攻击区间设置
                    && this.bossMoveTime % shoot_interval[i] == 0)//攻击间隔设置
            {
                boss_shoot.boss_shoot();
                break;
            }
        }
    }

    //boss1的攻击函数
    public void sea_destroyer_attack() {
        //双链弹
        this.boss_shoot(new int[]{50, 150, 300}, new int[]{100, 200, 350}, new int[]{10, 10, 10}, () -> {
            this.shoot(2, new double[]{this.getX() + this.getW() / 2 - 40, this.getY() + this.getH(),
                            this.getX() + this.getW() / 2 + 40, this.getY() + this.getH()},
                    new double[]{0, 3, 0, 3});
        });
        //平行双摆弹
        this.boss_shoot(new int[]{500}, new int[]{1300}, new int[]{30}, () -> {
            this.shoot(7, new double[]{this.getX() + this.getW() / 2 - 130, this.getY() + this.getH(),
                            this.getX() + this.getW() / 2 + 130, this.getY() + this.getH()},
                    new double[]{0, 4, 0, 4});
        });
        //三叉线性弹
        this.boss_shoot(new int[]{1500}, new int[]{2300}, new int[]{10}, () -> {
            this.shoot(2, new double[]{this.getX() + this.getW() / 2, this.getY() + this.getH(),
                            this.getX() + this.getW() / 2, this.getY() + this.getH(),
                            this.getX() + this.getW() / 2, this.getY() + this.getH()},
                    new double[]{-2, 3, 0, 3, 2, 3});
        });
    }


    //boss2的攻击函数
    public void yellow_bull_attack() {
        //圈弹
        this.boss_shoot(new int[]{50, 150, 300}, new int[]{100, 200, 340}, new int[]{30, 30, 30}, () -> {
            for (double i = 0; i <= 2.0; i += 0.1125) {
                this.shoot(2, new double[]{this.getX() + this.getW() / 2 + Math.cos(Math.PI * i) * 80,
                                this.getY() + this.getH() / 2 + Math.sin(Math.PI * i) * 80},
                        new double[]{3 * Math.cos(Math.PI * i), 3 * Math.sin(Math.PI * i)});
            }
        });

        //旋弹A
        this.boss_shoot(new int[]{560, 720, 880, 1040}, new int[]{570, 730, 890, 1050}, new int[]{80, 80, 80, 80}, () -> {
            for (double i = 0; i < 4; i++)
                for (double j = 0; j < 4; j++)
                    this.shoot(2, new double[]{this.getX() + this.getW() / 2 + Math.cos(Math.PI * i / 2.0 + j * 0.125) * (80 - j * 5),
                                    this.getY() + this.getH() / 2 + Math.sin(Math.PI * i / 2.0 + j * 0.125) * (80 - j * 5)},
                            new double[]{3 * Math.cos(Math.PI * i / 2.0 + j * 0.125), 3 * Math.sin(Math.PI * i / 2.0 + j * 0.125)});
        });
        //旋弹B
        this.boss_shoot(new int[]{640, 800, 960, 1120}, new int[]{650, 810, 970, 1130}, new int[]{80, 80, 80, 80}, () -> {
            for (double i = 0; i < 4; i++)
                for (double j = 0; j < 4; j++)
                    this.shoot(2, new double[]{this.getX() + this.getW() / 2 + Math.cos(Math.PI * i / 2.0 - j * 0.125) * (80 - j * 5),
                                    this.getY() + this.getH() / 2 + Math.sin(Math.PI * i / 2.0 - j * 0.125) * (80 - j * 5)},
                            new double[]{3 * Math.cos(Math.PI * i / 2.0 - j * 0.125), 3 * Math.sin(Math.PI * i / 2.0 - j * 0.125)});
        });

        //摆弹
        this.boss_shoot(new int[]{1600}, new int[]{1900}, new int[]{80}, () -> {
            for (double i = -1.0; i <= 0; i += 0.1)
                this.shoot(2, new double[]{this.getX() + this.getW() / 2 + Math.cos(Math.PI * i) * 80,
                                this.getY() + this.getH() / 2 - Math.sin(Math.PI * i) * 80},
                        new double[]{3 * Math.cos(Math.PI * i), -3 * Math.sin(Math.PI * i)});
        });
    }

    //boss3的攻击函数
    public void golden_lizard_attack() {
        //跟踪弹
        this.boss_shoot(new int[]{50, 150, 300}, new int[]{100, 200, 350}, new int[]{20, 20, 20}, () -> {
            this.shoot(0, new double[]{this.getX() + this.getW() / 2 - 60, this.getY() + this.getH(),
                            this.getX() + this.getW() / 2 + 60, this.getY() + this.getH()},
                    new double[]{0, 5, 0, 5});
        });

        //魔弹飞雨
        this.boss_shoot(new int[]{500}, new int[]{1300}, new int[]{70}, () -> {
            for (double i = -0.8; i <= -0.19; i += 0.15)
                this.shoot(2, new double[]{this.getX() + this.getW() / 2,
                                this.getY() + this.getH()},
                        new double[]{3 * Math.cos(Math.PI * i), -3 * Math.sin(Math.PI * i)});
        });

        //双圈爆弹A
        this.boss_shoot(new int[]{1560, 1680, 1800, 1920}, new int[]{1580, 1700, 1820, 1940}, new int[]{20, 20, 20, 20},
                () -> {
                    for (double i = 0; i <= 2.0; i += 0.125) {
                        this.shoot(2, new double[]{this.getX() + Math.cos(Math.PI * i) * 60,
                                        this.getY() + this.getH() + Math.sin(Math.PI * i) * 60},
                                new double[]{3 * Math.cos(Math.PI * i), 3 * Math.sin(Math.PI * i)});
                    }
                });

        //双圈爆弹B
        this.boss_shoot(new int[]{1620, 1740, 1860, 1980}, new int[]{1640, 1760, 1880, 2000}, new int[]{20, 20, 20, 20}, () -> {
            for (double i = 0; i <= 2.0; i += 0.125) {
                this.shoot(2, new double[]{this.getX() + this.getW() + Math.cos(Math.PI * i) * 60,
                                this.getY() + this.getH() + Math.sin(Math.PI * i) * 60},
                        new double[]{3 * Math.cos(Math.PI * i), 3 * Math.sin(Math.PI * i)});
            }
        });
    }


    //boss4的攻击函数
    public void purple_sovereign_attack() {
        //反射光弹（右边）
        this.boss_shoot(new int[]{50}, new int[]{1500}, new int[]{60}, () -> {
            this.shoot(8, new double[]{this.getX() + this.getW() / 2, this.getY() + this.getH()},
                    new double[]{1, 2});
        });
        //反射光弹（左边）
        this.boss_shoot(new int[]{50}, new int[]{1500}, new int[]{60}, () -> {
            this.shoot(9, new double[]{this.getX() + this.getW() / 2, this.getY() + this.getH()},
                    new double[]{-1, 2});
        });
        //发射等离子球(三个)
        this.boss_shoot(new int[]{50}, new int[]{1500}, new int[]{150}, () -> {
            this.shoot(6, new double[]{this.getX() + this.getW() / 2 - 60, this.getY() + this.getH(),
                            this.getX() + this.getW() / 2, this.getY() + this.getH(),
                            this.getX() + this.getW() / 2 + 60, this.getY() + this.getH(),},
                    new double[]{-1, 3, 0, 5, 1, 3});
        });

        //导弹风暴(四发)
        this.boss_shoot(new int[]{1700}, new int[]{2300}, new int[]{100}, () -> {
            this.shoot(3, new double[]{this.getX() + this.getW() / 2 - 120, this.getY() + this.getH(),
                            this.getX() + this.getW() / 2 - 40, this.getY() + this.getH(),
                            this.getX() + this.getW() / 2 + 40, this.getY() + this.getH(),
                            this.getX() + this.getW() / 2 + 120, this.getY() + this.getH(),},
                    new double[]{0, 5, 0, 5, 0, 5, 0, 5});
        });

        //跟踪弹
        this.boss_shoot(new int[]{1700}, new int[]{2300}, new int[]{150}, () -> {
            this.shoot(0, new double[]{this.getX() + this.getW() / 2 - 80, this.getY() + this.getH(),
                            this.getX() + this.getW() / 2 + 80, this.getY() + this.getH()},
                    new double[]{0, 5, 0, 5});
        });
    }

    @Override
    public void reborn() {
//        super.reborn();
        this.setBlood(this.getDensity());
        this.setRebornNum(this.getRebornNum() - 1);//消耗复活心
        this.setLive(true);
    }
}
