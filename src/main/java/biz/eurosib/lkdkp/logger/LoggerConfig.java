package biz.eurosib.lkdkp.logger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoggerConfig {
    @Bean
    public FluentdLogger createFluentdLogger() {
        return new FluentdLogger();
    }
}
