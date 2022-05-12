package wiki.laona.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import wiki.laona.service.OrderService;

/**
 * @author laona
 * @description 订单定时任务
 * @since 2022-05-12 15:50
 **/
@Component
public class OrderJob {

    private static final Logger logger = LoggerFactory.getLogger(OrderJob.class);

    @Autowired
    private OrderService orderService;

    /**
     * 使用定时任务关闭超期未支付订单，会存在的弊端：
     * 1. 会有时间差，程序不严谨
     *      10:39下单，11:00检查不足1小时，12:00检查，超过1小时多余39分钟
     * 2. 不支持集群
     *      单机没毛病，使用集群后，就会有多个定时任务
     *      解决方案：只使用一台计算机节点，单独用来运行所有的定时任务
     * 3. 会对数据库全表搜索，及其影响数据库性能：select * from order where orderStatus = 10;
     * 定时任务，仅仅只适用于小型轻量级项目，传统项目
     *
     * 后续课程会涉及到消息队列：MQ-> RabbitMQ, RocketMQ, Kafka, ZeroMQ...
     *      延时任务（队列）
     *      10:12分下单的，未付款（10）状态，11:12分检查，如果当前状态还是10，则直接关闭订单即可
     */

    @Scheduled(cron = "0 0 0/1 * * ?")
    public void autoCloseOrder() {
        // logger.info("------------定时任务：自动关闭支付超时订单：{}",
        //         DateUtil.dateToString(new Date(), DateUtil.DATETIME_PATTERN));
        orderService.closeOrder();
    }
}
