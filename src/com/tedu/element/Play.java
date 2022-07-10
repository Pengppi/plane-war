package com.tedu.element;

import java.awt.*;
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
    private int shoot_interval = 80;//射击间隔,单发为80

    private int rank = 1;//等级，玩家等级会因吃到升级道具而改变

    public static int useToolInterval = 100;//使用道具的时间间隔,放置重复使用

    public Play() {
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
        this.setDensity(10);// 满血值为10,不需要额外设置setblood
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
        //绘画图片
        super.showElement(g);
        g.setFont(new Font("微软雅黑", Font.PLAIN + Font.BOLD, 60));
        g.drawString("当前得分：" + this.getScore(), 0, 60);

        //血条显示
        int bloodBarWidth = this.getW() / 4 * 3;
        int bloodBarHeight = 10;
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.RED);
        g2.fillRect((int) (this.getX() + this.getW() / 8), (int) (this.getY() + this.getH()), bloodBarWidth * this.getBlood() / this.getDensity(), bloodBarHeight);
        g2.setStroke(new BasicStroke(2.0f));
        g2.setColor(Color.BLACK);
        g2.drawRect((int) (this.getX() + this.getW() / 8), (int) (this.getY() + this.getH()), bloodBarWidth, bloodBarHeight);
        g.setFont(new Font("", Font.BOLD, 40));
        //护盾显示
        if (this.getShieldCurrentTime() > 0) {
            g.drawImage(GameLoad.imgMap.get("shield").getImage(),
                    (int) this.getX(), (int) this.getY(),
                    this.getW(), this.getW(), null);
            this.setShieldCurrentTime(this.getShieldCurrentTime() - 1);
            g.drawString("不准确的护盾时间：" + this.getShieldCurrentTime(), 10, 880);
        }

        //显示浮游炮
        if(this.getTowerCurrentTime()>0)
        {
        g.drawImage(GameLoad.imgMap.get("tower1").getImage(),(int)(this.getX()+this.getW()/2-80-30),(int)(this.getY()+this.getH()/2-30),60,60,null);
        g.drawImage(GameLoad.imgMap.get("tower1").getImage(),(int)(this.getX()+this.getW()/2+80-30),(int)(this.getY()+this.getH()/2-30),60,60,null);
        }
        
        //道具数目的显示
        drawTools(g, 8, Tool.emp_count, 8, 940);//显示脉冲弹数目
        drawTools(g, 7, Tool.nuclear_count, 10, 990);//显示核弹数目
        drawTools(g, 5, this.getRebornNum(), 10, 1040);//显示复活心的数目
        
        //子弹时间的减少
        if (this.getBulletTime() > 0) {
            this.setBulletTime(this.getBulletTime() - 1);
            if (this.getBulletKind() > 1) {
                g.drawString("不准确的子弹时间：" + this.getBulletTime()/300, 10, 930);
            }
        }
   
        //使用道具冷却时间的减少
        if(useToolInterval>0)useToolInterval--;
        
        //浮游炮使用时间的减少
        if(this.getTowerCurrentTime()>0)this.setTowerCurrentTime(this.getTowerCurrentTime()-1);
    }

    /**
     * @description: 在左下角根据道具数量绘制道具图片，目前还核弹与EMP的数量设置还没完全实现
     * @method: drawTools
     * @params: [g, order：tool图片序号, num道具数量, x, y]
     * @return: void
     * @author: Neo
     * @date: 2022/7/10/010 15:59:58 下午
     **/
    private void drawTools(Graphics g, int order, int num, int x, int y) {
        Image img = GameLoad.imgMap.get("tool" + order).getImage();
        int w = 45;
        int h = 45;
        int gap = w / 5 + w;
        for (int i = 0; i < num; i++) {
            g.drawImage(img, x + i * gap, y, w, h, null);
        }
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
    public void RankToAttackKind() {
        if (rank % 5 == 0)
            //因等级上张，而子弹种类会变得更加高级，直到达到最高级的子弹种类后不变
            this.setAttackKind(this.getAttackKind() == ElementObj.attack_count ? this.getAttackKind() : this.getAttackKind() + 1);
    }

    //键盘事件，按f切换武器
    @Override
    public void keyClick(boolean bl, int key) {
    	 if (bl) {
             switch (key) {
                 case 70://切换武器f键
                     //升级玩家主机的攻击方式
                     this.setAttackKind(this.getAttackKind() == ElementObj.attack_count ? this.getAttackKind() : this.getAttackKind() + 1);
                     break;
                 case 88://脉冲弹道具的使用x键
                 	if(useToolInterval>0||Tool.emp_count<=0) break;
                 	ElementObj obj = GameLoad.getObj("flash");
                     ElementObj element = obj.createElement("2");
                     ElementManager.getManager().addElement(element, GameElement.DIE);
                     Tool.emp_count--;
                     useToolInterval = 100;
                     break;
                 case 90: //核弹道具的使用z键
                 	if(useToolInterval>0||Tool.nuclear_count<=0) break;
                     ElementObj obj2 = GameLoad.getObj("flash");
                     ElementObj element2 = obj2.createElement("1");
                     ElementManager.getManager().addElement(element2, GameElement.DIE);
                     Tool.nuclear_count--;
                     useToolInterval = 100;
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
    public void shoot(int bulletKind, double[] pos, double[] speed) {
        if (bulletKind > 1 && this.getBulletTime() <= 0) {
            this.setBulletKind(1);
            return;//特殊子弹使用期限结束
        }
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
            //System.out.println("attackKind:"+this.getAttackKind()+",bulletKind:"+this.getBulletKind());
            switch (this.getAttackKind())//(1 is 单发, 2 is 双发, 3 is 机枪, 4 is 双重机枪)
            {
                case 1:
                case 3://单发 机枪
                    setInterval(this.getAttackKind() == 1 ? 80 : 20);
                    switch (this.getBulletKind()) {
                        case 1://发射普通子弹
                            shoot(1, new double[]{this.getX() + this.getW() / 2, this.getY()},
                                    new double[]{0, -3});
                            break;
                        case 2://发射散弹
                            this.shoot(2,
                                    new double[]{
                                            this.getX() + this.getW() / 2 - 5, this.getY(),
                                            this.getX() + this.getW() / 2, this.getY(),
                                            this.getX() + this.getW() / 2 + 5, this.getY()},
                                    new double[]{-0.5, -3, 0, -3, 0.5, -3});
                            break;
                        case 3://发射导弹
                            shoot(3, new double[]{this.getX() + this.getW() / 2, this.getY() - 10},
                                    new double[]{0, -7});
                            break;
                        case 4://发射激光
                            shoot(4, new double[]{this.getX() + this.getW() / 2, this.getY()},
                                    new double[]{0, 0});
                            break;
                        case 5://发射等离子球
                            shoot(6, new double[]{this.getX() + this.getW() / 2, this.getY()},
                                    new double[]{0, -2});
                            break;
                    }
                    break;
                case 2:
                case 4:
                    setInterval(this.getAttackKind() == 2 ? 80 : 20);//双发 双重机枪
                    switch (this.getBulletKind()) {
                        case 1://发射普通子弹
                            this.shoot(1,
                                    new double[]{this.getX() + this.getW() / 2 - 10, this.getY(),
                                            this.getX() + this.getW() / 2 + 10, this.getY()},
                                    new double[]{0, -3, 0, -3});
                            break;
                        case 2://发射散弹
                            this.shoot(2,
                                    new double[]{
                                            this.getX() + this.getW() / 2 - 15, this.getY(),
                                            this.getX() + this.getW() / 2 - 10, this.getY(),
                                            this.getX() + this.getW() / 2 - 5, this.getY(),
                                            this.getX() + this.getW() / 2 + 5, this.getY(),
                                            this.getX() + this.getW() / 2 + 10, this.getY(),
                                            this.getX() + this.getW() / 2 + 15, this.getY()},
                                    new double[]{-0.5, -3, 0, -3, 0.5, -3,
                                            -0.5, -3, 0, -3, 0.5, -3});
                            break;
                        case 3://发射导弹
                            shoot(3, new double[]{this.getX() + this.getW() / 2 - 10, this.getY() - 10,
                                            this.getX() + this.getW() / 2 + 10, this.getY() - 10},
                                    new double[]{0, -7, 0, -7});
                            break;
                        case 4://发射激光
                            shoot(4, new double[]{
                                            this.getX() + this.getW() / 2 - 10, this.getY(),
                                            this.getX() + this.getW() / 2 + 10, this.getY()},
                                    new double[]{0, 0, 0, 0});
                            break;
                        case 5://发射等离子球
                            shoot(6, new double[]{
                                            this.getX() + this.getW() / 2 - 10, this.getY(),
                                            this.getX() + this.getW() / 2 + 10, this.getY()},
                                    new double[]{0, -2, 0, -2});
                            break;
                    }
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

    @Override//护盾保护
    public void deductLive(int attack) {
        if (this.getShieldCurrentTime() <= 0)
            super.deductLive(attack);
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







