package com.example.multidatasourcedemo.Component;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @ClassName: ScheduledTasks
 * @Auther: zhoucc
 * @Date: 2019/6/11 15:35
 * @Description: 定时任务
 */

@Component
public class ScheduledTasks {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Scheduled(fixedRate = 500000)
    // @Scheduled(cron="*/5 * * * * *")
    public void reportCurrentTime() {
        System.out.println("现在时间：" + dateFormat.format(new Date()));
    }
}
