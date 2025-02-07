package jp.onehr.base.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Async
    public void getList() {
      logger.info("userService getList executed!");
      int i = 1/0;
    }

}
