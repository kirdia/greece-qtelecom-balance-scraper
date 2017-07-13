package net.kirdia.qscraper.scraper;

/**
 *
 * @author kirdia
 */
public class ScraperFactory {
    
    public Scraper getScraper(String pr, String u, String p) {
        
        if (pr.equals("Qtelecom")) {
            return new QtelecomScraper(u, p);
        }        
        return null;
    }
}
