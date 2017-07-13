package net.kirdia.qscraper.scraper;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import java.io.IOException;
import java.util.List;
import net.amdtelecom.support.util.DateUtils;

/**
 *
 * @author kirdia
 */
public class QtelecomScraper extends Scraper {
    
    public QtelecomScraper(String u, String p) {
        super(u, p);
    }
    
    @Override
    public boolean login() {

        HtmlSubmitInput el;
        try {
            page = wc.getPage("https://www.wind.gr/gr/q/myq/login/?goto=%2fgr%2fq%2fmyq%2fto-upoloipo-mou%2f");
        } catch (IOException ex) {
            return false;
        } catch (FailingHttpStatusCodeException ex) {
            return false;
        }

        try {
            HtmlTextInput tx = page.getElementByName("username");
            tx.setValueAttribute(username);
            HtmlPasswordInput ps = page.getElementByName("password");
            ps.setValueAttribute(password);
            el = page.getHtmlElementById("loginbtn");
        } catch (ElementNotFoundException e) {
            return false;
        }

        try {
            page = el.click();
        } catch (IOException ex) {
            return false;
        }
        
        try {
            List tx = page.getElementsByName("username");
            if (!tx.isEmpty()) {
                return false;
            }
        } catch (ElementNotFoundException e) {
            return false;
        }        

        return true;      
    }
    
    public String scrape() throws Exception {
        results.clear();
        results.put("account", username);
        results.put("timestamp", DateUtils.getNow());        
        
        boolean loggedIn = login();
        if (!loggedIn) {
            results.put("error", "Couldn't login. The website may have changed "
                    + "or the credentials are invalid");
            return "-1";
        }
        String bal,s;
        HtmlElement el1;
        String balance;
        String smsQuantity = "0";
        
        List<HtmlElement> divs = (List<HtmlElement>) page.getByXPath("//div[contains(@class,'item')]");
        
        if (divs.size() > 0) {
            el1 = (HtmlElement) divs.get(0);
        }
        else {
            return "-2";
        }
        
        List<HtmlElement> spans = el1.getElementsByAttribute("span", "class", "price");
        if (spans.size() > 0) {
            balance = spans.get(0).asText().trim();
        }
        else {
            return "-2";
        }                    
                
        for (HtmlElement elm : divs) {
            if (!elm.asText().contains("ΥΠΟΛΕΙΠΟΜΕΝΑ SMS")) {
                continue;
            }
            List<HtmlElement> tempel = elm.getElementsByAttribute("span", "class", "quant");
            
            if (tempel.size() > 0) {
                HtmlElement temp = tempel.get(0);
                smsQuantity = temp.asText().trim();
            }
        }        
        
        if (smsQuantity.indexOf("SMS") > 0) {
            smsQuantity = smsQuantity.replace("SMS", "");
        }
        

        results.put("balance", balance.trim());
        results.put("smsQuantity",smsQuantity.trim());
        
        
        List<HtmlElement> spans2 = (List<HtmlElement>) page.getByXPath("//span[contains(@class,'orange')]");
        results.put("mobileNo",spans2.get(0).asText().split("[(]")[1].trim().replaceAll("[^0-9]*", ""));
        
        bal = balance.trim() + "€, " + smsQuantity.trim() + " SMS";
        return bal;
    }
}
