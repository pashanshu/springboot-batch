package com.zc.service.impl;

import java.sql.Timestamp;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zc.dao.AccessMapper;
import com.zc.model.Access;
import com.zc.service.TestService;


@Service
@Transactional(transactionManager="jdbcTransactionManager")
public class TestServiceImpl implements TestService {

	@Resource
	public AccessMapper accessMapper;
	
	@Override
	public List<Access> queryAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Access queryByName(String name) {
		if("".equals(name)||name==null){
			return null;
		}
		
		Access access =new Access();
		access.setUsername("zhanchang");
		access.setCreateTime(new Timestamp(System.currentTimeMillis()).toString()); 		
		return access;
	}

	@Override
	public int insert(Access access) {
		// TODO Auto-generated method stub
		return accessMapper.insert(access);
	}

}
