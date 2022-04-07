import service.CrawlerService;
import service.CrawlerServiceImpl;

public class CrawlerApp {

    public static final String BASE_URL = "https://tomblomfield.com/";

    public static void main(String[] args) {
        CrawlerService crawlerService = new CrawlerServiceImpl();
        try {
            crawlerService.crawl(BASE_URL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
