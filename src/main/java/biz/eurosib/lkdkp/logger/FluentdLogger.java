package biz.eurosib.lkdkp.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FluentdLogger {
    private final Logger log = LoggerFactory.getLogger(FluentdLogger.class);
    //todo make abstract structure for log message to mongo

    public void trace(String message) {
        log.trace(message);
    }

    public void debug(String message) {
        log.debug(message);
    }

    public void info(String message) {
        log.info(message);
    }
    public void info(String message, Object object) {
        log.info(message, object);
    }

    public void warn(String message) {
        log.warn(message);
    }

    public void error(String message) {
        log.error(message);
    }
    public void error(String message, Object object) {
        log.error(message, object);
    }
}
