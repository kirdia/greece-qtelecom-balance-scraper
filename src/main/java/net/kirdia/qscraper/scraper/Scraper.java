package net.kirdia.qscraper.scraper;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.logging.Level;

/**
 *
 * @author kirdia
 */
public class Scraper implements Callable<Map<String, String>> {
    
    protected HtmlPage page;
    protected WebClient wc;
    protected String name;
    protected String username;
    protected String password;
    protected Map<String, String> results;
    
    public Scraper(String u, String p) {        
        wc = new WebClient();
        java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(Level.OFF);
        wc.getOptions().setJavaScriptEnabled(false);
        wc.getOptions().setJavaScriptEnabled(false);
        username = u.trim();
        password = p.trim();
        results = new HashMap<String,String>();
    }
    
    public Scraper() {
        results = new HashMap<String,String>();
    }
    
    public boolean login() {return false;}
    
    public String scrape() throws Exception {return null;}
    
    public void destroy() 
    {
        if (wc != null) {
            wc = null;
        }
    }

    public Map<String, String> call() throws Exception {
        scrape();
        return results;
    }

    public Map<String, String> getResults() {
        return results;
    }

    public void setResults(Map<String, String> results) {
        this.results = results;
    }
}