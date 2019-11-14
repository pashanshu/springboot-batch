package com.zc.processor;

import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import com.zc.batch.DataBatchConfiguration;
import com.zc.model.Access;

@Resource
public class DataProcessor implements ItemProcessor<Access, Access> {
    private static final Logger log = LoggerFactory.getLogger(DataProcessor.class);

	@Override
	public Access process(Access access) throws Exception {
        log.info("processor data : " + access.toString());  //模拟  假装处理数据,这里处理就是打印一下
        return access;
	}

}
