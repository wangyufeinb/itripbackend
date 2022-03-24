package com.bdqn.dao;



import com.bdqn.pojo.ItripHotelVO;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.apache.solr.client.solrj.response.QueryResponse;

import java.io.IOException;
import java.util.List;

public class SolrMain {

    public static void main(String[] args) throws IOException, SolrServerException {
        String url="http://localhost:8080/solr/hotel/";
        HttpSolrClient solr=new HttpSolrClient(url);
    solr.setParser(new XMLResponseParser());//设置相应解析器
    solr.setConnectionTimeout(500);//最大连接时长
        SolrQuery solrQuery=new SolrQuery();//新建查询
        solrQuery.setQuery("keyword:北京");
        solrQuery.setSort("id",SolrQuery.ORDER.desc);
        QueryResponse queryResponse=solr.query(solrQuery);
        List<ItripHotelVO> hotel=queryResponse.getBeans(ItripHotelVO.class);
        for(ItripHotelVO h:hotel){
            System.out.println(h.getId()+h.getAddress());
        }

    }
}
