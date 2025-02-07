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
java -Dspring.profiles.active=dev -Duser.timezone=UTC -Dfile.encoding=UTF-8 -jar base-1.0.0.jar D:\GithubRepos\spring-templates\base\env
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
# 日志文件中的日期时区
LOG_TIMEZONE=Asia/Tokyo
```
