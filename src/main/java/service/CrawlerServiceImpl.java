package service;

import com.fasterxml.jackson.databind.ObjectMapper;
import dto.Links;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class CrawlerServiceImpl implements CrawlerService {

    private final ConcurrentHashMap<String, Set<String>> links = new ConcurrentHashMap<>();
    private static final ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final AtomicInteger atomicInteger = new AtomicInteger(0);
    private static final CountDownLatch countDownLatch = new CountDownLatch(1);

    @Override
    public void crawl(String url) throws IOException, InterruptedException {
        atomicInteger.incrementAndGet();
        executorService.submit(new CrawlTask(links, executorService, atomicInteger, countDownLatch, url, null));
        countDownLatch.await();

        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(toLinks(url)));
    }

    private Links toLinks(String url) {
        Map<String, Boolean> visitedLinks = new HashMap<>();
        return convertLinks(url, visitedLinks);
    }

    private Links convertLinks(String url, Map<String, Boolean> visitedLinks) {
        if (visitedLinks.getOrDefault(url, false)) {
            return buildLinks(url, null);
        }

        visitedLinks.put(url, true);
        Set<Links> children = links.get(url).stream()
                .map(link -> convertLinks(link, visitedLinks)).collect(Collectors.toSet());

        return buildLinks(url, children);
    }

    private Links buildLinks(String parentUrl, Set<Links> childrenUrls) {
        return Links.builder()
                .parentUrl(parentUrl)
                .childrenUrls(childrenUrls)
                .build();
    }
}
