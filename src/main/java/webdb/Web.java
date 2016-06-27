package webdb;

import java.net.UnknownHostException;
import java.util.ArrayList;

import util.LogUtil;
import util.SearchProvider;

import Parser.WebPage;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.util.JSON;

public class Web {
	private static MongoClient mongoClient;
	private static DB db;
	private static DBCollection webCollection;
	private static DBCollection searchCollection;
	
	static{
		mongoClient = new MongoClient();
		db = mongoClient.getDB("web");
		webCollection = db.getCollection("urlCollection");
		searchCollection = db.getCollection("searchCollection");
	}
	
	private static void insert(String url, String html){
		BasicDBObject obj = new BasicDBObject("url",url).append("html", html);
		webCollection.insert(obj);
	}
	
	public static ArrayList<WebPage> getSearchResults(ArrayList<String> seedList, String concept, int noOfResults, double overlapTolerance){
		BasicDBObject dbquery = new BasicDBObject();
		String query = SearchProvider.constructQuery(seedList, concept);
        dbquery.put("query", query);
        DBCursor cur = searchCollection.find(dbquery);
    	Gson gson = new Gson();

        if(cur.count()>0){
            JsonParser p = new JsonParser();
        	SearchResult result = gson.fromJson(p.parse(cur.next().toString()), SearchResult.class);
        	return result.getResults();
        }else{
        	ArrayList<WebPage> result = SearchProvider.bingSearch(seedList, concept, noOfResults, overlapTolerance);
        	LogUtil.log.fine("Going to Bing for " + query + " got "+ result.size() + " resutls");
        	SearchResult sr = new SearchResult(query,result);
    		DBObject obj = (DBObject)JSON.parse(gson.toJson(sr));
    		searchCollection.save(obj);
        	return result;
        }
	}
	
	public static String getPageHtml(String url){
		BasicDBObject query = new BasicDBObject();
        query.put("url", url);
        DBCursor cur = webCollection.find(query);
        
        if(cur.count()>0){
        	return (String)cur.next().get("html");
        }else{
        	String html = SearchProvider.getHtml(url);
        	insert(url,html);
        	return html;
        }
	}
}
