package com.kxxfydj.crawlerConfig;

import com.kxxfydj.common.CommonTag;
import com.kxxfydj.utils.ApplicationContextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * create by kaiming_xu on 2017/9/2
 */
public class CommonPipeline implements Pipeline{

    private static Logger logger = LoggerFactory.getLogger(CommonPipeline.class);


    private Map<String,Map<String,Integer>> cityRegionMap = new HashMap<>();

    public CommonPipeline(){
//        houseService = ApplicationContextUtils.getBean(HouseService.class);
//        cityService = ApplicationContextUtils.getBean(CityService.class);
    }

    @Override
    public void process(ResultItems resultItems, Task task) {
//        boolean isFinal = false;
//        if(resultItems.get(CommonTag.FINISHED) != null){
//            isFinal = resultItems.get(CommonTag.FINISHED);
//        }
//
//        if(isFinal) {
//            Object object = resultItems.get(CommonTag.HOUSE_LIST);
//            if (object != null && object instanceof List) {
//                List<House> houseList = (List) object;
//                List<City> cityList = cityService.getCitys();
//                cityList.forEach(city -> {
//                    if(cityRegionMap.get(city.getCity()) == null){
//                        Map<String,Integer> map = new HashMap<>();
//                        map.put(city.getRegion(),city.getId());
//                        cityRegionMap.put(city.getCity(),map);
//                    }else {
//                        cityRegionMap.get(city.getCity()).put(city.getRegion(),city.getId());
//                    }
//                });
//                //初始化house的cityId
//                int rows;
//                long startTime = System.currentTimeMillis();
//                houseList.forEach(house ->
//                    house.setCityId(cityRegionMap.get("深圳").get(house.getRegion()))
//                );
//
//                rows = houseService.deleteAllRecord();
//                logger.info("删除house表原有数据{}条!",rows);
//
//                rows = houseService.insertHouseRecord(houseList);
//                long endTime = System.currentTimeMillis();
//                logger.info("数据插入完成! 共插入{}条数据!",rows);
//                logger.info("共耗时:{}毫秒!",endTime - startTime);
//            }
//        }
    }
}
