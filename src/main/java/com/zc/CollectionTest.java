package com.zc;

import com.zc.batch.DataBatchConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

public class CollectionTest {
    private static final Logger log = LoggerFactory.getLogger(CollectionTest.class);


    public static void main(String[] args) {
        CollectionTest ct = new CollectionTest();
        try {
            ct.multiThread();
        } catch (InterruptedException e) {
            log.error("exception happens",e);
        } catch (ExecutionException e) {
            log.error("exception happens",e);
        }
    }

    public void multiThread() throws InterruptedException, ExecutionException {
        log.info("begin to  create multi threads！");
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 5,
                0L, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(3), new ThreadPoolExecutor.DiscardOldestPolicy()
        );

        List<Callable<List<Long>>> tasks = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            Callable<List<Long>> task = new Callable<List<Long>>() {
                @Override
                public List<Long> call() throws Exception {
                    return Arrays.asList(1L, 2L, 3L, 4L);
                }
            };
            tasks.add(task);
        }

        List<List<Long>> finalResult = new ArrayList<>();
        List<Future<List<Long>>> results = threadPoolExecutor.invokeAll(tasks);

        for(Future<List<Long>> future:results){
            List<Long> list = future.get();
            finalResult.add(list);
        }
        log.info(finalResult.toString());
    }
}
