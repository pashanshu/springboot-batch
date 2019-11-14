package com.zc.batch;

import com.alibaba.druid.pool.DruidDataSource;
import com.zc.dao.AccessRowMapper;
import com.zc.listener.JobListener;
import com.zc.model.Access;
import com.zc.service.TestService;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.batch.MyBatisPagingItemReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import org.springframework.batch.item.database.Order;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

/**
 * Created by EalenXie on 2018/9/10 14:50.
 * :@EnableBatchProcessing提供用于构建批处理作业的基本配置
 */
@Configuration
@EnableBatchProcessing
@ComponentScan(basePackageClasses = MyBatchConfigurer.class)
public class DataBatchConfiguration {
	private static final Logger log = LoggerFactory.getLogger(DataBatchConfiguration.class);

	@Resource
	private JobBuilderFactory jobBuilderFactory; // 用于构建JOB

	@Resource
	private StepBuilderFactory stepBuilderFactory; // 用于构建Step

	@Resource
	private JobListener jobListener; // 简单的JOB listener

	// @Autowired
	// private SqlSessionFactory sqlSessionFactory;
	@Resource
	private DruidDataSource bizDataSource;
	@Autowired
	private TestService testServiceImpl;
	@Autowired
	private PlatformTransactionManager transactionManager;
	@Autowired
	private ItemProcessor<Access, Access> dataProcessor;
	@Autowired
	private ItemWriter<Access> dataWriter;

	/**
	 * 一个简单基础的Job通常由一个或者多个Step组成
	 */
	@Bean
	public Job dataHandleJob() {
		return jobBuilderFactory.get("dataHandleJob").incrementer(new RunIdIncrementer()).start(handleDataStep()). // start是JOB执行的第一个step
		// next(xxxStep()).
		// next(xxxStep()).
		// ...
				listener(jobListener). // 设置了一个简单JobListener
				build();
	}

	/**
	 * 一个简单基础的Step主要分为三个部分 ItemReader : 用于读取数据 ItemProcessor : 用于处理数据 ItemWriter :
	 * 用于写数据
	 */
	@Bean
	public Step handleDataStep() {
		return stepBuilderFactory.get("getData").
				transactionManager(transactionManager).
				<Access, Access>chunk(100). 
	    // <输入,输出>。chunk通俗的讲类似于SQL的commit;这里表示处理(processor)100条后写入(writer)一次。
		// faultTolerant().retryLimit(0).retry(Exception.class).skipLimit(100).skip(Exception.class).
		// //捕捉到异常就重试,重试100次还是异常,JOB就停止并标志失败
				reader(getDataReader()). // 指定ItemReader
				processor(dataProcessor). // 指定ItemProcessor
				writer(dataWriter). // 指定ItemWriter
				build();
	}

	@Bean
	public ItemReader<Access> getDataReader() {
		JdbcPagingItemReader<Access> reader = new JdbcPagingItemReader<Access>();
		reader.setDataSource(bizDataSource);
		reader.setFetchSize(100); // 设置一次最大读取条数
		reader.setRowMapper(new AccessRowMapper());

		MySqlPagingQueryProvider queryProvider = new MySqlPagingQueryProvider();
		queryProvider.setSelectClause(
				"id,username,shop_name,category_name,description,delete_status,brand_name,shop_id,omit,update_time,create_time"); // 设置查询的列
		queryProvider.setFromClause("from access"); // 设置要查询的表
		Map<String, Order> sortKeys = new HashMap<String, Order>();// 定义一个集合用于存放排序列
		sortKeys.put("id", Order.ASCENDING);// 按照升序排序
		queryProvider.setSortKeys(sortKeys);
		reader.setQueryProvider(queryProvider);// 设置排序列
		return reader;
	}

	@Bean
	public ItemProcessor<Access, Access> getDataProcessor() {
		return new ItemProcessor<Access, Access>() {
			@Override
			public Access process(Access access) throws Exception {
				log.info("processor data : " + access.toString()); // 模拟 假装处理数据,这里处理就是打印一下
				return access;
			}
		};
	}

	@Bean
	public ItemWriter<Access> getDataWriter() {
		return list -> {
			for (Access access : list) {
				access.setId(null);
				access.setUsername("zhanchang");
				access.setUpdateTime(new Date().toString());
				int resultNum = testServiceImpl.insert(access);
				log.info("write data : " + access + "====resultNum===> " + resultNum); // 模拟 假装写数据 ,这里写真正写入数据的逻辑
			}
		};
	}

}
