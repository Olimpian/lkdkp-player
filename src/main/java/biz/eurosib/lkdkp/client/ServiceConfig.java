package biz.eurosib.lkdkp.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

@Configuration
public class ServiceConfig {
    @Value("${wsdl.default-uri}")
    private String defaultUri;
    @Value("${wsdl.soap-action}")
    private String soapAction;
    @Value("${wsdl.generate-package}")
    private String generatePackage;

    @Bean
    public Jaxb2Marshaller marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        // this package must match the package in the <generatePackage> specified in
        // pom.xml
        marshaller.setContextPath(generatePackage);
        return marshaller;
    }

//    @Bean
//    public ServiceClient countryClient(Jaxb2Marshaller marshaller) {
//        ServiceClient client = new ServiceClient();
//
//        client.setDefaultUri(defaultUri);
//        client.setMarshaller(marshaller);
//        client.setUnmarshaller(marshaller);
//        return client;
//    }

    @Bean
    @ConditionalOnProperty(name = "wsdl.profile", havingValue = "plug")
    public WfcClient getPlugClient(Jaxb2Marshaller marshaller) {
        WfcClient client = new PlugClient(this);
//        client.setDefaultUri("http://atg-ca.selfip.biz/samples2/Service1.svc");
        client.setDefaultUri(defaultUri);
        client.setMarshaller(marshaller);
        client.setUnmarshaller(marshaller);
        return client;
    }

    @Bean
    @ConditionalOnProperty(name = "wsdl.profile", havingValue = "data-core")
    public WfcClient getDataCoreClient(Jaxb2Marshaller marshaller) {
        WfcClient client = new DataCoreClient(this);
//        client.setDefaultUri("http://tsxdatacoretest/TSDatacoreTest2/ws/DataCoreWS_response");
        client.setDefaultUri(defaultUri);
        client.setMarshaller(marshaller);
        client.setUnmarshaller(marshaller);
        return client;
    }


    public String getSoapAction() {
        return soapAction;
    }
}
