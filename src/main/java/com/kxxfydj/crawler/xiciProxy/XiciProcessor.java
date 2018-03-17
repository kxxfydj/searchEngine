package com.kxxfydj.crawler.xiciProxy;

import com.kxxfydj.common.CommonTag;
import com.kxxfydj.common.PipelineKeys;
import com.kxxfydj.entity.Proxy;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.nio.channels.Pipe;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kxxfydj on 2018/3/16.
 */
public class XiciProcessor implements PageProcessor {
    private Site site;

    private List<Proxy> proxyList = new ArrayList<>();

    public XiciProcessor(Site site) {
        this.site = site;
    }

    @Override
    public void process(Page page) {
        String type = (String) page.getRequest().getExtra(CommonTag.TYPE);
        if(CommonTag.FIRST_PAGE.equals(type)){
            processFirstPage(page);
        }
    }

    private void processFirstPage(Page page){
        Document document = page.getHtml().getDocument();
        Elements trs = document.select("#ip_list > tbody > tr");

        trs.stream().filter(element -> element.children().size() == 8 && !"subtitle".equals(element.className()))
                .forEach(element -> {
                    String ip = element.child(1).text();
                    String port = element.child(2).text();
                    String type = element.child(5).text().toLowerCase();

                    Proxy proxy = new Proxy();
                    proxy.setIp(ip);
                    proxy.setPort(Integer.parseInt(port));
                    proxy.setType(type);
                    proxy.setSpeed(Long.MAX_VALUE);
                    proxy.setEnabled(true);
                    proxy.setUsedTimes(0);
                    proxyList.add(proxy);
                });

        page.putField(PipelineKeys.PROXY_LIST,proxyList);
        page.putField(PipelineKeys.FINISHED,true);
    }

    @Override
    public Site getSite() {
        return this.site;
    }
}
