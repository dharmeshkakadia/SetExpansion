package webdb;

import java.util.ArrayList;

import parser.WebPage;

public class SearchResult {
	String query;
	ArrayList<WebPage> results;

	public SearchResult() {
		// TODO Auto-generated constructor stub
	}

	
	public SearchResult(String query, ArrayList<WebPage> results) {
		super();
		this.query = query;
		this.results = results;
	}


	public ArrayList<WebPage> getResults() {
		return results;
	}
	
	public void setQuery(String query) {
		this.query = query;
	}
	
	public String getQuery() {
		return query;
	}
	
	public void setResults(ArrayList<WebPage> results) {
		this.results = results;
	}
}
