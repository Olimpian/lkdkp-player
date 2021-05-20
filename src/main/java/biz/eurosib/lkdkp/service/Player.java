package biz.eurosib.lkdkp.service;

import biz.eurosib.lkdkp.cachedb.CacheAnswer;
import biz.eurosib.lkdkp.cachedb.CacheAnswerRepository;
import biz.eurosib.lkdkp.client.WfcClient;
import biz.eurosib.lkdkp.kafka.KafkaConsumerConfig;
import biz.eurosib.lkdkp.kafka.*;
import biz.eurosib.lkdkp.keycloak.UserDto;
import biz.eurosib.lkdkp.logger.FluentdLogger;
import com.google.gson.Gson;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Service
public class Player {
    private final KafkaTemplate<Long, ResponseDto> kafkaTemplate;
    private final WfcClient wfcClient;
    private final CacheAnswerRepository repository;
    private final UserManager userManager;
    private final KafkaConsumerConfig consumerConfig;
    private final KafkaProducerConfig producerConfig;
    private final FluentdLogger log;


    @Autowired
    public Player(KafkaTemplate<Long, ResponseDto> kafkaTemplate,
                  WfcClient wfcClient,
                  CacheAnswerRepository repository,
                  UserManager userManager,
                  KafkaConsumerConfig consumerConfig,
                  KafkaProducerConfig producerConfig,
                  FluentdLogger log) {
        this.kafkaTemplate = kafkaTemplate;
        this.wfcClient = wfcClient;
        this.repository = repository;
        this.userManager = userManager;
        this.consumerConfig = consumerConfig;
        this.producerConfig = producerConfig;
        this.log = log;


        log.info("Player started");
    }

    @KafkaListener(id = "Request",
            topics = {"#{@kafkaConsumerConfig.getRequestQueue()}"},
            containerFactory = "singleFactory")
    public void consume(RequestDto dto) {
        JSONObject requestJson = new JSONObject(dto.getData());

        log.info("=> consumed {}", requestJson.get("request").toString());
        ResponseDto result;


        if(requestJson.get("request").toString().equals("createUser")) {
            result = wfcClient.convert(dto);
            Gson gson = new Gson();
            UserDto user = gson.fromJson(requestJson.get("data").toString().toLowerCase(), UserDto.class);
            userManager.createUser(user, result.getGuid());
            //kafkaTemplate.send("lkdkp.user", result);
        }

        if (isCached(requestJson.get("request").toString())) {
            log.info("Request \"" + requestJson.get("request").toString() + "\" get from cache db");
            List<CacheAnswer> answer = repository.findByRequest(requestJson.get("request").toString());
            result = new ResponseDto(answer.get(0).getData());
        } else {
            log.info("Request \"" + requestJson.get("request").toString() + "\" get from wsdl");
            result = wfcClient.convert(dto);
        }

        log.info("<= sending {}", result.getResult() + ", " + result.getData());
        result.setTaskGuid(dto.getTaskGuid());
        result.setData(result.getData().toString());
        kafkaTemplate.send(producerConfig.getResultQueue(), result);
    }

    //@Scheduled(initialDelay = 2000, fixedDelay = 2000)
    public void isCached() {
        final String uri = "http://localhost:8082/cached?request=getLocationList";
        RestTemplate synchManager = new RestTemplate();
        Boolean isCached = synchManager.getForObject(uri, Boolean.class);
        System.err.println("isCached = " + isCached);
    }

    private boolean isCached(String requestName) {
        String uri = consumerConfig.getSynchManagerUri() + requestName;
        RestTemplate synchManager = new RestTemplate();
        try {
            Boolean isCached = synchManager.getForObject(uri, Boolean.class);
            log.info("synchManager \"" + requestName + "\" is cached = " + isCached);
            return isCached != null ? isCached : false;
        } catch (Throwable ex) {
            log.warn("synchManager is NOT online");
            return false;
        }
    }

    //@Scheduled(initialDelay = 2000, fixedDelay = 2000)
    public void logTest() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("level", "error");
        map.put("time", LocalDateTime.now().toString());
        log.error("follow" , map);
    }
}
