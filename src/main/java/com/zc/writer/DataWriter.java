package com.zc.writer;

import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import com.zc.batch.DataBatchConfiguration;
import com.zc.model.Access;
import com.zc.service.TestService;

@Resource
public class DataWriter implements ItemWriter<Access> {
    private static final Logger log = LoggerFactory.getLogger(DataBatchConfiguration.class);
    
    @Autowired
    private TestService testServiceImpl;

	@Override
	public void write(List<? extends Access> items) throws Exception {
        for (Access access : items) {
        	access.setId(null);
        	access.setUsername("zhanchang");
        	access.setUpdateTime(new Date().toString());
        	int resultNum = testServiceImpl.insert(access);
            log.info("write data : " + access+"====resultNum===> "+resultNum); //模拟 假装写数据 ,这里写真正写入数据的逻辑
        }
	}


}
