package jp.onehr.base.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jp.onehr.base.common.enums.ExceptionLevel;
import jp.onehr.base.common.exceptions.AppException;
import jp.onehr.base.common.utils.SpringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Map;

@RestController
public class TestController {


    private static final Logger logger = LoggerFactory.getLogger(TestController.class);

    private final UserService userService;

    public TestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("1")
    public Map<String,Object> lang1() {
        return Map.of("msg1", SpringUtil.getMessage("objSerialize2JsonFailed"));
    }

    @GetMapping("2")
    public String restartApp() {
        SpringUtil.restartApp();
        return "Restart app";
    }

    @GetMapping("3")
    public Map<String,Object> test3() {
        logger.info("test3 executed! logId is printed");
        userService.getList();
        var ld1 = LocalDateTime.now();
        var d1 = new Date();
        var zd1 = ZonedDateTime.now();
        return Map.of(
                "d1",d1,
                "zd1",zd1,
                "localDateTime1",ld1,"double1", 1.24,
                "float1",1.1f,"long1",100L);
    }

    /**
     * 请求示例:
     *    {
     *        "pid":1,
     *        "pname": "Person1",
     *        "uid":2,
     *        "uname":"User1"
     *    }
     */
    @PostMapping("4")
    public Map<String,Object> test4(@RequestBody User user,@RequestBody Person person) {
      return Map.of("user",user,"person",person);
    }

    /**
     * 请求示例

     * const form = new FormData()
     * form.append('file', file)
     * const user = {age: 5000,name: 'Jack'}
     * const userBlob = new Blob([JSON.stringify(user)], {type: 'application/json'})
     * form.append('user',userBlob)
     * const xmlBlob = new Blob(['<message></message>'], {type: 'application/xml'})
     * form.append('xml',xmlBlob)

     * axios({
     *     method: 'post',
     *     url: '',
     *     headers: {
     *         'Content-Type': 'multipart/form-data'
     *     },
     *     data: form
     * })
     */
    @PostMapping("5")
    public void test5(@RequestPart User user,@RequestPart MultipartFile file,@RequestPart String xml) {

    }

    @PostMapping("6")
    public void test6(HttpServletRequest request,@RequestBody User user) {
//        System.out.println(ServletUtil.getBodyBytes(request).length);
//        System.out.println(ServletUtil.getBody(request));
//        System.out.println(ServletUtil.getClientIP(request));
        int a = 1/0;
//        userService.getList();
    }

    @GetMapping("7")
    public void test7() {
       try {
           int a = 1/0;
       } catch (Exception e) {
//           throw new AppException(e);
       }
    }

    @GetMapping("8")
    public void test8() {
//      throw new AppException();
        try{
            int a = 1/0;
        }  catch (Exception e){
            throw new AppException(50001,"App error occurred:",true, ExceptionLevel.WARN,e);
        }
    }

    @PostMapping("9")
    public void test9(@Valid @RequestBody ValidDto dto) {

    }

    // http://localhost:8080/10/100?p1=9
    // 触发HandlerMethodValidationException
    @GetMapping("10/{a:\\d+}")
    public void test10(@RequestParam @Min(value = 10, message = "数字必须大于或等于10") int p1,
                       @PathVariable @Max(value=99, message = "数字必须小于或等于99") int a) {

    }

    public static class ValidDto {
        @NotBlank(message = "name is blank")
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class Person {
        private Integer pid;
        private String pname;

        public void setPid(Integer pid) {
            this.pid = pid;
        }

        public Integer getPid() {
            return this.pid;
        }

        public void setPname(String pname) {
            this.pname = pname;
        }

        public String getPname() {
            return this.pname;
        }
    }

    public static class User {
        private Integer uid;
        private String uname;

        public void setUid(Integer uid) {
            this.uid = uid;
        }

        public Integer getUid() {
            return this.uid;
        }

        public void setUname(String uname) {
            this.uname = uname;
        }

        public String getUname() {
            return this.uname;
        }
    }

}
