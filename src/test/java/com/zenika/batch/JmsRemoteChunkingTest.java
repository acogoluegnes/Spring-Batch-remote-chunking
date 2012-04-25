/**
 * 
 */
package com.zenika.batch;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.util.Random;

import javax.jms.ConnectionFactory;
import javax.jms.Message;
import javax.jms.Session;
import javax.sql.DataSource;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.integration.chunk.ChunkHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author acogoluegnes
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
	"classpath:/jms-remote-chunking-master-context.xml",
	"classpath:/init-db-ctx.xml"
})
@ActiveProfiles("env")
public class JmsRemoteChunkingTest {
	
	private static final int NB_ROWS = 50;
	
	private static final String OUTPUT_DIR = "./jms-remote-chunking-output";
	
	@Autowired ApplicationContext masterCtx;

	@Autowired JobLauncher jobLauncher;
	
	@Autowired Job job;
	
	@Autowired DataSource ds;
	
	@Autowired ConnectionFactory connectionFactory;
	
	JdbcOperations tpl;
	
	@Before public void setUp() throws Exception {
//		drainQueues();
		tpl = new JdbcTemplate(ds);
		tpl.update("delete from transfer_request");
		Random r = new Random(System.currentTimeMillis());
		for(int i=0;i<NB_ROWS;i++) {
			tpl.update(
				"insert into transfer_request (id,account_id,amount,processed) values (?,?,?,?)",
				i,r.nextInt(1000),r.nextInt(500)+1,false
			);
		}
		File outputDir = new File(OUTPUT_DIR);
		if(outputDir.exists()) {
			FileUtils.forceDelete(new File(OUTPUT_DIR));
		}
		FileUtils.forceMkdir(outputDir);
		
	}

	@Autowired ChunkHandler chunkHandler;
	
	@Test public void sunnyDay() throws Exception {
		Thread.sleep(1000000000000L);
		
		
		ClassPathXmlApplicationContext slaveCtx = new ClassPathXmlApplicationContext();
		slaveCtx.getEnvironment().setActiveProfiles(masterCtx.getEnvironment().getActiveProfiles());
		slaveCtx.setConfigLocation("classpath:/jms-remote-chunking-slave-context.xml");
		slaveCtx.refresh();
		
		try {
			JobExecution exec = jobLauncher.run(
				job,
				new JobParametersBuilder().addLong("time",System.currentTimeMillis()).toJobParameters()
			);
			
			assertThat(exec.getStatus(),is(BatchStatus.COMPLETED));
			assertThat(new File(OUTPUT_DIR).list().length,is(NB_ROWS));
		} finally {
			slaveCtx.close();
		}
	}
	
	private void drainQueues() throws Exception {
		JmsTemplate jmsTpl = new JmsTemplate(connectionFactory);
		jmsTpl.setReceiveTimeout(100L);
		jmsTpl.setSessionAcknowledgeMode(Session.AUTO_ACKNOWLEDGE);
		jmsTpl.setSessionTransacted(false);
		jmsTpl.afterPropertiesSet();
		String [] queuesToDrain = new String[]{"replies","requests"};
		for(String queueToDrain : queuesToDrain) {
			Message msg = jmsTpl.receive(queueToDrain);
			while(msg != null) { 
				msg = jmsTpl.receive(queueToDrain);
			}
		}
	}
}
