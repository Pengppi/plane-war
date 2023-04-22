# plane-war

这是一个使用Java Swing技术制作的飞机大战小游戏。游戏中玩家将通过移动鼠标控制一个飞机，与敌机进行激烈的空中战斗，躲避敌机的攻击，同时尽量消灭更多的敌机，获得高分。

该游戏采用经典的2D飞机大战游戏玩法，包括了基本的游戏元素，如飞机角色、敌机、子弹、道具等，并通过Java Swing技术实现了游戏的图形界面和用户交互。

## 主要特点：

使用Java Swing技术，实现了游戏的图形界面和用户交互，简单易懂。
包含了经典的飞机大战游戏玩法，包括飞机角色的控制、敌机的生成与攻击、子弹的发射和碰撞检测等。
设计了多种敌机类型，具有不同的攻击方式和血量，增加了游戏的难度和挑战性。
实现了基本的游戏逻辑，包括得分计算、生命值管理、游戏关卡切换等功能。
代码结构清晰，采用面向对象的设计思想，易于理解和扩展。

## 系统架构：

![image-20230422110535752](https://article.biliimg.com/bfs/article/90a5314c44bb0193815d4df1dd638009a08e5eb9.png)

## 项目结构:
```text
plane-war
├─ image                      # 图片资源目录
└─ src                        # 项目源代码目录
    └─ com
        └─ tedu
            ├─ controller        # 控制层，游戏逻辑相关类，监听用户操作、游戏状态等
            ├─ element           # 游戏元素类，如飞机、子弹、敌机、陷阱等
            ├─ game              # 游戏启动入口
            ├─ manager           # 管理类，如资源管理、游戏状态管理等
            ├─ show              # 显示类，如绘制游戏画面的类
            └─ text              # 文本处理类，如读取游戏配置文件、保存用户数据等

```

## 游戏截图：

![image-20230422110205827](https://article.biliimg.com/bfs/article/8462b1069dfeda3f748500ca60b43bd09402ab92.png)

![image-20230422110316310](https://article.biliimg.com/bfs/article/6dcc6d7a7d04487eb810ef7c9da193799c65a672.png)

![image-20230422110343753](https://article.biliimg.com/bfs/article/f497b9a5b92b6d1223801685abd28a2f2f4999c9.png)

![image-20230422110405841](https://article.biliimg.com/bfs/article/bbe95016e570b57fb607c9d24f12cb0ffc34d830.png)

![image-20230422110427501](https://article.biliimg.com/bfs/article/4abcce8732bff2464e2e51c2edf028603b3400f0.png)

## 如何运行游戏：

确保已安装Java运行环境（JRE）和Java开发工具包（JDK）。
克隆或下载本仓库到本地。
使用Java编译器编译并运行主程序文件，或直接运行plane_war.jar文件
游戏将在Java Swing窗口中打开，玩家可以通过鼠标控制飞机进行游戏。

