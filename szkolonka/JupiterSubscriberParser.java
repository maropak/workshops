package com.citi.mercury.connectivity.jupiter.config;


import com.citi.mercury.connectivity.jupiter.esper.EsperJupiterConfig;
import com.citi.mercury.connectivity.jupiter.esper.EsperJupiterHandler;
import com.citi.mercury.connectivity.jupiter.esper.EsperServiceActivator;
import com.citi.mercury.connectivity.jupiter.spring.JupiterDeletedKeysToSpringMessageBuilder;
import com.citi.mercury.connectivity.jupiter.spring.JupiterToSpringMessageBridge;
import com.citi.mercury.connectivity.jupiter.subscribe.JupiterInboundAdapter;
import com.citi.mercury.connectivity.jupiter.subscribe.JupiterSubscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

import java.util.*;

public class JupiterSubscriberParser implements BeanDefinitionParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(JupiterSubscriberParser.class);

    private EsperJupiterConfig esperJupiterConfig;

    @Override
    public BeanDefinition parse(Element element, ParserContext parserContext) {
        initializeEsperJupiterConfig();

        Map<String, AbstractBeanDefinition> beanDefinitions = createBeanDefinitions(element);
        for (Map.Entry<String, AbstractBeanDefinition> beanEntry : beanDefinitions.entrySet()) {
            registerBean(beanEntry.getKey(), beanEntry.getValue(), parserContext);
        }
        return null;
    }

    private void initializeEsperJupiterConfig() {
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath*:**/jupiterEsperConfig*.xml");
        esperJupiterConfig = (EsperJupiterConfig) context.getBean("esperJupiterConfig");
    }

    private Map<String, AbstractBeanDefinition> createBeanDefinitions(Element element) {
        Map<String, AbstractBeanDefinition> beanDefinitions = new HashMap<>();
        beanDefinitions.put(element.getAttribute("id"), buildInboundAdapter(element, beanDefinitions));
        return beanDefinitions;
    }

    private AbstractBeanDefinition buildInboundAdapter(Element element, Map<String, AbstractBeanDefinition> beanDefinitions) {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(JupiterInboundAdapter.class);
        builder.addConstructorArgValue(element.getAttribute("clientId"));
        builder.addConstructorArgValue(element.getAttribute("serverUrl"));
        builder.addConstructorArgValue(element.getAttribute("userName"));
        builder.addConstructorArgValue(element.getAttribute("userPassword"));


        builder.addPropertyValue("jupiterSubscriptions", buildSubscriptionList(element, beanDefinitions));

        return builder.getBeanDefinition();
    }

    private ManagedList<BeanDefinition> buildSubscriptionList(Element element, Map<String, AbstractBeanDefinition> beanDefinitions) {
        ManagedList<BeanDefinition> jupiterSubscriptions = new ManagedList<>();

        // add subscriptions
        List<Element> subscriptionList = DomUtils.getChildElementsByTagName(element, "subscription");
        for (Element subscriptionElement : subscriptionList) {
            jupiterSubscriptions.add(buildJupiterSubscription(subscriptionElement));
        }

        // add subscriptions from join-subscription
        buildJoinSubscriptionList(element, jupiterSubscriptions, beanDefinitions);

        return jupiterSubscriptions;
    }

    private void buildJoinSubscriptionList(Element element, ManagedList<BeanDefinition> jupiterSubscriptions, Map<String, AbstractBeanDefinition> beanDefinitions) {
        List<Element> joinSubscriptionList = DomUtils.getChildElementsByTagName(element, "join-subscription");
        for (Element joinSubscriptionElement : joinSubscriptionList) {
            String esperChannel = joinSubscriptionElement.getAttribute("esperChannel");
            String redisKeyPatterns = joinSubscriptionElement.getAttribute("redisKeyPatterns");
            List<String> keys = new ArrayList<>();
            for (String redisKeyPattern : redisKeyPatterns.split(",")) {
                jupiterSubscriptions.add(buildJupiterSubscription(redisKeyPattern.trim() + ":*", "updated(*)", redisKeyPattern.trim() + ":.*", "false", esperChannel, null));
                keys.add(redisKeyPattern.trim());
            }

            String joinFields = joinSubscriptionElement.getAttribute("joinFields");
            validateJoinSubscription(joinSubscriptionElement, keys, joinFields);

            String handlerId = joinSubscriptionElement.getAttribute("id") + "Handler";
            addEsperJupiterHandler(beanDefinitions, joinSubscriptionElement, keys, joinFields, handlerId);
            addEsperServiceActivator(beanDefinitions, joinSubscriptionElement, handlerId);
        }
    }

    private void validateJoinSubscription(Element joinSubscriptionElement, List<String> keys, String joinFields) {
        if (keys.size() < 2) {
            throw new IllegalStateException("RedisKeyPatterns should have at least 2 values");
        }

        if (keys.size() - 1 != joinFields.split(",").length) {
            throw new IllegalStateException("Join fields amount should be 1 lower than RedisKeyPatterns size");
        }
    }

    private void addEsperServiceActivator(Map<String, AbstractBeanDefinition> beanDefinitions, Element joinSubscriptionElement, String handlerId) {
        BeanDefinitionBuilder esperServiceActivatorBuilder = BeanDefinitionBuilder.genericBeanDefinition(EsperServiceActivator.class);
        esperServiceActivatorBuilder.addConstructorArgReference(handlerId);
        esperServiceActivatorBuilder.addConstructorArgReference(joinSubscriptionElement.getAttribute("esperChannel"));
        beanDefinitions.put(joinSubscriptionElement.getAttribute("id"), esperServiceActivatorBuilder.getBeanDefinition());
    }

    private void addEsperJupiterHandler(Map<String, AbstractBeanDefinition> beanDefinitions, Element joinSubscriptionElement, List<String> keys, String joinFields, String handlerId) {
        BeanDefinitionBuilder esperJupiterHandlerBuilder = BeanDefinitionBuilder.genericBeanDefinition(EsperJupiterHandler.class);
        esperJupiterHandlerBuilder.addConstructorArgValue(keys);
        esperJupiterHandlerBuilder.addConstructorArgValue(joinFields);
        esperJupiterHandlerBuilder.addConstructorArgValue(esperJupiterConfig);
        esperJupiterHandlerBuilder.addConstructorArgReference(joinSubscriptionElement.getAttribute("channel"));

        beanDefinitions.put(handlerId, esperJupiterHandlerBuilder.getBeanDefinition());
    }

    private AbstractBeanDefinition buildJupiterSubscription(Element element) {
        return buildJupiterSubscription(element.getAttribute("redisKeyPattern"), element.getAttribute("queryBody"), element.getAttribute("regexKeyPattern"),
                element.getAttribute("with-metadata"), element.getAttribute("channel"), element.getAttribute("deletedKeysChannel"));
    }

    private AbstractBeanDefinition buildJupiterSubscription(String redisKeyPattern, String queryBody, String regexKeyPattern, String withMetadata, String channel, String deletedKeysChannel) {
        BeanDefinitionBuilder subscriptionBuilder = BeanDefinitionBuilder.genericBeanDefinition(JupiterSubscription.class);
        subscriptionBuilder.addConstructorArgValue(redisKeyPattern);
        subscriptionBuilder.addConstructorArgValue(queryBody);
        subscriptionBuilder.addConstructorArgValue(regexKeyPattern);

        // build a callback with JupiterToSpringMessageBridge
        addJupiterToSpringMessageBridge(withMetadata, channel, subscriptionBuilder);

        // build a callback for deleted keys with JupiterDeletedKeysToSpringMessageBuilder
        addJupiterDeletedKeysToSpringMessageBuilder(channel, deletedKeysChannel, subscriptionBuilder);

        return subscriptionBuilder.getBeanDefinition();
    }

    private void addJupiterToSpringMessageBridge(String withMetadata, String channel, BeanDefinitionBuilder subscriptionBuilder) {
        boolean useMetadata = "true".equals(withMetadata);
        BeanDefinitionBuilder messageBridgeBuilder = BeanDefinitionBuilder.genericBeanDefinition(JupiterToSpringMessageBridge.class);
        messageBridgeBuilder.addPropertyReference("outputChannel", channel);
        messageBridgeBuilder.addPropertyValue("withMetadata", useMetadata);
        subscriptionBuilder.addConstructorArgValue(messageBridgeBuilder.getBeanDefinition());
    }

    private void addJupiterDeletedKeysToSpringMessageBuilder(String channel, String deletedKeysChannel, BeanDefinitionBuilder subscriptionBuilder) {
        BeanDefinitionBuilder deletedKeysMessageBridgeBuilder = BeanDefinitionBuilder.genericBeanDefinition(JupiterDeletedKeysToSpringMessageBuilder.class);
        deletedKeysMessageBridgeBuilder.addPropertyReference("outputChannel", channel); // set output channel because required

        if (deletedKeysChannel != null && !deletedKeysChannel.isEmpty()) {
            // override outputChannel and enable sending functionality
            deletedKeysMessageBridgeBuilder.addPropertyReference("deletedKeysOutputChannel", deletedKeysChannel);
        }
        subscriptionBuilder.addConstructorArgValue(deletedKeysMessageBridgeBuilder.getBeanDefinition());
    }

    private void registerBean(String beanId, AbstractBeanDefinition beanDef, ParserContext parserContext) {
        LOGGER.info("Registering bean " + beanId + ", class: " + beanDef.getBeanClassName());
        BeanDefinitionHolder beanHolder = new BeanDefinitionHolder(beanDef, beanId);
        BeanDefinitionReaderUtils.registerBeanDefinition(beanHolder, parserContext.getRegistry());
    }
}
