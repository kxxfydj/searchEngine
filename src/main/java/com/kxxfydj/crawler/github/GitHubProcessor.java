package com.kxxfydj.crawler.ziruwang;

import com.kxxfydj.common.CommonTag;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * create by kaiming_xu on 2017/9/2
 */
public class GitHubProcessor implements PageProcessor{

    private static Logger logger = LoggerFactory.getLogger(GitHubProcessor.class);

    private Site site;
//
//    private List<House> houseList = Collections.synchronizedList(new ArrayList<>());
//
//    private AtomicInteger pageCount = new AtomicInteger(0);
//
    public GitHubProcessor(Site site) {
        this.site = site;
    }
//
    @Override
    public void process(Page page) {
        String type = (String) page.getRequest().getExtra(CommonTag.TYPE);

        if(CommonTag.HOME_PAGE.equals(type)){
            processHomePage(page);
        }else if(CommonTag.REGION_PAGE.equals(type)){
            processRegionPage(page);
        }
    }
//
//
    private void processHomePage(Page page){
        Document document = page.getHtml().getDocument();
//        Elements lis = document.select("#selection > div > div > dl.clearfix.zIndex6 > dd > ul > li");
//        lis.remove(0);
//
//        for(Element li : lis){
//            String targetUrl = "http:" + li.child(0).getElementsByTag("a").attr("href");
//            Request request = RequestUtil.createGetRequest(targetUrl,CommonTag.REGION_PAGE);
//            request.putExtra("isFirstPage",true);
//            pageCount.incrementAndGet();
//            page.addTargetRequest(request);
//        }
    }
//
//    /**
//     *
//     * @param page
//     */
    private void processRegionPage(Page page){
//        Document document = page.getHtml().getDocument();
//        Elements homeNodes = document.select("#houseList").get(0).children();
//
//        Boolean isFirstPage = (Boolean) page.getRequest().getExtra("isFirstPage");
//        if(isFirstPage != null){
//            String firstUrl = document.select("#page > .active").attr("href");
//            String preffixUrl = "http:" + firstUrl.substring(0,firstUrl.length() - 1);
//            int totalPage = Integer.parseInt(RegexUtils.singleExtract(document.select("#page > span").text(),"共(\\d+)页",1));
//            for(int i = 2 ;i < totalPage ;i++){
//                pageCount.incrementAndGet();
//                Request request = RequestUtil.createGetRequest(preffixUrl + i,CommonTag.REGION_PAGE);
//                page.addTargetRequest(request);
//            }
//        }
//
//        for(Element houseNode : homeNodes){
//            House house = processImageUrl(houseNode);
//            processHouseText(houseNode,house);
//            processPriceDetail(houseNode,house);
//            this.houseList.add(house);
//        }
//
//        if(pageCount.decrementAndGet() == 0){
//            page.putField(CommonTag.HOUSE_LIST,this.houseList);
//            page.putField(CommonTag.FINISHED,true);
//        }
//
    }
//
//    private House processImageUrl(Element element){
//        String imageUrl = "http:" + element.select(".img.pr").first().getElementsByTag("img").attr("src");
//        House house = new House();
//        house.setImageUrl(imageUrl);
//        return house;
//    }
//
//    /**
//     *
//     * @param element
//     * @return
//     */
//    private House processHouseText(Element element,House house){
//        String detailLocation = element.select(".txt > h3").text();
//        String locationInfo = element.select(".txt > h4").text();
//        String region = RegexUtils.singleExtract(locationInfo,"\\[(.*?区)",1);
//        String location = RegexUtils.singleExtract(locationInfo,".*区(.*?)]",1);
//        String subwayNo = RegexUtils.singleExtract(locationInfo,"(\\d.{2}(\\(.*\\))?)(.*)",1);
//        String subwayName = RegexUtils.singleExtract(locationInfo,"(\\d.{2}(\\(.*\\))?)(.*)",3);
//
//        Elements detailHouse = element.select(".txt > .detail > p > span");
//        String area = RegexUtils.singleExtract(detailHouse.get(0).text(),"(\\d*(\\.\\d*)?)",0);
//        String houseHight = detailHouse.get(1).text();
//        String houseLayout = detailHouse.get(2).text();
//        String howfar = detailHouse.get(4).text();
//
//        if(StringUtils.isNotBlank(detailLocation)) {
//            house.setDetailLocation(detailLocation);
//        }
//        if(StringUtils.isNotBlank(area)) {
//            house.setArea(Double.parseDouble(area));
//        }
//        house.setRegion(region);
//        house.setLocation(location);
//        house.setSubwayNo(subwayNo);
//        house.setSubwayName(subwayName);
//        house.setHouseHight(houseHight);
//        house.setHouseLayout(houseLayout);
//        house.setHowfar(howfar);
//        return house;
//    }
//
//    /**
//     *
//     * @param element
//     * @param house
//     */
//    private void processPriceDetail(Element element,House house){
//        String price = RegexUtils.singleExtract(element.select(".priceDetail > .price").text(),".*?(\\d+).*",1);
//        String detailUrl = "http:" + element.select(".priceDetail > .more > a").attr("href");
//        house.setPrice(Integer.parseInt(price));
//        house.setDetailInfoUrl(detailUrl);
//    }

    @Override
    public Site getSite() {
        return this.site;
    }
}
