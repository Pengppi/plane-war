/**
 * @Author: Neo
 * @Date: 2022/07/09 星期六 19:52:54
 * @Project: plane_war
 * @IDE: IntelliJ IDEA
 **/
package com.tedu.controller;


public class Stopwatch {
    private final long start;
    private long last;

    public Stopwatch() {
        start = System.currentTimeMillis();
        last = start;
    }

    /**
     * @description: 距离上次调用函数是否已经过了 time 时间
     * @method: sinceLast
     * @params: [time]
     * @return: boolean
     * @author: Neo
     * @date: 2022/7/11/011 18:48:29 下午
     **/
    public boolean sinceLast(double time) {
        long now = System.currentTimeMillis();
        if ((now - last) / 1000.0 >= time) {
            last = now;
            return true;
        }
        return false;
    }

    /**
     * @description: 返回秒表计时时间
     * @method: elapsedTime
     * @params: []
     * @return: double
     * @author: Neo
     * @date: 2022/7/9/009 19:53:18 下午
     **/
    public double currentTime() {
        long now = System.currentTimeMillis();
        return (now - start) / 1000.0;
    }
}
