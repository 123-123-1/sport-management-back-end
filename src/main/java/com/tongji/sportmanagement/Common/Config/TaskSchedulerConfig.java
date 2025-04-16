package com.tongji.sportmanagement.Common.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
public class TaskSchedulerConfig {

  final static int POOLSIZE = 5;

  @Bean
  public TaskScheduler taskScheduler()
  {
    ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
    scheduler.setPoolSize(POOLSIZE);
    scheduler.setThreadNamePrefix("scheduled-task-");
    return scheduler;
  }
}
