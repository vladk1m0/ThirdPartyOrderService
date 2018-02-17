package ru.gosuslugi.im.ws.conf;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.server.EndpointInterceptor;
import org.springframework.ws.server.EndpointMapping;
import org.springframework.ws.server.MessageDispatcher;
import org.springframework.ws.server.endpoint.adapter.DefaultMethodEndpointAdapter;
import org.springframework.ws.server.endpoint.interceptor.PayloadLoggingInterceptor;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;
import org.springframework.ws.soap.server.SoapMessageDispatcher;
import org.springframework.ws.soap.server.endpoint.SoapFaultAnnotationExceptionResolver;
import org.springframework.ws.soap.server.endpoint.SoapFaultDefinition;
import org.springframework.ws.soap.server.endpoint.mapping.SoapActionAnnotationMethodEndpointMapping;
import org.springframework.ws.transport.http.HttpUrlConnectionMessageSender;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.transport.http.WebServiceMessageReceiverHttpHandler;
import org.springframework.ws.transport.http.WsdlDefinitionHttpHandler;
import org.springframework.ws.wsdl.wsdl11.SimpleWsdl11Definition;
import org.springframework.ws.wsdl.wsdl11.Wsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;
import ru.gosuslugi.im.ws.service.TPOSFaultException;
import ru.gosuslugi.im.ws.service.TPOSSignatureInterceptor;

import java.util.Collections;
import java.util.Properties;

@EnableWs
@Configuration
public class WebServiceConfig extends WsConfigurerAdapter {

    @Bean
    public ServletRegistrationBean messageDispatcherServlet(ApplicationContext applicationContext) {
        MessageDispatcherServlet servlet = new MessageDispatcherServlet();
        servlet.setApplicationContext(applicationContext);

        return new ServletRegistrationBean(servlet, "/ws/*");
    }

    @Bean
    public WebServiceMessageReceiverHttpHandler soapHandler() {
        WebServiceMessageReceiverHttpHandler soapHandler = new WebServiceMessageReceiverHttpHandler();
        soapHandler.setMessageFactory(messageFactory());
        soapHandler.setMessageReceiver(messageReceiver());
        return soapHandler;
    }

    @Bean
    public SaajSoapMessageFactory messageFactory() {
        return new SaajSoapMessageFactory();
    }

    @Bean
    public MessageDispatcher messageReceiver() {
        SoapMessageDispatcher messageReceiver = new SoapMessageDispatcher();
        messageReceiver.setEndpointAdapters(Collections.singletonList(endpointAdapter()));
        messageReceiver.setEndpointMappings(Collections.singletonList(endpointMapping()));
        messageReceiver.setEndpointExceptionResolvers(Collections.singletonList(exceptionResolver()));
        return messageReceiver;
    }

    @Bean
    public DefaultMethodEndpointAdapter endpointAdapter() {
        return new DefaultMethodEndpointAdapter();
    }

    @Bean
    public EndpointMapping endpointMapping() {
        final SoapActionAnnotationMethodEndpointMapping endpointMapping = new SoapActionAnnotationMethodEndpointMapping();
        endpointMapping.setInterceptors(new EndpointInterceptor[]{wssInterceptor(), payloadLoggingInterceptor()});
        return endpointMapping;
    }

    @Bean
    public HttpUrlConnectionMessageSender messageSender() {
        return new HttpUrlConnectionMessageSender();
    }

    @Bean
    public WsdlDefinitionHttpHandler wsdlHandler() {
        return new WsdlDefinitionHttpHandler(wsdlDefinition());
    }

    @Bean(name = "ThirdPartyOrderService")
    public Wsdl11Definition wsdlDefinition() {
        return new SimpleWsdl11Definition(new ClassPathResource("wsdl/ThirdPartyOrderService.wsdl"));
    }

    @Bean(name = "include")
    public XsdSchema includeSchema() {
        return new SimpleXsdSchema(new ClassPathResource("wsdl/types/include.xsd"));
    }

    @Bean(name = "smev.gosuslugi.ru.rev120315")
    public XsdSchema rev120315Schema() {
        return new SimpleXsdSchema(new ClassPathResource("wsdl/types/smev.gosuslugi.ru.rev120315.xsd"));
    }

    @Bean(name = "ThirdPartyOrderServiceTypes")
    public XsdSchema thirdPartyOrderServiceTypesSchema() {
        return new SimpleXsdSchema(new ClassPathResource("wsdl/types/ThirdPartyOrderServiceTypes.xsd"));
    }

    @Bean
    public Jaxb2Marshaller jaxb2Marshaller() {
        Jaxb2Marshaller jaxb2Marshaller = new Jaxb2Marshaller();
        jaxb2Marshaller.setContextPaths(
                "ru.gosuslugi.im.types.thirdpartyorderservicetypes",
                "ru.gosuslugi.smev.rev120315",
                "org.w3._2004._08.xop.include"
        );

        return jaxb2Marshaller;
    }

    @Bean
    public PayloadLoggingInterceptor payloadLoggingInterceptor() {
        return new PayloadLoggingInterceptor();
    }

    @Bean
    public TPOSSignatureInterceptor wssInterceptor() {
        return new TPOSSignatureInterceptor();
    }

    @Bean
    public SoapFaultAnnotationExceptionResolver exceptionResolver() {
        final SoapFaultAnnotationExceptionResolver exceptionResolver = new SoapFaultAnnotationExceptionResolver();
        SoapFaultDefinition faultDefinition = new SoapFaultDefinition();
        faultDefinition.setFaultCode(SoapFaultDefinition.SERVER);
        exceptionResolver.setDefaultFault(faultDefinition);

        Properties errorMappings = new Properties();
        errorMappings.setProperty(Exception.class.getName(), SoapFaultDefinition.SERVER.toString());
        errorMappings.setProperty(TPOSFaultException.class.getName(), SoapFaultDefinition.SERVER.toString());
        exceptionResolver.setOrder(1);

        return exceptionResolver;
    }
}
