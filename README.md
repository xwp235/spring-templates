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
