package biz.eurosib.lkdkp.client;

import biz.eurosib.lkdkp.kafka.RequestDto;
import biz.eurosib.lkdkp.kafka.ResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

public abstract class WfcClient extends WebServiceGatewaySupport {
    protected static final Logger log = LoggerFactory.getLogger(WfcClient.class);

    public abstract ResponseDto convert (RequestDto request);
}
