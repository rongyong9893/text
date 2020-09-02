package com.rong.demo.utils;

import com.google.common.util.concurrent.RateLimiter;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author rongyong
 * @version 1.0
 * @date 2020/9/2 23:47
 */
public class GuavaRateLimiter {
    public static ConcurrentHashMap<String, RateLimiter> resourceRateLimiter = new ConcurrentHashMap<String, RateLimiter>();

    //初始化限流工具RateLimiter
    static {
        createResourceRateLimiter("order", 0.5);
    }

    public static void createResourceRateLimiter(String resource, double qps) {
        if (resourceRateLimiter.contains(resource)) {
            resourceRateLimiter.get(resource).setRate(qps);
        } else {
            //创建限流工具，每秒发出50个令牌指令
            RateLimiter rateLimiter = RateLimiter.create(qps);
            resourceRateLimiter.putIfAbsent(resource, rateLimiter);

        }

    }

    public static void main(String[] args) throws InterruptedException {
//        for (int i = 0; i < 5; i++) {
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
                    //如果获得令牌指令，则执行业务逻辑
//                    if (resourceRateLimiter.get("order").tryAcquire()) {
//                        System.out.println("执行业务逻辑");
//                    } else {
//                        System.out.println("限流");
//                    }
//            Thread.sleep(100);
//        }

        while (true){
                        if (resourceRateLimiter.get("order").tryAcquire()) {
                            System.out.println("执行业务逻辑");
                            System.out.println("时间---: "+resourceRateLimiter.get("order").acquire()+"s");;

                        }
                           // Thread.sleep(100);
                    }
//                }
//            }).start();

    }
}
