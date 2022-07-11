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
    private GameJFrame gj = null;
    public static int GameGate = 1;//关卡数
    public static String[] GameLevel =new String[] {"第一关","第二关","第三关","第四关"};//关卡字符串
    private ImageIcon bg;
    private int score = 0;

    public GameOverJPanel(GameJFrame gj, boolean isWin, int score, ImageIcon icon) {
        this.gj = gj;
        this.bg = icon;
        this.score = score;
        this.GameGate = Integer.parseInt(ElementManager.getBossId());
        this.setLayout(null);
        GameRecord.updateRecord(new GameRecord(GameLevel[GameGate-1], score));
        gj.isReady = false;
        init(isWin);
    }


    private void init(boolean isWin) {
        String text[];
        if (isWin&&Integer.parseInt(ElementManager.getBossId())!=4) {
            text = new String[]{"下一关", "返回主界面"};
        } else {
            text = new String[]{"重玩关卡", "返回主界面"};
        }
        gj.loadButton(text, this, 0, 100, 200,90,1);
        gj.setjPanel(this);
        gj.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        g.drawImage(bg.getImage(), 0, 0, bg.getIconWidth(), bg.getIconHeight(), this);
        showInfo(g, "GameOver!", 460, 260, 200);
        String txt = "您最终的得分是:" + score + "分！";
        showInfo(g, txt, 620, 420, 90);

    }

    private void showInfo(Graphics g, String txt, int x, int y, int size) {
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
