package org.tizzer.smmgr.config;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.tizzer.smmgr.constant.SystemConstants;

@Component
public class ScheduleTask {

    @Scheduled(cron = "59 59 23 * * ?")
    public void resetMarkId() {
        SystemConstants.markId = 1;
        SystemConstants.refundId = 1;
        SystemConstants.lossId = 1;
    }

}
