package com.tedu.element;

import com.tedu.controller.Stopwatch;

import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.*;

/**
 * @author renjj
 * @说明 所有元素的基类。
 */
public abstract class ElementObj {

    public int deleteTime;//子弹消失的时间(只有deleteTime==0与isLive==false时才会消失)
    private double x;
    private double y;
    private int w;
    private int h;
    private ImageIcon icon;
    //	还有。。。。 各种必要的状态值，例如：是否生存.
    private boolean live = true; //生存状态 true 代表存在，false代表死亡
    private String kind = "";//种类
    private int camp = 0;//阵营(1 is play,2 is enemy)

    //水平速度与垂直速度
    private double moveXNum = 0;//水平移动距离
    private double moveYNum = 0;//垂直移动距离
    /*
  /**
   * 有防御力的对象
   */
    private int density = 0;//对象的强度
    private int blood = 0;//对象的血量
    private int shield_liveTime = 10;//护盾的持续时间为10s

    public Stopwatch god_timer = null; //无敌时间控制器

    public int god_time = 2;
    public boolean isShow = true;//是否显示

    public double getCurrentGodTime() {
        if (god_timer == null) {
            return -1;
        }
        return god_time - god_timer.currentTime();
    }

    private Stopwatch shield_time = null;

    public void setShield_time(Stopwatch sw, int time) {
        this.shield_time = sw;
        if (time > 0) {
            this.shield_liveTime = time;
        }
    }


    /**
     * 有攻击力的对象
     */
    private int attack = 0;//对象的攻击力(一般用于子弹)
    private Stopwatch bullet_time = null;//特殊子弹的时间
    private int bullet_kind = 1;//玩家的子弹种类(1 is 普通子弹， 2 is 散弹， 3 is 导弹， 4 is 激光， 5 is 等离子球)
    private int attack_kind = 1;//玩家的攻击方式(1 is 单发，2 is 双发，3 is 机枪,4 is 双重机枪)
    public static int attack_count = 4;//玩家的攻击方式数目
    public static int bullet_count = 4;//玩家的攻击种类数目 
    public static int bullet_liveTime = 10;//玩家特殊子弹的总时间
    public static int emp_time = 2000;//飞机脉冲紊乱的时间
    public int emp_currentTime = 0;//飞机脉冲紊乱的剩余时间
    private int tower_time = 5000;//浮游炮的持续时间
    private int tower_currentTime = 0;//当前浮游炮的剩余时间
    /*
     * 陷阱警告时间
     * */
    private int restTime = 300;

    private int score = 0; //分值

    public int getRebornNum() {
        return rebornNum;
    }

    public void setRebornNum(int reborn) {
        rebornNum = reborn;
    }

    public int rebornNum = 0; //复活币数量

    // 可以采用枚举值来定义这个(生存，死亡，隐身，无敌)
//	注明：当重新定义一个用于判定状态的变量，需要思考：1.初始化 2.值的改变 3.值的判定
    public ElementObj() {    //这个构造其实没有作用，只是为继承的时候不报错写的
    }

    /**
     * @param x    左上角X坐标
     * @param y    左上角y坐标
     * @param w    w宽度
     * @param h    h高度
     * @param icon 图片
     * @说明 带参数的构造方法; 可以由子类传输数据到父类
     */
    public ElementObj(int x, int y, int w, int h, ImageIcon icon) {
        super();
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.icon = icon;
    }

    /**
     * @param g 画笔 用于进行绘画
     * @说明 抽象方法，显示元素
     */
    public void showElement(Graphics g) {
        if (this.getIcon() == null || this.getIcon().getImage() == null) {
            return;
        }
        g.drawImage(this.getIcon().getImage(),
                (int) this.getX(), (int) this.getY(),
                this.getW(), this.getH(), null);
    }

    /**
     * @param bl  点击的类型  true代表按下，false代表松开
     * @param key 代表触发的键盘的code值
     * @说明 使用父类定义接收键盘事件的方法
     * 只有需要实现键盘监听的子类，重写这个方法(约定)
     * @说明 方式2 使用接口的方式;使用接口方式需要在监听类进行类型转换
     * @题外话 约定  配置  现在大部分的java框架都是需要进行配置的.
     * 约定优于配置
     * @扩展 本方法是否可以分为2个方法？1个接收按下，1个接收松开(给同学扩展使用)
     */
    public void keyClick(boolean bl, int key) {  //这个方法不是强制必须重写的。

    }

    public void mouseMove(int tx, int ty) {
    }

    /**
     * @说明 移动方法; 需要移动的子类，请 重写这个方法
     */
    protected void move(long gameTime) {
    }

    /**
     * @设计模式 模板模式;在模板模式中定义 对象执行方法的先后顺序,由子类选择性重写方法
     * 1.移动  2.换装  3.子弹发射
     */
    public final void model(long gameTime) {
//		先换装
        updateImage(gameTime);
//		在移动
        move(gameTime);
//		在发射子弹
        add(gameTime);
    }

    //	 long ... aaa  不定长的 数组,可以向这个方法传输 N个 long类型的数据
    protected void updateImage(long time) {
    }

    protected void add(long gameTime) {
    }

    //	死亡方法  给子类继承的
    public void die() {  //死亡也是一个对象

    }

    public ElementObj createElement(String str) {
        return null;
    }

    /**
     * @return
     * @说明 本方法返回 元素的碰撞矩形对象(实时返回)
     */
    public Rectangle getRectangle() {
//		可以将这个数据进行处理
        return new Rectangle((int) x, (int) y, w, h);
    }

    /**
     * @param obj
     * @return boolean 返回true 说明有碰撞，返回false说明没有碰撞
     * @说明 碰撞方法
     * 一个是 this对象 一个是传入值 obj
     */
    public boolean pk(ElementObj obj) {
        return this.getRectangle().intersects(obj.getRectangle());
    }


    /**
     * 只要是 VO类 POJO 就要为属性生成 get和set方法
     */
    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public int getW() {
        return w;
    }

    public void setW(int w) {
        this.w = w;
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }

    public ImageIcon getIcon() {
        return icon;
    }

    public void setIcon(ImageIcon icon) {
        this.icon = icon;
    }

    //设置生死状态
    public boolean isLive() {
        return live;
    }

    public void setLive(boolean live) {
        this.live = live;
    }

    //设置种类
    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    //设置阵营
    public int getCamp() {
        return camp;
    }

    public void setCamp(int camp) {
        this.camp = camp;
    }

    //************************************设置攻击力******************************************
    //扣血函数
    public void deductLive(int attack) {
        //System.out.println("deduct:"+attack);
        this.blood = this.blood - attack <= 0 ? 0 : this.blood - attack;
        if (this.blood == 0) this.setLive(false);
    }

    //设置攻击力
    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getAttack() {
        return attack;
    }

    //设置玩家子弹类型
    public void setBulletKind(int bullet_kind) {
        this.bullet_kind = bullet_kind;
        this.setBulletTime(new Stopwatch());
    }

    //获得玩家子弹类型
    public int getBulletKind() {
        return bullet_kind;
    }

    //设置玩家特殊子弹时间
    public void setBulletTime(Stopwatch sw) {
        this.bullet_time = sw;
    }

    //获得玩家特殊子弹时间
    public double getBulletTime() {
        if (this.bullet_time == null) {
            return -1;
        }
        return bullet_liveTime - this.bullet_time.currentTime();
    }

    //设置玩家攻击方式
    public void setAttackKind(int attack_kind) {
        this.attack_kind = attack_kind;
    }

    //获得玩家攻击方式
    public int getAttackKind() {
        return attack_kind;
    }

    //受到脉冲攻击
    public void getEMP() {
        this.emp_currentTime = emp_time;
    }

    //获得脉冲攻击剩余时间
    public int getEMPTime() {
        return this.emp_currentTime;
    }

    //设置当前的脉冲剩余时间
    public void setEMPTime(int time) {
        this.emp_currentTime = time;
    }
    //***************************************************************************************


    //****************************************设置防御力*************************************
    //设置血量
    public void setBlood(int blood) {
        this.blood = blood;
    }

    public int getBlood() {
        return blood;
    }

    //设置强度
    public void setDensity(int density) {
        this.density = density;
        this.setBlood(this.density);//测试时默认强度为血量
    }

    public int getDensity() {
        return density;
    }


    public Stopwatch getShield_time() {
        return shield_time;
    }

    public Stopwatch getBullet_time() {
        return bullet_time;
    }


    //获得当前护盾的时间
    public double getShieldCurrentTime() {
        if (this.shield_time == null) {
            return -1;
        }
        return this.shield_liveTime - this.shield_time.currentTime();
    }
    //***************************************************************************************


    //获得当前浮游炮的时间
    public int getTowerCurrentTime() {
        return tower_currentTime;
    }

    //设置当前浮游炮的时间
    public void setTowerCurrentTime(int tower_currentTime) {
        this.tower_currentTime = tower_currentTime;
    }

    //获得浮游炮
    public void setTowerTime() {
        this.setTowerCurrentTime(this.tower_time);
    }

    //****************************************设置速度********************************************
    public void setSpeed(double moveXNum, double moveYNum) {
        this.moveXNum = moveXNum;
        this.moveYNum = moveYNum;
    }

    public double getXSpeed() {
        return this.moveXNum;
    }

    public double getYSpeed() {
        return this.moveYNum;
    }

    //********************************************************************************************
    //设置爆炸信息
    //爆炸参数设置增加范围(爆炸初始直径范围，爆炸扩散长度（直径差）)默认为(20,8)
    public void setExplodeMsg(int explodeOriginRange, int explodeExpandRange, int explodeRelayTime) {

    }

    //设置警告时间
    public void setRestTime(int restTime) {
        this.restTime = restTime;
    }

    //陷阱时间减少
    public void reduceTime() {
        restTime--;
        return;
    }

    //返回陷阱警告时间
    public int getRestTime() {
        return restTime;
    }

    //分数设置
    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void reborn() {
        if (this.getRebornNum() <= 0 || this.getCurrentGodTime() > 0) {
            return;
        }
        this.setBlood(this.getDensity());
        this.god_timer = new Stopwatch();//开启无敌时间计时器
        this.isShow = false;
        this.setRebornNum(this.getRebornNum() - 1);//消耗复活心
        this.setLive(true);

    }
}










