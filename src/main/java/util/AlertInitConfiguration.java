package util;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AlertInitConfiguration {

	
	@Bean
	public Scheduler startSchedulerDump() {
		Scheduler sched = null;
		try {
			SchedulerFactory sf = new StdSchedulerFactory();
			sched = sf.getScheduler(); 
			JobDetail job = JobBuilder.newJob(SendAlert.class).withIdentity("job1", "group1").build();
			CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger1", "group1")
					.withSchedule(CronScheduleBuilder.cronSchedule("0/5 * * * * ?")).build();
			sched.scheduleJob(job, trigger);
			
			sched.start();
		} catch (SchedulerException e) {
			LoggerFoxAlert.getLoggerInstance().logError(e.getMessage());
		}
		return sched;
	}
}
