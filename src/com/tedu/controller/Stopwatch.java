/**
 * @Author: Neo
 * @Date: 2022/07/09 星期六 19:52:54
 * @Project: plane_war
 * @IDE: IntelliJ IDEA
 **/
package com.tedu.controller;


public class Stopwatch {
    private final long start;

    public Stopwatch() {
        start = System.currentTimeMillis();
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
