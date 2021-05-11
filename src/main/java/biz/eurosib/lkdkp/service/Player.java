package biz.eurosib.lkdkp.service;

import biz.eurosib.lkdkp.cachedb.CacheAnswer;
import biz.eurosib.lkdkp.cachedb.CacheAnswerRepository;
import biz.eurosib.lkdkp.client.WfcClient;
import biz.eurosib.lkdkp.config.KafkaConsumerConfig;
import biz.eurosib.lkdkp.kafka.*;
import biz.eurosib.lkdkp.keycloak.UserDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final ObjectMapper objectMapper;
    private final WfcClient wfcClient;
    private final CacheAnswerRepository repository;
    private final UserManager userManager;
    private final KafkaConsumerConfig config;

    private final Logger log = LoggerFactory.getLogger(Player.class);

    @Autowired
    public Player(KafkaTemplate<Long, ResponseDto> kafkaTemplate,
                  ObjectMapper objectMapper, WfcClient wfcClient,
                  CacheAnswerRepository repository,
                  UserManager userManager,
                  KafkaConsumerConfig config) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
        this.wfcClient = wfcClient;
        this.repository = repository;
        this.userManager = userManager;
        this.config = config;


        log.info("Player started");
    }

    @KafkaListener(id = "Request",
            topics = {"lkdkp.request.low",
                "lkdkp.request.middle",
                "lkdkp.request.high",
                "lkdkp.request.critical",
                "lkdkp.request.own1",
            },
            containerFactory = "singleFactory")
    public void consume(RequestDto dto) {
        JSONObject requestJson = new JSONObject(dto.getData());

        log.info("=> consumed {}", requestJson.get("request").toString());
        ResponseDto result = new ResponseDto();


        if(requestJson.get("request").toString().equals("createUser")) {
            result = wfcClient.convert(dto);
            Gson gson = new Gson();
            UserDto user = gson.fromJson(requestJson.get("data").toString().toLowerCase(), UserDto.class);
            userManager.createUser(user, result.getGuid());
            kafkaTemplate.send("lkdkp.user", result);
        }

        if (isCached(requestJson.get("request").toString())) {
            log.info("Request \"" + requestJson.get("request").toString() + "\" get from cache db");
            List<CacheAnswer> answer = repository.findByRequest(requestJson.get("request").toString());
            result.setResult(answer.get(0).getData());
        } else {
            log.info("Request \"" + requestJson.get("request").toString() + "\" get from wsdl");
            result = wfcClient.convert(dto);
        }

        log.info("<= sending {}", result.getResult() + ", " + result.getData());
        kafkaTemplate.send("lkdkp.result", result);
    }

    //@KafkaListener(id = "Result", topics = {"lkdkp.result"}, containerFactory = "singleFactory")
    public void answer(ResponseDto dto) {
        log.info("get from queue Result");
    }

    //@Scheduled(initialDelay = 2000, fixedDelay = 2000)
    public void isCached() {
        final String uri = "http://localhost:8082/cached?request=getLocationList";
        RestTemplate synchManager = new RestTemplate();
        Boolean isCached = synchManager.getForObject(uri, Boolean.class);
        System.err.println("isCached = " + isCached);
    }

    private boolean isCached(String requestName) {
        String uri = config.getSynchManagerUri() + requestName;
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
