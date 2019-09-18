package com.sgs.mylibrary.util;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TimerScheduler {


    // Create a scheduled thread pool with 5 core threads
    ScheduledThreadPoolExecutor sch = (ScheduledThreadPoolExecutor)
            Executors.newScheduledThreadPool(5);

    // Create a task for one-shot execution using schedule()
    Runnable oneShotTask = new Runnable() {
        @Override
        public void run() {

        }
    };

    // Create another task
    Runnable delayTask = new Runnable() {
        @Override
        public void run() {
            try {

                Thread.sleep(10 * 1000);

            } catch (Exception e) {

            }
        }
    };

    // And yet another
    Runnable periodicTask = new Runnable() {
        @Override
        public void run() {
            try {

                Thread.sleep(10 * 1000);

            } catch (Exception e) {

            }
        }
    };


    ScheduledFuture<?> periodicFuture = sch.scheduleAtFixedRate(periodicTask, 0, 5, TimeUnit.SECONDS);


}
