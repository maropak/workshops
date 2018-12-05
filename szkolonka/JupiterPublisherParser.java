package com.citi.mercury.connectivity.jupiter.config;


import com.citi.mercury.connectivity.jupiter.publish.JupiterOutboundAdapter;
import com.citi.mercury.connectivity.jupiter.spring.SpringToJupiterMessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.integration.config.ConsumerEndpointFactoryBean;
import org.w3c.dom.Element;

import java.util.HashMap;
import java.util.Map;

public class JupiterPublisherParser implements BeanDefinitionParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(JupiterPublisherParser.class);

    @Override
    public BeanDefinition parse(Element element, ParserContext parserContext) {
        Map<String, AbstractBeanDefinition> beanDefinitions = createBeanDefinitions(element, parserContext);
        for (Map.Entry<String, AbstractBeanDefinition> beanEntry : beanDefinitions.entrySet()) {
            registerBean(beanEntry.getKey(), beanEntry.getValue(), parserContext);
        }
        return null;
    }

    private Map<String, AbstractBeanDefinition> createBeanDefinitions(Element element, ParserContext parserContext) {
        Map<String, AbstractBeanDefinition> beanDefinitions = new HashMap<>();
        beanDefinitions.put(getOutboundAdapterId(element), buildOutboundAdapter(element));
        beanDefinitions.put(getSpringToJupiterMessageBridgeId(element), buildSpringToJupiterMessageBridge(element));
        beanDefinitions.put(getSpringToJupiterMessageHandlerId(element), buildSpringToJupiterMessageHandler(element));
        return beanDefinitions;
    }

    private AbstractBeanDefinition buildOutboundAdapter(Element element) {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(JupiterOutboundAdapter.class);
        builder.addConstructorArgValue(element.getAttribute("clientId"));
        builder.addConstructorArgValue(element.getAttribute("serverUrl"));
        builder.addConstructorArgValue(element.getAttribute("userName"));
        builder.addConstructorArgValue(element.getAttribute("userPassword"));

        return builder.getBeanDefinition();
    }

    private AbstractBeanDefinition buildSpringToJupiterMessageBridge(Element element) {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(ConsumerEndpointFactoryBean.class);
        builder.addPropertyReference("inputChannel", element.getAttribute("inputChannel"));
        builder.addPropertyReference("handler", getSpringToJupiterMessageHandlerId(element));
        return builder.getBeanDefinition();
    }

    private AbstractBeanDefinition buildSpringToJupiterMessageHandler(Element element) {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(SpringToJupiterMessageHandler.class);
        builder.addConstructorArgReference(getOutboundAdapterId(element));
        builder.addConstructorArgValue(element.getAttribute("clientId"));
        return builder.getBeanDefinition();
    }

    private String getOutboundAdapterId(Element element) {
        return element.getAttribute("id");
    }

    private String getSpringToJupiterMessageBridgeId(Element element) {
        return element.getAttribute("id") + ".outboundBridge";
    }

    private String getSpringToJupiterMessageHandlerId(Element element) {
        return element.getAttribute("id") + ".outboundHandler";
    }

    private void registerBean(String beanId, AbstractBeanDefinition beanDef, ParserContext parserContext) {
        LOGGER.info("Registering bean " + beanId + ", class: " + beanDef.getBeanClassName());
        BeanDefinitionHolder beanHolder = new BeanDefinitionHolder(beanDef, beanId);
        BeanDefinitionReaderUtils.registerBeanDefinition(beanHolder, parserContext.getRegistry());
    }

}
