/**
 * @Author: Neo
 * @Date: 2022/07/09 星期六 23:24:30
 * @Project: plane_war
 * @IDE: IntelliJ IDEA
 **/
package com.tedu.show;

import com.tedu.element.GameRecord;
import com.tedu.manager.ElementManager;

import javax.swing.*;
import java.awt.*;


public class GameOverJPanel extends JPanel {
    private final double time;
    private GameJFrame gj = null;
    public static int GameGate = 1;//关卡数
    public static String[] GameLevel = new String[]{"第一关", "第二关", "第三关", "第四关"};//关卡字符串
    private ImageIcon bg;
    private int score = 0;

    public GameOverJPanel(GameJFrame gj, boolean isWin, int score, ImageIcon icon, double time) {
        this.gj = gj;
        this.bg = icon;
        this.score = score;
        this.time = time;
        this.GameGate = Integer.parseInt(ElementManager.getBossId());
        this.setLayout(null);
        GameRecord.updateRecord(new GameRecord(GameLevel[GameGate - 1], score,String.format("%.0f", time) + "秒"));
        gj.isReady = false;
        init(isWin);
    }


    private void init(boolean isWin) {
        String text[];
        if (isWin && Integer.parseInt(ElementManager.getBossId()) != 4) {
            text = new String[]{"下一关", "返回主界面"};
        } else {
            text = new String[]{"重玩关卡", "返回主界面"};
        }
        gj.loadButton(text, this, 0, 140, 200, 125, 1);
        gj.setjPanel(this);
        gj.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        g.drawImage(bg.getImage(), 0, 0, bg.getIconWidth(), bg.getIconHeight(), this);
        showString(g, "GameOver!", 460, 240, 200);
        String txt = "您最终的得分是:" + score + "分！";
        showString(g, txt, 580, 370, 90);
        if (time > 0) {
            showString(g, "您所用的时间是:" + String.format("%.0f", time) + "秒！", 610, 490, 80);

        }
    }

    private void showString(Graphics g, String txt, int x, int y, int size) {
        g.setFont(new Font("", Font.BOLD, size));
        int gap = size / 20;
        g.setColor(Color.pink);
        g.drawString(txt, x, y);
        g.setColor(Color.black);
        g.drawString(txt, x + gap, y);
        g.setColor(Color.white);
        g.drawString(txt, x + gap * 2, y);
    }

    public GameJFrame getGj() {
        return gj;
    }

    public void setGj(GameJFrame gj) {
        this.gj = gj;
    }

}
