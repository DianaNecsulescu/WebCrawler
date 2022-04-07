package service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static utils.LinkUtils.*;

public class CrawlTask implements Runnable {

    private final ConcurrentHashMap<String, Set<String>> visitedLinks;
    private final ExecutorService executorService;
    private final AtomicInteger atomicInteger;
    private final CountDownLatch countDownLatch;
    private final String url;
    private final String parentUrl;

    public CrawlTask(ConcurrentHashMap<String, Set<String>> visitedLinks, ExecutorService executorService,
                     AtomicInteger atomicInteger, CountDownLatch countDownLatch, String url, String parentUrl) {
        this.visitedLinks = visitedLinks;
        this.executorService = executorService;
        this.atomicInteger = atomicInteger;
        this.countDownLatch = countDownLatch;
        this.url = url;
        this.parentUrl = parentUrl;
    }

    @Override
    public void run() {
        if (isValidLink(url)) {
            if (parentUrl != null) {
                visitedLinks.get(parentUrl).add(url);
            }
            if (visitedLinks.get(url) == null) {
              visitLink();
            }
        }

        int remainingTasks = atomicInteger.decrementAndGet();
        if (remainingTasks == 0) {
           shutdownExecutorService();
        }
    }

    private void visitLink() {
        visitedLinks.put(url, new HashSet<>());
        try {
            Document document = Jsoup.connect(url).get();
            Elements linksOnPage = document.select(LINK_TAG);

            for (Element childLink : linksOnPage) {
                atomicInteger.incrementAndGet();
                executorService.submit(new CrawlTask(visitedLinks, executorService, atomicInteger, countDownLatch,
                        childLink.attr(ABSOLUT_PATH), url));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void shutdownExecutorService() {
        executorService.shutdown();
        countDownLatch.countDown();

        try {
            executorService.awaitTermination(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            countDownLatch.countDown();
        }
    }
}
