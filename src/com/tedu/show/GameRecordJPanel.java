/**
 * @Author: Neo
 * @Date: 2022/07/10 星期日 18:41:54
 * @Project: plane_war
 * @IDE: IntelliJ IDEA
 **/
package com.tedu.show;

import com.tedu.element.GameRecord;

import javax.swing.*;
import java.awt.*;
import java.util.Collections;
import java.util.List;

public class GameRecordJPanel extends JPanel {

    private GameJFrame gj = null;

    public GameRecordJPanel(GameJFrame gj) {
        this.gj = gj;
        init();
    }

    private void init() {
        this.setLayout(null);
        String text[] = {"返回主界面"};
        gj.loadButton(text, this, 0, 510, 140,120,1);
        gj.setjPanel(this);
        gj.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        ImageIcon bg = new ImageIcon("image/bg/bg5.png");
        g.drawImage(bg.getImage(), 0, 0, bg.getIconWidth(), bg.getIconHeight(), this);
        char title[] = GameJFrame.GAMETITLE.toCharArray();
        for (int i = 0; i < title.length; i++) {
            showString(g, title[i] + "", 0, 170 + i * 185, 240);
        }
        title = "游戏记录".toCharArray();
        for (int i = 0; i < title.length; i++) {
            showString(g, title[i] + "", 1600, 300 + i * 240, 320);
        }
//        showRecord(g, 5, 350, 230, 280);
        showRecord(g, 4, 800, 220, 280);
    }

    private void showRecord(Graphics g, int num, int x, int y, int size) {
        List<GameRecord> list = GameRecord.getRecords();
        num = Math.min(num, list.size());
        int gapy = size - size / 6;
        Collections.sort(list, (a, b) -> (b.score - a.score));
        for (int i = 0; i < num; i++) {
            GameRecord r = list.get(i);
            int len = getLen(r.score) - 1;
            int iy = y + i * gapy;
            int ix = x - len * 155;
            showString(g, r.score + "", ix, iy, size);
            String key[] = {"玩家：", "关卡：", "时间："};
            String value[] = r.toStringArray();
            for (int j = 0; j < key.length; j++) {
                int jy = j * size / 6 + iy - 130;
                showString(g, key[j], x + 200, jy, size / 6);
                showString(g, value[j], x + 320, jy, size / 6);
            }
        }

    }

    private int getLen(int score) {
        int cnt = 0;
        int t = score;
        while (t > 0) {
            cnt++;
            t /= 10;
        }
        return score == 0 ? 1 : cnt;
    }

    private void showString(Graphics g, String txt, int x, int y, int size) {
        g.setFont(new Font("", Font.BOLD, size));
        int gap = size / 20;
        if (size > 250) {
            gap = size / 25;
        }
        g.setColor(Color.pink);
        g.drawString(txt, x, y);
        g.setColor(Color.black);
        g.drawString(txt, x + gap, y);
        g.setColor(Color.white);
        g.drawString(txt, x + gap * 2, y);
    }


}
