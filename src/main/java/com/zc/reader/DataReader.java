package com.zc.reader;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.pool.DruidDataSource;
import com.zc.dao.AccessRowMapper;
import com.zc.model.Access;

@Service
public class DataReader implements ItemReader<Access> {

	@Override
	public Access read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		
		return null;
	}


}
