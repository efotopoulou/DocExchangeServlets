package org.ubi.maincontrol;

import net.sf.json.JSONObject;
import java.lang.System;

import java.security.PublicKey;
import java.util.Date;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SimpleTrigger;
import org.quartz.impl.StdSchedulerFactory;



public class EndUsersSingleton {

    private static EndUsersSingleton instance = null;
    JSONObject EndUsers;
    private static Scheduler scheduler =null;


    protected EndUsersSingleton() {
        EndUsers = new  JSONObject();
    }


    public static EndUsersSingleton getInstance() {
        if(instance == null) {
            instance = new EndUsersSingleton();

            try {
            JobDetail job = new JobDetail();
            job.setName("CleanUsersInfo");
            job.setJobClass(CleanUsersInfoJob.class);

            //configure the scheduler time
            SimpleTrigger trigger = new SimpleTrigger();
            trigger.setName("CleanUsersInfoJobTrigger");
            trigger.setStartTime(new Date(System.currentTimeMillis() + 300000));
            trigger.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY);
            trigger.setRepeatInterval(600000);

            //schedule it
            scheduler = new StdSchedulerFactory().getScheduler();
            scheduler.start();
            scheduler.scheduleJob(job, trigger);

            } catch (Throwable t) {
                t.printStackTrace();
            }
        }

        return instance;
    }

    public JSONObject getEndUsers() {
        return EndUsers;
    }

    public void clearEndUsers() {
        EndUsers.clear();
    }

    public void shutdownScheduler() {
        try{
        scheduler.shutdown(false);
        } catch (Throwable t) {
            t.printStackTrace();
        }

    }




}