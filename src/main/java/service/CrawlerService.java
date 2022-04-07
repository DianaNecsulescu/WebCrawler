package service;

import java.io.IOException;

public interface CrawlerService {
    void crawl(String url) throws IOException, InterruptedException;
}
