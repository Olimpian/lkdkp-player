package biz.eurosib.lkdkp;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.IOException;

@SpringBootApplication
@EnableKafka
@EnableScheduling
//@EnableAutoConfiguration(exclude = {
//        KafkaAutoConfiguration.class
//})
@ComponentScan(basePackages = {
		"biz.eurosib.lkdkp.config",
		"biz.eurosib.lkdkp.controller",
		"biz.eurosib.lkdkp.service",
		"biz.eurosib.lkdkp.client",
		"biz.eurosib.lkdkp.cachedb"
})
//@PropertySource({
//		"classpath:kafka.properties"
//})
public class LkDkpApplication {

	public static void main(String[] args) {
		System.out.println("IT IS THE LAST OWN");
		SpringApplication.run(LkDkpApplication.class, args);
	}

	@Bean
	public JsonDeserializer jsonDeserializer() {
		return new JsonDeserializer() {
			@Override
			public Object deserialize(JsonParser p, DeserializationContext context) throws IOException {
				return null;
			}
		};
	}

	@Bean
	public ObjectMapper objectMapper() {
		return new ObjectMapper();
	}



	//@Bean
//	CommandLineRunner lookup(ServiceClient wfcClient) {
//		return args -> {
//			String values[] = "1,2,3,4".split(",");
//
//			if (args.length > 0) {
//				values[0] = args[0];
//			}
//			for (String value : values) {
//				ResponseResponse response = wfcClient.getValue(value);
////				System.err.println(response.getReturn());
//				System.err.println(response.getResponseResult());
//			}
//		};
//	}

//	@Autowired
//	private JdbcTemplate jdbcTemplate;
//
//	public void databaseConnect(String... args) throws Exception {
//		String sql = "SELECT * FROM sys.Tables";
//
//		List<Customer> customers = jdbcTemplate.query(sql,
//				BeanPropertyRowMapper.newInstance(Customer.class));
//		customers.forEach(System.out :: println);
//	}

}
