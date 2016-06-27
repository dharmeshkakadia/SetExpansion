package concept;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import util.IRUtil;
import util.LogUtil;
import util.MapUtil;
import util.SearchProvider;
import webdb.Web;
import parser.WebList;
import parser.WebPage;


public class Runner {
	private static final double VARIANCE = 0.20;
	//private static final double OVERLAP_TOLERANCE = 0.4;
	//private static final int STOP_THRESHOLD = 4;
	static int fileCount = 1;
	final int seedThrehold = 10;
	static ArrayList<WebPage> seedPages = new ArrayList<WebPage>();
	static HashMap<String,Double> finalSeedScores = new HashMap<String, Double>();
	/**
	 * @param args
	 * @throws IOException 
	 * @throws MalformedURLException 
	 */
	public static void main(String[] args) throws IOException {
		int noOfResults = Integer.parseInt(args[2]);

		BufferedReader reader = null;
		FileWriter writer = null;
		String line;
		ArrayList<String> seedList = new ArrayList<String>();
		try {
			reader = new BufferedReader(new FileReader(args[0]));
			writer = new FileWriter(args[1]);
			while ((line = reader.readLine()) != null && !line.equals("")) {
				LogUtil.log.info("======= "+line+" =======");
				seedList.addAll(Arrays.asList(line.toLowerCase().split(" ")));
				writer.append(expandSeed(seedList,noOfResults).toString());
			}
			reader.close();
			writer.close();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	private static ArrayList<String> expandSeed(ArrayList<String> seedList, int noOfResults) throws IOException {
		finalSeedScores.clear(); // very imp
		
		// step 0 get the initial concept
		String concept= Extractor.getConcept(seedList,null,noOfResults,1);
		LogUtil.log.info("got initial concept as : " +concept);
		ArrayList<String> newSeedList = new ArrayList<String>(seedList);
		int iteration = -1;
		for(String seed : seedList){
			finalSeedScores.put(seed, 1.0);
		}
		do{
			seedList=newSeedList;
			
			//String newSeed = getNextSeed(seedList, concept, noOfResults,1/(1+Math.pow(Math.E,-iteration)));
			String newSeed = getNextSeed(seedList, concept, noOfResults,1);

			LogUtil.log.info(newSeed + " avgFinalSeedScore()="+avgFinalSeedScore() +" and seedScore="+finalSeedScores.get(newSeed));

			if(finalSeedScores.get(newSeed) >= avgFinalSeedScore()*VARIANCE){
				LogUtil.log.info("Adding the new Seed : "+newSeed);
				newSeedList.add(newSeed);
			}else{
				LogUtil.log.info("Stopping with "+seedList);
				break;
			}
			LogUtil.log.info("New Seed list is : "+newSeedList);

			//newSeedList=getNewSeedList(seedList,noOfResults,concept,1/(1+Math.pow(Math.E,-iteration)));
			//System.out.println("*******************************" + 1/(1+Math.pow(Math.E,-iteration)));
			//newSeedList=getExpandedSeedSet(seedList,noOfResults,concept,OVERLAP_TOLERANCE);

			// TODO step 2 go to web one more time to validate seed

			// setp 3 extract the concept
			String candidateConcept = Extractor.getConcept(seedList,null,noOfResults,1);

			//if(Extractor.isValidConcept(newSeedList,noOfResults, candidateConcept)){
				concept=candidateConcept;
			//}
			LogUtil.log.info("got concept for "+newSeedList + " as : "+concept);

			iteration++;	
		}while(true);

		return seedList;
	}

	private static Double avgFinalSeedScore() {
		double total=0;
		for(Double score :finalSeedScores.values()){
			total += score;
		}
		return total/finalSeedScores.size();
	}

	/*private static double getSeedScore(String newSeed, ArrayList<String> seedList) throws IOException {
		
		Search.fetch(newSeed);
	    ArrayList<String> listURL = ResultParser.parser();
	    ListFinderHTML myfinder = new ListFinderHTML();
		ArrayList<WebPage> allPages = new ArrayList<WebPage>();

		for(String url:listURL){
			WebPage page = new WebPage(myfinder.getTitle(), url);
			 ArrayList<String> mylist= new ArrayList<String>();
			 myfinder.SetHTML(new URL(url));


			 while ((mylist = myfinder.getNextList())!=null) {
		        if(isValid(mylist,seedList)>0){
		        	page.addList(new WebList(mylist, myfinder.getHeader(), myfinder.getDescription()));
		        }
		     }
			 allPages.add(page);
		}
		 
		//return newSeed == null ? false: true;
		return 1;
	}*/

	public static String getNextSeed(ArrayList<String> seedList, String concept, int noOfResults, double overlapTolerance){
		HashMap<String, Integer> freqMap = new HashMap<String, Integer>();
		HashMap<String,Set<String>> docMap = new HashMap<String, Set<String>>();
		
		ArrayList<WebPage> listPages = Web.getSearchResults(seedList,concept,noOfResults,overlapTolerance);
		LogUtil.log.fine("ListPages.size()" + listPages.size());
		seedPages.addAll(listPages); // mem or not ?
		for(WebPage wp : seedPages){
			for(WebList wl : wp.getAllList()){
				LogUtil.log.finer("WebLists for "+wp.getUrl() + "\n" + wl.getList());
				for(String item : wl.getList()){
					boolean isValid = Extractor.isValid(item,seedList);
					if(isValid){
						MapUtil.updateFrequency(freqMap, item.toLowerCase(), 1);
						MapUtil.updateDocFrequency(docMap, item.toLowerCase(), wp.getUrl());
					}
				}
			}
		}

		LogUtil.log.finer("freqMap in getNextSeed : " + freqMap);
		LogUtil.log.finer("docMap in getNextSeed : " + docMap);
/*
		Entry<String, Integer> preEntry = null;
		HashMap<String, Integer> map2 = new HashMap<String, Integer>();
		for(Entry<String, Integer> e : freqMap.entrySet()){
			if(preEntry == null){
				preEntry=e;
			}else{
				String lcs=StringUtil.longestCommonSubstring(e.getKey(),preEntry.getKey());
				//MapUtils.updateFrequency(map2,StringUtils.longestCommonSubstring(e.getKey(),preEntry.getKey()),e.getValue()+preEntry.getValue());
				if(Extractor.isValid(lcs, seedList)){
					MapUtil.updateFrequency(map2,lcs,(int) (StringUtil.compareStrings(e.getKey(),preEntry.getKey())*10));
				}
			}
		}


		HashMap<String, Integer> map3 = new HashMap<String, Integer>();
		for(Entry<String, Integer> e3 : map2.entrySet()){
			String[] arr = e3.getKey().split(IRUtil.Token);
			Set<String> uniq = new HashSet<String>(Arrays.asList(arr));
			for(String word : uniq){
				if(Extractor.isValid(word,seedList)){
					MapUtil.updateFrequency(map3,word,3);	
				}
			}

		}

		HashMap<String, Integer> freqMap2 = new HashMap<String, Integer>();
		freqMap2.putAll(freqMap);
		freqMap2.putAll(map2);
		freqMap2.putAll(map3);

		LogUtil.log.finer("Map2 in listExpander : " + map2);
		LogUtil.log.finer("Map3 in listExpander : " + map3);
*/
	/*	String maxWord = null;
		double maxScore = -1;
		LogUtil.log.fine("in listExpander() getting the best  5 candidate : ");
		//for(Entry<String, Set<String>> e : docMap.entrySet()){
		for(Entry<String, Integer> e : freqMap.entrySet()){
			int tf1 = freqMap.get(e.getKey());
			//int tf2 = freqMap2.get(e.getKey());
			double idf = IRUtil.getIdf(docMap, e);
			double score = tf1;//*idf;
			//LogUtil.log.fine(e.getKey() + " : tf1=" +tf1+" :tf2="+tf2+" :idf="+idf+" :tf1*idf="+score+" :tf2*idf="+tf2*idf);
			LogUtil.log.fine(e.getKey() + " : tf1=" +tf1 + ":idf="+idf+" :tf1*idf="+tf1*idf);
			if(maxScore < score){
				maxScore = score;
				maxWord = e.getKey();
			}
		}*/
		
		LogUtil.log.fine("in getNextSeed() getting the best "+ 5 + " candidates : ");
		TreeMap<Double,String> seedScoreMap = new TreeMap<Double,String>();
		for(Entry<Double, String> en : MapUtil.getTopKEntries(freqMap, 5)){
			String candidateSeed =en.getValue();
			double s = getSeedScore(candidateSeed,seedList,noOfResults);
			LogUtil.log.info("Trying out seed "+candidateSeed+" which had tf="+en.getKey()+": got score :" +s);
			seedScoreMap .put(s,candidateSeed);
		}
		
		finalSeedScores.put(seedScoreMap.lastEntry().getValue(), seedScoreMap.lastEntry().getKey());
		
		return seedScoreMap.lastEntry().getValue();
	}

	private static double getSeedScore(String candidateSeed, ArrayList<String> seedList, int noOfResults) {
		ArrayList<String> mockSeedList = new ArrayList<String>();
		mockSeedList.add(candidateSeed);
		ArrayList<String> newSeed = SearchProvider.bingUrls(mockSeedList, null, noOfResults,1);
		ArrayList<String> seeds = SearchProvider.bingUrls(seedList, null, noOfResults,1);
		double score=0,temp=0;
		for(String c : newSeed){
			for(String s : seeds){
				temp=IRUtil.compareDocsWithSeed(c,s,seedList);
				LogUtil.log.fine(candidateSeed + " : "+ c +" : "+s+" = "+temp);
				score += temp;
			}
		}
		LogUtil.log.fine(seedList + " : " + candidateSeed + " scored = "+score);
		return score;
	}
}
