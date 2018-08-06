package hello;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 当只有一个@Scheduled,按说明的执行,但是多个@Scheduled同时存在(无论是否在同一个类里面),则共用一个任务队列,虽然按时间策略产生了任务,但是队列不一定轮到执行
 * 
 * @author carl
 *
 */
@Component
public class ScheduledTasks {

	private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

	//注入配置文件属性$
	@Value("${name1}")
	private String name;
	
	//注入系统属性#
	@Value("#{systemProperties['os.name'] }")
	private String name2;
	
	/**
	 * 每5s执行一次,从开始调用方法计时
	 */
	@Scheduled(fixedRate = 5000)
	public void reportCurrentTime() {
		log.info("The time1 is now {}", dateFormat.format(new Date()));
		try {
			// 本身任务时间超过了定时策略,下一个任务在前一个任务执行完后马上执行
			Thread.sleep(10000);
			log.info(name);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		log.info("The time2 is now {}", dateFormat.format(new Date()));
	}

	/**
	 * 每5s执行一次,从结束调用方法计时
	 */
	@Scheduled(fixedDelay = 5000)
	public void reportCurrentTime2() {
		log.info("The time3 is now {}", dateFormat.format(new Date()));
		try {
			// 无论本身任务时间多久结束,下一个任务总是在前一个任务结束之后等待5s再执行
			Thread.sleep(10000);
			log.info(name2);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		log.info("The time4 is now {}", dateFormat.format(new Date()));
	}

	// 空格分割的六部分,依次代表秒，分钟，小时，日，月，星期。
	// "0 0 * * * *" = 每天每小时的最高点.
	// "*/10 * * * * *" = 每10秒.
	// "0 0 8-10 * * *" = 每天8, 9 和 10整点.
	// "0 0 6,19 * * *" = 每天6:00 AM 和 7:00 PM.
	// "0 0/30 8-10 * * *" = 每天的8:00, 8:30, 9:00, 9:30, 10:00 and 10:30.
	// "0 0 9-17 * * MON-FRI" = 周一到周五 上午9点到下午5点
	// "0 0 0 25 12 ?" = 每个12月25号凌晨

	//@Scheduled(cron = "*/5 * * * * MON-FRI")
	//配置时间到文件
	@Scheduled(cron = "${cron}")
	public void doSomething() {
		log.info("The time5 is now {}", dateFormat.format(new Date()));
		try {
			// 本身任务时间超过了定时策略,下一个任务在前一个任务执行完后马上执行
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		log.info("The time6 is now {}", dateFormat.format(new Date()));
	}

}