package com.example.gsasyncmethod;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * 2. сервис запросов к GitHub для поиска страниц.
 */
@Service //делая его кандидатом для сканирования компонентов в Spring для обнаружения и помещения его в
public class GitHubLookupService {
    private static final Logger logger = LoggerFactory.getLogger(GitHubLookupService.class);

    private RestTemplate restTemplate = new RestTemplate();
    /*private RestTemplate restTemplate;

    public GitHubLookupService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }*/

    @Async //будет запущен в отдельном потоке
    public Future<User> findPage(String user) throws InterruptedException {

        //System.out.println("Looking up " + user);
        logger.info("Looking up " + user);

        String url = String.format("https://api.github.com/users/%s", user);
        User results = restTemplate.getForObject( url, User.class);

        Thread.sleep(1000L);
        return new AsyncResult<User>(results);  //AsyncResult требование любого асинхронного сервиса
    }
}
