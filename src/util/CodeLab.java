package util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import net.billylieurance.azuresearch.AzureSearchResultSet;
import net.billylieurance.azuresearch.AzureSearchWebResult;

import org.jsoup.Jsoup;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import Parser.WebList;
import Parser.WebPage;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
import com.mongodb.MongoClient;

import webdb.Web;

import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import concept.Extractor;
import concept.Runner;
import de.l3s.boilerpipe.BoilerpipeProcessingException;
import de.l3s.boilerpipe.document.TextDocument;
import de.l3s.boilerpipe.extractors.ArticleExtractor;
import de.l3s.boilerpipe.extractors.CanolaExtractor;
import de.l3s.boilerpipe.extractors.DefaultExtractor;
import de.l3s.boilerpipe.extractors.KeepEverythingExtractor;
import de.l3s.boilerpipe.extractors.LargestContentExtractor;
import de.l3s.boilerpipe.sax.BoilerpipeSAXInput;
import de.l3s.boilerpipe.sax.HTMLFetcher;

public class CodeLab {
	public static void main(String[] args) throws IOException, BoilerpipeProcessingException, SAXException, URISyntaxException {

		/*BufferedReader reader = new BufferedReader(new FileReader("/home/dharmesh/table.txt"));
		String line="";
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		while((line=reader.readLine())!=null){
			map.put(line, 1);
		}

		Entry<String, Integer> preEntry = null;
		HashMap<String, Integer> map2 = new HashMap<String, Integer>();
		for(Entry<String, Integer> e : map.entrySet()){
			if(preEntry == null){
				preEntry=e;
			}else{
				//MapUtils.updateFrequency(map2,StringUtils.longestCommonSubstring(e.getKey(),preEntry.getKey()),e.getValue()+preEntry.getValue());
				MapUtil.updateFrequency(map2,StringUtil.longestCommonSubstring(e.getKey(),preEntry.getKey()),(int) (StringUtil.compareStrings(e.getKey(),preEntry.getKey())*10));
			}
		}

		reader.close();*/

		/*Entry<String, Integer> preEntry3 = null;
		HashMap<String, Integer> map3 = new HashMap<String, Integer>();
		for(Entry<String, Integer> e : map2.entrySet()){
			if(preEntry3 == null){
				preEntry3=e;
			}else{
				MapUtils.updateFrequency(map3,
						StringUtils.longestCommonSubstring(e.getKey(),
								preEntry3.getKey()),e.getValue()+preEntry3.getValue());
			}
		}*/

		/*HashMap<String, Integer> map3 = new HashMap<String, Integer>();
		for(Entry<String, Integer> e3 : map2.entrySet()){
			String[] arr = e3.getKey().split(" ");
			Set<String> uniq = new HashSet<String>(Arrays.asList(arr));
			for(String word : uniq){
				if(Extractor.isValid(word)){
					MapUtil.updateFrequency(map3,word,e3.getValue());	
				}
			}

		}

		System.out.println(map2);
		System.out.println();
		System.out.println(map3);


		String s1 = StringUtils.longestCommonSubstring("ajj-mmc emu local (43420)","ajj-mmc emu local (43436)");
		String s2 = StringUtils.longestCommonSubstring("ajj-mmc passr (66010)","ajj-mmc emu local (43412)");
		System.out.println(StringUtils.longestCommonSubstring(s1, s2));

		System.out.println(StringUtil.compareStrings("ajj-mmc emu local","ajj-mmc emu local"));
		System.out.println(StringUtil.compareStrings("brmt pune passenger","passenger"));
		 */


		//		ArrayList<String> sentences = new ArrayList<String>();
		//		ArrayList<String> headings = new ArrayList<String>();
		//		ArrayList<String> titles = new ArrayList<String>();
		//		Extractor ex = new Extractor();
		//		sentences.add("list of cars");
		//		headings.add("This is headings");
		//		titles.add("Pre-Owned Inventroy - Nissan, Ford, Toyota, Hyundai, Chevrolet, GM, Mini, Mazda, Infinitit, Suzuki, Volkswagen");
		//		System.out.println(ex.extract(sentences, headings, titles));
		/*
		 ArrayList<String> mylist;
	        String header;

	        ListFinderHTML myfinder = new ListFinderHTML();

	        //myfinder.SetHTML(mystrings[0]);
	        myfinder.SetHTML(new URL("http://www.actabit.com/diet-nutrition/dieters-friends-orange-apple-and-banana"));


	        while ((mylist = myfinder.getNextList())!=null)
	        {

	        header = myfinder.getHeaderenthiran();

	        System.out.println("Next List");
	        System.out.println("Title: " + myfinder.getTitle());
	        System.out.println("Header: " + header);
	        System.out.println("Description: " + myfinder.getDescription());
	        System.out.println("List items:");

	        for(int i =0;i < mylist.size();i++)
	           System.out.println(mylist.get(i));
	        }


		 */
		// final InputSource is = HTMLFetcher.fetch(url).toInputSource();
		/*
        final BoilerpipeSAXInput in = new BoilerpipeSAXInput(is);
        final TextDocument doc = in.getTextDocument();

		 */// You have the choice between different Extractors

		// System.out.println(DefaultExtractor.INSTANCE.getText(doc));


		//System.out.println(SearchProvider.bingRawSearch("india",20).getWebTotal());
		//System.out.println(ArticleExtractor.INSTANCE.getText(new URL("http://www.liveindia.com/news/1n.html")));
		/*System.out.println(IRUtil.compareDocs("http://100wondersoftheworld.com/index.html",
				"http://www.travelchoice.in/"
				//"http://articles.economictimes.indiatimes.com/2012-10-03/news/34238920_1_realty-project-dubai-multi-purpose-project"
				));*/
		/*System.out.println(Web.getPageHtml("http://www.msnbc.msn.com/id/15700352/ns/travel-news/t/new-wonders-attract-widespread-interest/"));
		System.out.println(Web.getPageHtml("http://www.msnbc.msn.com/id/15700352/ns/travel-news/t/new-wonders-attract-widespread-interest/"));
		System.out.println(Web.getPageHtml("http://yahoo.com"));
		System.out.println(Web.getPageHtml("http://www.msnbc.msn.com/id/15700352/ns/travel-news/t/new-wonders-attract-widespread-interest/"));*/

		/*System.out.println(IRUtil.compareDocs("http://www.foxnews.com/entertainment/2012/12/12/10-worst-social-media-screw-ups-2012/",
				"http://www.motortrend.com/roadtests/hatchbacks/1006_ford_fiesta_honda_fit_toyota_yaris_nissan_versa_comparison/viewall.html"));*/


		/*HashMap<String, Integer> freqMap = new HashMap<String, Integer>();
		freqMap.put("Dharmesh", 2);
		freqMap.put("int", 1);
		freqMap.put("double", 5);
		freqMap.put("i6nt", 3);
		freqMap.put("trty", 1);
		freqMap.put("sdlfhkf", 6);
		//freqMap.put("int", 10);
		System.out.println(MapUtil.getTopKEntries(freqMap, 7));*/
		ArrayList<String> seedList = new ArrayList<String>();
		seedList.add("melbourne");seedList.add("tokyo");seedList.add("zaire");seedList.add("delhi");
		//System.out.println(IRUtil.compareDocsWithSeed("http://topics.nytimes.com/top/news/international/countriesandterritories/india/index.html", "http://www.librarything.com/place/Melbourne%2C+Victoria%2C+Australia", seedList));

		/*String noHTMLString = Jsoup.parse(SearchProvider.getHtml("http://www.amsterdamcityguide.info/contact.asp")).text();
		System.out.println(noHTMLString);*/
		//System.out.println(Runner.getNextSeed(seedList, "india", 30, 1));
		
		
		System.out.println(Web.getSearchResults(seedList, null, 30, 1));
	}

	private static ArrayList<String> getURLToekns(String url) {
		String[] arr = url.split(IRUtil.Token);
		return new ArrayList<String>(Arrays.asList(arr));
	}

}
