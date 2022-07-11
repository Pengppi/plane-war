/**
 * @Author: Neo
 * @Date: 2022/07/10 星期日 17:43:29
 * @Project: plane_war
 * @IDE: IntelliJ IDEA
 **/
package com.tedu.element;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class GameRecord implements Serializable {
    public String user;
    String level;
    public int score;
    String date;

    public GameRecord(String level, int score) {
        super();
        this.user = System.getProperty("user.name");
        this.level = level;
        this.score = score;
        this.date = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(System.currentTimeMillis());
    }

    @Override
    public String toString() {
        StringJoiner sj = new StringJoiner("\t");
        sj.add("玩家：" + user);
        sj.add("分数：" + score);
        sj.add("关卡：" + level);
        sj.add("时间：" + date);
        return sj.toString();
    }

    public String[] toStringArray() {
        return new String[]{user, level, date};
    }

    public static List<GameRecord> getRecords() {
        List<GameRecord> list = null;
        try {

            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("src/com/tedu/text/record.dat"));
            Object obj = ois.readObject();
            list = (List<GameRecord>) obj;
            ois.close();
        } catch (IOException | ClassNotFoundException e) {  //内容为空
            return new ArrayList<>();
        }
        return list;
    }


    public static void setRecords(List<GameRecord> list) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("src/com/tedu/text/record.dat"));
            oos.writeObject(list);
            oos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void updateRecord(GameRecord c) {
        List<GameRecord> recs = GameRecord.getRecords();
        recs.add(c);
        setRecords(recs);
    }

    public static void showRecord() {
        List<GameRecord> recs = GameRecord.getRecords();
        System.out.println(recs.size());
        for (GameRecord r : recs) {
            System.out.println(r);
        }
    }

    public static void main(String[] args) {
        GameRecord.showRecord();
    }
}
