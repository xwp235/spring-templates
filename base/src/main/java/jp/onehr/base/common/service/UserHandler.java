package jp.onehr.base.common.service;

import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;

public class UserHandler {

    @ResponseBody
    public List<User> list() {
        return List.of(
                new User(1,"张三",23,"男",new Date()),
                new User(2,"李四",30,"男",new Date())
        );
    }

    public static class User {
        private int id;
        private String name;
        private int age;
        private String sex;
        private Date birthday;

        public User(int id, String name, int age, String sex, Date birthday) {
            this.id = id;
            this.name = name;
            this.age = age;
            this.sex = sex;
            this.birthday = birthday;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public Date getBirthday() {
            return birthday;
        }

        public void setBirthday(Date birthday) {
            this.birthday = birthday;
        }
    }

}
