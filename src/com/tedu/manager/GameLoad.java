package com.tedu.manager;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import javax.swing.ImageIcon;

import com.tedu.controller.GameThread;
import com.tedu.element.Boss;
import com.tedu.element.ElementObj;
import com.tedu.element.Enemy;
import com.tedu.element.Play;
import com.tedu.element.Tool;
import com.tedu.element.Trap;

/**
 * @author renjj
 * @说明 加载器(工具 ： 用户读取配置文件的工具)工具类, 大多提供的是 static方法
 */
public class GameLoad {
    //	得到资源管理器
    private static ElementManager em = ElementManager.getManager();
    //	图片集合  使用map来进行存储     枚举类型配合移动(扩展)
    public static Map<String, ImageIcon> imgMap = new HashMap<>();

    public static Map<String, List<ImageIcon>> imgMaps;

    //	用户读取文件的类
    private static Properties pro = new Properties();

    /**
     * @param mapId 文件编号 文件id
     * @说明 传入地图id有加载方法依据文件规则自动产生地图文件名称，加载文件
     */
    public static void MapLoad(int mapId) {
//		得到啦我们的文件路径
        String mapName = "com/tedu/text/" + mapId + ".map";
//		使用io流来获取文件对象   得到类加载器
        ClassLoader classLoader = GameLoad.class.getClassLoader();
        InputStream maps = classLoader.getResourceAsStream(mapName);
        if (maps == null) {
            System.out.println("配置文件读取异常,请重新安装");
            return;
        }
        try {
//			以后用的 都是 xml 和 json
            pro.clear();
            pro.load(maps);
//			可以直接动态的获取所有的key，有key就可以获取 value
//			java学习中最好的老师 是 java的API文档。
            Enumeration<?> names = pro.propertyNames();
            while (names.hasMoreElements()) {//获取是无序的
//				这样的迭代都有一个问题：一次迭代一个元素。
                String key = names.nextElement().toString();
                System.out.println(pro.getProperty(key));
//				就可以自动的创建和加载 我们的地图啦
                String[] arrs = pro.getProperty(key).split(";");
                for (int i = 0; i < arrs.length; i++) {
                    ElementObj obj = getObj("map");
                    ElementObj element = obj.createElement(key + "," + arrs[i]);
                    System.out.println(element);
                    em.addElement(element, GameElement.MAPS);
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    //加载背景
    public static void loadMap(int mapId) {
        ElementObj obj = new com.tedu.element.Map().createElement(String.valueOf(mapId));
        em.addElement(obj, GameElement.MAPS);
    }

    /**
     * @说明 加载图片代码
     * 加载图片 代码和图片之间差 一个 路径问题
     */
    public static void loadImg() {//可以带参数，因为不同的关也可能需要不一样的图片资源
        String texturl = "com/tedu/text/GameData.pro";//文件的命名可以更加有规律
        ClassLoader classLoader = GameLoad.class.getClassLoader();
        InputStream texts = classLoader.getResourceAsStream(texturl);
//		imgMap用于存放数据
        pro.clear();
        try {
//			System.out.println(texts);
            pro.load(texts);
            Set<Object> set = pro.keySet();//是一个set集合
            for (Object o : set) {
                String url = pro.getProperty(o.toString());
                //System.out.println(o.toString());
                imgMap.put(o.toString(), new ImageIcon(url));
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 加载玩家
     */
    /**
     * 测试用的加载方法
     * 加载玩家飞机
     */
    public static void wpploadPlay() {
        ElementObj obj = new Play().createElement("800,450,1");//实例化对象（x,y,玩家飞机种类）
//		讲对象放入到 元素管理器中
        em.addElement(obj, GameElement.PLAY);//直接添加
    }


  

    public static ElementObj getObj(String str) {
        try {
            Class<?> class1 = objMap.get(str);
            Object newInstance = class1.newInstance();
            if (newInstance instanceof ElementObj) {
                return (ElementObj) newInstance;   //这个对象就和 new Play()等价
//				新建立啦一个叫  GamePlay的类
            }
        } catch (InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    //子弹字符串函数(x:水平位置,y:垂直位置,hv:水平速度,vv:垂直速度,c:[1|2]:1 is play,2 is enemy)
    public static String getFileString(double x,double y,double hv,double vv,int camp,int bulletKind)
	{
	return "x:"+x+",y:"+y+",hv:"+hv+",vv:"+vv+",c:"+camp+",k:"+bulletKind;
	}

    /**
     * 扩展： 使用配置文件，来实例化对象 通过固定的key(字符串来实例化)
     *
     * @param args
     */
    private static Map<String, Class<?>> objMap = new HashMap<>();

    public static void loadObj() {
        String texturl = "com/tedu/text/obj.pro";//文件的命名可以更加有规律
        ClassLoader classLoader = GameLoad.class.getClassLoader();
        InputStream texts = classLoader.getResourceAsStream(texturl);
        pro.clear();
        try {
            pro.load(texts);
            Set<Object> set = pro.keySet();//是一个set集合
            for (Object o : set) {
                String classUrl = pro.getProperty(o.toString());
                //System.out.println(o.toString());
//				使用反射的方式直接将 类进行获取
                Class<?> forName = Class.forName(classUrl);
                objMap.put(o.toString(), forName);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } 
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


//    //	用于测试
//    public static void main(String[] args) {
//        MapLoad(5);
//    	  loadImg();
//
//        try {
////			通过类路径名称， com.tedu.Play
//            Class<?> forName = Class.forName("");
////			通过类名  可以直接访问到这个类
//            Class<?> forName1 = GameLoad.class;
////			通过实体对象 获取 反射对象
//            GameLoad gameLoad = new GameLoad();
//            Class<? extends GameLoad> class1 = gameLoad.getClass();
//
//
//        } catch (ClassNotFoundException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//
//    }


}
