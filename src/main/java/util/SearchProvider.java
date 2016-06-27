package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import net.billylieurance.azuresearch.AzureSearchResultSet;
import net.billylieurance.azuresearch.AzureSearchWebQuery;
import net.billylieurance.azuresearch.AzureSearchWebResult;
import util.GoogleResults.Result;
import webdb.Web;

import parser.ListFinderHTML;
import parser.WebList;
import parser.WebPage;

import com.google.gson.Gson;

public class SearchProvider {
	//private static final String APP_ID = "sWfZW7SsZBVnnuf9UJl7DYhaJBqVUTKB/k9/b7f/zCQ="; // Dharmesh
	//private static final String APP_ID = "4YlNS8WSm+N9DNfJoGwsZ0G9CQrtCot7t0x+dw64PL0=";
	private static final String APP_ID = "oz+hcEJE0S+ZDjBNO+viPx+2t85Hayx7rJiCxdfuVlw="; // Dharmesh SetExpansion
	
	static {
		System.setProperty("https.proxyHost", "proxy.iiit.ac.in");
		System.setProperty("https.proxyPort", "8080");

		System.setProperty("http.proxyHost", "proxy.iiit.ac.in");
		System.setProperty("http.proxyPort", "8080");
	}
	
	public static String constructQuery(ArrayList<String> seedList, String concept) {
		String ret = new String();
		for(String s : seedList){
			ret = ret + " " + s;
		}
		return concept==null ? ret : " \"List of "+concept + "\""+ret;
	}

	public static ArrayList<WebPage> bingSearch(ArrayList<String> seedList, String concept, int noOfResults, double overlapTolerance) {
		ArrayList<WebPage> listPages = new ArrayList<WebPage>();
		AzureSearchWebQuery query = new AzureSearchWebQuery();
		ListFinderHTML myfinder = new ListFinderHTML();
		
		query.setAppid(APP_ID);
		query.setQuery(constructQuery(seedList, concept));
		query.setMarket("en-us");
        //aq2.setPage(2);
		query.setPerPage(noOfResults);
		query.doQuery();
        AzureSearchResultSet<AzureSearchWebResult> results = query.getQueryResult();
        LogUtil.log.info("Searching for : "+query.getQuery()+" : got "+ results.getASRs().size()+" results");
        for(AzureSearchWebResult r : results){
			myfinder.SetHTML(r.getUrl());
        	WebPage page = new WebPage(r.getTitle(), r.getUrl(),r.getDescription());
        	ArrayList<String> webList= new ArrayList<String>();
			while ((webList = myfinder.getNextList())!=null && webList.size()>0) {
				if(ListUtil.getOverLap(webList,seedList)>=(seedList.size()*overlapTolerance)){
					page.addList(new WebList(webList, myfinder.getHeader(), myfinder.getDescription()));
				}
			}
			listPages.add(page);
        }
        
        return listPages;
	}
	
	public static ArrayList<String> bingUrls(ArrayList<String> seedList, String concept, int noOfResults, double overlapTolerance) {
		ArrayList<String> listURL = new ArrayList<String>();
		ArrayList<WebPage> results = Web.getSearchResults(seedList, concept, noOfResults, overlapTolerance);
        
        for(WebPage r : results){
        	//System.out.println(r.getUrl());
        	listURL.add(r.getUrl());
        	//LogUtil.log.fine(r.getDescription());
        	//System.out.println(r.getTitle() + " :: " + r.getDescription());
        }
        
        return listURL;
	}
	
	public static ArrayList<String> googleSearch(String inputQuery, int noOfResults) throws IOException {
		ArrayList<String> urls = new ArrayList<String>();
		String google = "http://ajax.googleapis.com/ajax/services/search/web?v=1.0&q=";
	    String search = inputQuery;
	    String charset = "UTF-8";

	   // for(int i=0; i<10;i++){
	    	//URL url = new URL(google + URLEncoder.encode(search, charset)+"&start="+i);
	    	URL url = new URL(google + URLEncoder.encode(search, charset));
		    Reader reader = new InputStreamReader(url.openStream(), charset);
		    GoogleResults results = new Gson().fromJson(reader, GoogleResults.class);

		    for(Result r :results.getResponseData().getResults()){
		    	//System.out.println(r);
		    	urls.add(r.getUrl());
		    }	
	    //}
	    
	    	    // Show title and URL of 1st result.
	    //System.out.println(results);
	    return urls;
	}
	
	public static String getHtml(String url){
		System.setProperty("http.proxyHost", "proxy.iiit.ac.in");
		System.setProperty("http.proxyPort", "8080");
		LogUtil.log.fine("Going to web for : "+url );
		BufferedReader in;
		String inputLine;
		StringBuilder sb = new StringBuilder();
		try {
			in = new BufferedReader(new InputStreamReader(new URL(url).openStream()));
			while ((inputLine = in.readLine()) != null){
				sb.append(inputLine).append("\n");
			}
		} catch (MalformedURLException e) {
			LogUtil.log.fine(e.toString());
		} catch (IOException e) {
			LogUtil.log.fine(e.toString());
		}
		return sb.toString();
	}
}
