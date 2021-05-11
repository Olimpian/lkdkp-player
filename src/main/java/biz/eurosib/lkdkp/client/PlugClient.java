package biz.eurosib.lkdkp.client;


import biz.eurosib.lkdkp.client.wsdl.plug.ObjectFactory;
import biz.eurosib.lkdkp.client.wsdl.plug.Response;
import biz.eurosib.lkdkp.client.wsdl.plug.ResponseResponse;
import biz.eurosib.lkdkp.kafka.RequestDto;
import biz.eurosib.lkdkp.kafka.ResponseDto;
import org.springframework.ws.soap.client.core.SoapActionCallback;

import java.util.UUID;

public class PlugClient extends WfcClient {
    private ServiceConfig config;

    public PlugClient(ServiceConfig config) {
        this.config = config;
    }

    public ResponseResponse getValue(String value) {
        log.info("client test");

        Response request = new Response();

        ObjectFactory factory = new ObjectFactory();
        request.setJson(factory.createResponseJson(value));
        request.setTaskId(factory.createResponseTaskId(UUID.randomUUID().toString()));

        log.info("Requesting location for " + value);

        ResponseResponse response = (ResponseResponse) getWebServiceTemplate()
                .marshalSendAndReceive(request,
                        new SoapActionCallback(config.getSoapAction()));

        return response;
    }

    public ResponseDto convert (RequestDto request) {
        ResponseResponse response = this.getValue(request.getData());
        System.err.println(response.getResponseResult().getValue());

        return new ResponseDto(response.getResponseResult().getValue());
    }
}
