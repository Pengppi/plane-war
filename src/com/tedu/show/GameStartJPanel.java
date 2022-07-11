/**
 * @Author: Neo
 * @Date: 2022/07/09 星期六 11:59:53
 * @Project: plane_war
 * @IDE: IntelliJ IDEA
 **/
package com.tedu.show;


import com.tedu.manager.GameLoad;

import javax.swing.*;
import java.awt.*;

public class GameStartJPanel extends JPanel {

    private GameJFrame gj = null;

    public GameStartJPanel(GameJFrame gj) {
        this.gj = gj;
        gj.isReady = false;
        init();
    }

    private void init() {
        this.setLayout(null);
        String text[] = {"开始游戏", "游戏记录", "退出游戏"};
        gj.loadButton(text, this, 650, 210, 140,110,1);
        gj.setjPanel(this);
        gj.start();
    }


    @Override
    public void paintComponent(Graphics g) {
        ImageIcon bg = new ImageIcon("image/bg/bg6.png");
        g.drawImage(bg.getImage(), 0, 0, bg.getIconWidth(), bg.getIconHeight(), this);
        showInfo(g, GameJFrame.GAMETITLE, 1325, 560, 90);
        String txt = "至臻豪华尊享版";
        showInfo(g, txt, 1440, 625, 45);

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
