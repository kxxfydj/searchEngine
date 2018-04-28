import com.kxxfydj.common.RedisKeys;
import com.kxxfydj.entity.CodeInfo;
import com.kxxfydj.redis.RedisUtil;
import com.kxxfydj.service.CodeInfoService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kxxfydj on 2018/4/23.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@WebAppConfiguration("src/main/resources")
public class RedisTest {

    private static final Logger logger = LoggerFactory.getLogger(RedisTest.class);

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    CodeInfoService codeInfoService;

    @Test
    public void testRedis(){
        List<CodeInfo> codeInfoList = codeInfoService.getAllCodeInfo();
        List<String> codeInfoKeys = new ArrayList<>();
        for(CodeInfo codeInfo : codeInfoList){
            codeInfoKeys.add(RedisKeys.CODEINFOID.getKey() + ":" + codeInfo.getId());
        }

        redisUtil.setForBatch(codeInfoKeys,codeInfoList);
        logger.info("codeInfo对象缓存到redis中！共{}条", codeInfoKeys.size());
    }
}
