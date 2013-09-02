package org.ubi.maincontrol;


import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.lang.System;

public class CleanUsersInfoJob implements Job
{
    public void execute(JobExecutionContext context)
            throws JobExecutionException {

        EndUsersSingleton endUsers = EndUsersSingleton.getInstance();
        System.out.println("EndUsers size Before"+endUsers.getEndUsers().size() );
        endUsers.clearEndUsers();
        System.out.println("EndUsers size After"+endUsers.getEndUsers().size() );
    }

}
