package com.jachs.elasticsearch;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.client.transport.TransportClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/***
 * 
 * @author zhanchaohan
 *
 */
public class add {
    private TransportClient client;
    private String EsIndex="etest";
    private String EsType="doc";

    @Before
    public void init () throws FileNotFoundException, IOException {
    	ElasticConfig ec=new ElasticConfig();
    	client=ec.init();
    }

    @After
    public void destroy () {
        if ( client != null ) {
            client.close ();
        }
    }

    // 单条随机ID
    @Test
    public void add () {
        Map<String, Object> data = new HashMap<String, Object> ();
        data.put ( "name", "zhangsan" );
        data.put ( "age", 18 );
        IndexRequestBuilder indexRequestBuilder = client.prepareIndex ( EsIndex, EsType );
        indexRequestBuilder.setSource ( data );
        indexRequestBuilder.execute ();
    }

    // 单条制定ID
    @Test
    public void add1 () {
        Map<String, Object> data = new HashMap<String, Object> ();
        data.put ( "name", "zhangsan" );
        data.put ( "age", 18 );
        IndexRequestBuilder indexRequestBuilder = client.prepareIndex ( EsIndex, EsType, "id01" );
        indexRequestBuilder.setSource ( data );
        indexRequestBuilder.execute ();
    }

    // 单条
    @Test
    public void add2 () {
        IndexRequestBuilder indexRequestBuilder = client.prepareIndex ( EsIndex, EsType );

        indexRequestBuilder.setSource ( "name", "zhangsan", "age", "18" );
        indexRequestBuilder.execute ();
    }

    // 多条
    @Test
    public void add3 () {
        Map<String, Object> map = new HashMap<String, Object> ();
        map.put ( "name", "zhangsan" );
        map.put ( "age", 18 );

        Map<String, Object> map1 = new HashMap<String, Object> ();
        map1.put ( "name", "lisi" );
        map1.put ( "age", 19 );

        BulkRequestBuilder bulkRequestBuilder = client.prepareBulk ();
        bulkRequestBuilder.add ( client.prepareIndex ( EsIndex, EsType ).setSource ( map ) );
        bulkRequestBuilder.add ( client.prepareIndex ( EsIndex, EsType ).setSource ( map1 ) );
        BulkResponse bulkResponse = bulkRequestBuilder.execute ().actionGet ();
        Iterator<BulkItemResponse> bulkItemResponse = bulkResponse.iterator ();
        while ( bulkItemResponse.hasNext () ) {
            BulkItemResponse itemResponse = bulkItemResponse.next ();
            System.out.println ( itemResponse.getItemId () );
        }
    }

    @Test
    public void add4 () {
        BulkRequestBuilder bulkRequestBuilder = client.prepareBulk ();
        for ( int i = 0 ; i < 2 ; i++ ) {
            Map<String, Object> map = new HashMap<String, Object> ();
            map.put ( "name", "njhiaw" + i );
            map.put ( "gent", "男" );
            map.put ( "age", new Random ().nextInt () * 10 );
            map.put ( "region", "中国" );
            bulkRequestBuilder.add ( client.prepareIndex ( EsIndex, EsType ).setSource ( map ) );
        }

        BulkResponse bulkResponse = bulkRequestBuilder.execute ().actionGet ();
        Iterator<BulkItemResponse> bulkItemResponse = bulkResponse.iterator ();
        while ( bulkItemResponse.hasNext () ) {
            BulkItemResponse itemResponse = bulkItemResponse.next ();
            System.out.println ( itemResponse.getItemId () );
        }
    }
    
    
}
