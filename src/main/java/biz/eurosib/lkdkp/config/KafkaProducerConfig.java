package biz.eurosib.lkdkp.config;

import biz.eurosib.lkdkp.kafka.RequestDto;
import biz.eurosib.lkdkp.kafka.ResponseDto;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {
    @Value("${kafka.server}")
    private String kafkaServer;

    @Value("${kafka.producer.id}")
    private String kafkaProducerId;

    @Bean
    public Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
        props.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, false);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, kafkaProducerId);
        props.put(ProducerConfig.MAX_REQUEST_SIZE_CONFIG, 15728640);
        //props.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, 60000);
        return props;
    }

    @Bean
    public ProducerFactory<Long, RequestDto> producerRequestFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    public KafkaTemplate<Long, RequestDto> kafkaRequestTemplate() {
        KafkaTemplate<Long, RequestDto> template = new KafkaTemplate<>(producerRequestFactory());
        template.setMessageConverter(new StringJsonMessageConverter());
        return template;
    }

    @Bean
    public ProducerFactory<Long, ResponseDto> producerResultFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    public KafkaTemplate<Long, ResponseDto> kafkaResultTemplate() {
        KafkaTemplate<Long, ResponseDto> template = new KafkaTemplate<>(producerResultFactory());
        template.setMessageConverter(new StringJsonMessageConverter());
        return template;
    }
}
