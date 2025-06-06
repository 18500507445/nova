## Linux

### 1.常用命令

#### 1.1 基础
（1）pwd：打印出当前目录路径  
（2）top -c：查看cpu信息，核数  
（3）ls：list的简写，显示目标列表  
（4）cd：进入  
（5）ps auxw|head -1;ps auxw|sort -rn -k3|head -10（cpu占用前十的进程）  
（6）java -XX:+PrintCommandLineFlags -version（查看jdk gc信息，默认的垃圾回收器）  
（7）java -XX:+PrintGCDetails -version（查看gc详细信息）

#### 1.2 文件
（1）open -e .zshrc：打开文件
（2）vim ~/.zshrc：窗口打开文件
（3）rs filename ：上传到服务器
（4）rz filename：下载到本地

#### 1.3 启、停服务
（1）ps -ef|grep java：查找java进程  
（2）kill -9 pid：根据id立刻停止进程  
（3）kill -15 pid：请求完毕后，停止进程（死锁的时候还是kill -9吧，哈哈）   
（4）nohup java -Xms1g -Xmx1g -Xmn256m -Xss512k -XX:PermSize=512m -XX:MaxPermSize=512m -jar /xxx/xxx.jar >/dev/null 2>&1 &

- 根据JVM参数启动服务

#### 1.4 日志
（1）grep 'xxx' xxx.log （查看包含xxx字符串的所有信息）     
（2）grep 'xxx' xxx.log | tail -n 5（查找包含a的最后五条内容）  
（3）grep -C 5 "xxx" xxx.log（查找包含xxx的前后5行内容，-A后5行、-B前五行）       
（4）grep -E 'a.*b|b.*a' xxx.log （查看包含a和b字符串的所有信息）     
（5）tail -fn 500 xxx.log （循环最新500行 ）         
（6）tail -f xxx.log |grep 'xxx' （动态刷新包含xxx字符串的信息 ）       
（7）tail -f xxx.log |grep 'a.*b\|b.*a'（查找包含a和b的内容）       
（8）cat xxx.log |grep -A 16 'xxx' （慎用，查找文件包含xxx16行以内的内容 ）       

#### 1.5安装环境需要用到的命令
（1）cat /etc/passwd |cut -f 1 -d :（查询所有用户列表） 
（2）useradd（添加用户）    
（3）passwd（设置密码） 
（4）chmod -R elastic /usr/local/* （给文件夹读写权限） 

#### 1.6 线上cpu过高，如何排查
（1）top -c，看一下哪一个pid高    
（2）ps H -eo pid,tid,%cpu | grep 'pid'，查看过高的tid  
（3）printf "%x\n" tid，转换成16进制的线程id   
（4）jstack 'pid' |grep '线程id'    