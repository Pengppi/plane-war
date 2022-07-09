/**
 * @Author: Neo
 * @Date: 2022/07/09 星期六 13:16:05
 * @Project: plane_war
 * @IDE: IntelliJ IDEA
 **/
package com.tedu.show;


import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;

/*GameButton类，继承JButton类重写用于绘制按钮形状的函数*/
public class GameButton extends JButton {
    private int x;
    private int y;
    private int width;
    private int height;

    public GameButton(String text, int x, int y, int width, int height) {
        super(text);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        init();
    }


    private void init() {
        setBorder(roverBorder);
        setForeground(Color.white);
        this.setContentAreaFilled(false);
        this.setFocusPainted(false);
        this.setRolloverEnabled(true);
        this.setHorizontalAlignment(SwingConstants.CENTER);
        this.setVerticalAlignment(SwingConstants.CENTER);
        this.setFont(new Font("仿宋", Font.BOLD + Font.PLAIN, this.height - 8));
        this.setBounds(x, y, width, height);
        this.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {
                if (isRolloverEnabled()) {
                    roverBorderColor = Color.pink;
                    setForeground(Color.PINK);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (isRolloverEnabled()) {
                    roverBorderColor = Color.white;
                    setForeground(Color.white);
                }
            }
        });
    }

    private Border emptyBorder = BorderFactory.createEmptyBorder(0, 0, 0, 0);
    private Color roverBorderColor = Color.white;
    private Border roverBorder = new Border() {

        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
//            g.drawRect(x, y, width - 1, height - 1);
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(roverBorderColor);
            g2.setStroke(new BasicStroke(3.0f));
            g2.drawRoundRect(x, y, width - 1, height - 10, 35, 35);
        }

        public Insets getBorderInsets(Component c) {
            return new Insets(1, 1, 1, 1);
        }

        public boolean isBorderOpaque() {
            return true;
        }
    };


}
