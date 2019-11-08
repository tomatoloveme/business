package com.bh.schedule;

import com.bh.service.IOrderService;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Date;

@Component
public class CloseOrderSchedule {
    @Autowired
    IOrderService orderService;
    @Value("${order.close.timeout}")
    private int orderTimeOut;
    @Scheduled(cron = "0 * */5 * * * ")
    public void closeOrder(){
        Date closeOrderTime = DateUtils.addHours(new Date(),-orderTimeOut);
        orderService.closeOrder(com.bh.utils.DateUtils.dateToStr(closeOrderTime));
    }
}
