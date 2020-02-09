package com.zc.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.alibaba.druid.pool.DruidDataSource;

@Configuration // 扫描 Mapper 接口并容器管理
//@MapperScan(basePackages = "com.zc.model", sqlSessionFactoryRef = "springSqlSessionFactory")
public class DataSourceSpringConfig {

	@Value("${spring.datasource.type}")
	private Class<? extends DataSource> dataSourceType;

	@Bean(name = "springDataSource", destroyMethod = "close", initMethod = "init")
	@Primary
	@ConfigurationProperties(prefix = "spring.datasource")
	public DataSource dataSource() {
		return DataSourceBuilder.create().type(dataSourceType).build();
	}
	
    @Bean(name = "transactionManager")
    public PlatformTransactionManager transactionManager(DataSource springDataSource) {
        return new DataSourceTransactionManager(springDataSource);
    }


	
	@Bean(name = "bizDataSource")
	@ConfigurationProperties(prefix = "biz.datasource")
	public DataSource bizDataSource() {
		return DataSourceBuilder.create().type(dataSourceType).build();
	}
	
    @Bean(name = "jdbcTransactionManager")
    public PlatformTransactionManager jdbcTransactionManager(DataSource bizDataSource) {
        return new DataSourceTransactionManager(bizDataSource);
    }

	@Bean(name = "springSqlSessionFactory")
	@ConditionalOnMissingBean
	public SqlSessionFactory sqlSessionFactory() throws Exception {
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(bizDataSource());
		sqlSessionFactoryBean.setTypeAliasesPackage("com.zc.dao");
		sqlSessionFactoryBean.setMapperLocations(
				new PathMatchingResourcePatternResolver().getResources("classpath:com/zc/dao*//*.xml"));
		sqlSessionFactoryBean.getObject().getConfiguration().setMapUnderscoreToCamelCase(true);
		return sqlSessionFactoryBean.getObject();
	}
}
