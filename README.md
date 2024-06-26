**仓库的功能、意义、说明**

- 一个基于Maven的Spring项目**脚手架示例**，帮助快速搭建聚合项目、微服务项目
- 收录没有重度依赖的可重复使用的**代码片段**

**模块说明**

- template-common：可重复使用的代码、工具类代码模块
- template-tool：纯工具类（邮件、文件上传等）的代码模块
- template-logger：日志记录器模块
- template-third-platform：与第三方服务整合模块，起到防腐层的作用
- service-app-01：业务模块A，可单独打成Jar包运行
- service-app-02：业务模块B，可单独打成Jar包运行

# 项目启动

**使用Maven的打包工具，将service-app-01与service-app-02打成Jar运行**

> **注意：**不要对单个模块进行聚合打包，否则会出现错误。
>
> 例如，对”service-app-01“进行单独打包，就会出现依赖”com.hackyle:template-common:jar:1.0.0 is missing, no dependency infomation availble"
>
> **原因:** 执行打包时他会在本地仓库中寻找模块，然而Maven本地仓库中并没有template-common这个模块的jar包，因此就失败了
>
> 此时我们尝试往本地仓库中安装template-common这个模块的jar包, 点击install，但这次又构建失败了：提示没有找到他的父工程, 因此继续安装父工程以后, 最终才打包成功.
>
> **解决办法：**找到父工程，执行一键打包！不要对某个模块单独打包

**Windows平台执行Jar**

```bash
# 设置临时环境变量
set JAVA_HOME=D:\ProgramFilesKS\Java\JDK11
set path=%JAVA_HOME%\bin;%path%

# 进入Jar包所在目录
cd D:\\project-template

# 执行Jar
java -jar template-service-app01.jar.jar

pause
```

**Linux上后台执行打成Jar**

```shell
nohup java -jar Jar名字.jar --spring.profiles.active=prod >/dev/null 2>&1 &
```

# 项目部署-Nginx

**在Nginx配置文件的http块中添加如下配置**

```properties
server {
  listen 80;
  server_name template.hackyle.com;
  
  #地址重写为HTTPS
  rewrite ^(.*)$ https://$host$1;
}

server {
    listen 443 ssl http2;
    listen [::]:443 ssl http2;
    server_name template.hackyle.com;
    
    #SSL证书以及配置
    ssl_certificate "/etc/nginx/cert/template.hackyle.com.pem";
    ssl_certificate_key "/etc/nginx/cert/template.hackyle.com.key";
    ssl_session_cache shared:SSL:1m;
    ssl_session_timeout  10m;
    ssl_ciphers HIGH:!aNULL:!MD5;
    ssl_prefer_server_ciphers on;
    
    #解决nginx反向代理后获取不到客户端的真实ip地址问题
    proxy_set_header Host $host;
    proxy_set_header X-Real-IP $remote_addr;  #获取客户端真实IP
    proxy_set_header REMOTE-HOST $remote_addr;
    proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;

    #业务模块A
    location /service-app-01 {
        alias /; #去除调请求中的'/service-app-01'，然后转到proxy_pass的地址上
        proxy_pass http://localhost:7000/;
    }
    #业务模块B
    location /service-app-02 {
        alias /; #去除调请求中的'/service-app-02'，然后转到proxy_pass的地址上
        proxy_pass http://localhost:8000/;
    }
    #静态资源
    location /static {
      alias /data/hackyle.com/project-demo/static/;
      index index.html;
    }
    
    error_page 404 /404.html;
        location = /40xds.html {
    }
    error_page 500 502 503 504 /50x.html;
        location = /50x.html {
    }
}
```

# 数据库备份

**Linux平台-生产备份**

```shell
cd /opt/mysql8.0.27/bin && ./mysqldump -u数据库用户名 -p数据库连接密码 --databases 数据库名 --opt --no-create-info --no-create-db --skip-extended-insert --skip-quick > 备份位置的绝对路径+文件
```

- n --no-create-db：不添加Create Database语句
- n --no-create-info：不添加Create Table语句

 

**Windows平台**

pushd D:\ProgramFilesKS\JavaDeveloper\mysql-8.0.26-winx64\bin && mysqldump -u数据库用户名 -p数据库连接密码 --databases 数据库名 --opt --no-create-info --no-create-db --skip-extended-insert --skip-quick > 备份位置的绝对路径+文件
