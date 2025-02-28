### Gradle构建常用命令

#### 重新生成 gradle-wrapper.jar

```bash
./gradlew.bat wrapper --gradle-version 8.12.1
# 或
gradle wrapper --gradle-version 8.12.1
```

#### 删除构建好的build目录

```bash
./gradlew.bat clean --warning-mode all
# 或
gradle clean --warning-mode all
```

#### 开始构建

```bash
./gradlew.bat build --warning-mode all
# 或
gradle build --warning-mode all
```

#### 打包后的jar启动命令

```bash
java -Dspring.profiles.active=prod -Duser.timezone=UTC -Dfile.encoding=UTF-8 -jar base-1.0.0.jar D:\GithubRepos\spring-templates\base\env
```

#### 当前项目支持配置

```html
# 是否开启结构化日志
LOGGING_STRUCTURED=off
# 生产环境下的日志路径
LOG_PATH=D:\logs
# 项目使用的默认时区
APP_TIMEZONE=UTC
# 项目使用的默认日期格式
APP_DATETIME_PATTERN=yyyy-MM-dd'T'HH:mm:ss.SSSZ
# 日志显示时间所用时区
LOG_TIMEZONE=Asia/Tokyo
# 异步任务执行超时时间(使用DeferredResult作为返回值使用时有效)
ASYNC_REQUEST_TIMEOUT=PT5S
# 数据库连接地址
DB_URL=jdbc:postgresql://127.0.0.1:5432/elegant_api?stringtype=unspecified&timezone=UTC&jvmZone=UTC
# 数据库连接用户名
DB_USERNAME=postgres
# 数据库连接密码
DB_PASSWORD=123456
# 数据源最小空闲连接数
DATASOURCE_MINIMUM_IDLE=2
# 数据源最大空闲连接数
DATASOURCE_MAXIMUM_POOL_SIZE=10
```

#### 开启editorconfig支持步骤

![第一步](.\shortcut\editorconfig-01.png)

![第二步](.\shortcut\editorconfig-02.png)
