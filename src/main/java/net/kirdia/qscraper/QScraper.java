package net.kirdia.qscraper;

import com.google.common.base.Splitter;
import com.google.common.io.Files;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.kirdia.qscraper.scraper.Scraper;
import net.kirdia.qscraper.scraper.ScraperFactory;
import org.apache.commons.codec.Charsets;

/**
 *
 * @author kirdia
 */
public class QScraper {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String configPath = "";
        List<Map<String, String>> results = new ArrayList();

        if (args.length == 1) {
            configPath = args[0];
            results = scrapeMultiple(configPath);
            System.out.println(results);
        } else if (args.length == 2) {
            Map<String, String> r;
            r = scrapeSingle(args[0], args[1]);
            System.out.println(r);

        } else {
            System.out.println("Not enough arguments given");
            System.out.println("Should provide one argument, the configuration file path");
            System.out.println("or two arguments, username and password");
            System.exit(0);
        }
    }

    private static List<Map<String, String>> scrapeMultiple(String configPath) {

        List<Map<String, String>> results = new ArrayList();
        List<String> lines = new ArrayList();
        try {
            lines = Files.readLines(new File(configPath), Charsets.UTF_8);
        } catch (IOException ex) {
            System.out.println("Couldn't read configuration file");
            System.out.println(ex.getMessage());
        }
        ScraperFactory sf = new ScraperFactory();

        for (String line : lines) {

            List<String> columns = Splitter.on(",").
                    omitEmptyStrings().trimResults().splitToList(line);
            if (columns.isEmpty()) {
                continue;
            }
            String scraperName = columns.get(0);
            String username = columns.get(1);
            String password = columns.get(2);
            Scraper scr = sf.getScraper(scraperName, username, password);

            String output = "";
            try {
                output = scr.scrape();
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
            Map<String, String> r = scr.getResults();
            r.put("mobileId", columns.get(4));
            results.add(r);

        }
        return results;
    }

    private static Map<String, String> scrapeSingle(String username, String password) {

        ScraperFactory sf = new ScraperFactory();
        Scraper scr = sf.getScraper("Qtelecom", username, password);

        String output = "";
        try {
            output = scr.scrape();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        Map<String, String> r = scr.getResults();
        return r;
    }
}
