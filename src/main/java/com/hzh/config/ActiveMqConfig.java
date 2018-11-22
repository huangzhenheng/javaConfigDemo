package com.hzh.config;

import com.hzh.RootConfig;
import com.hzh.activemq.listener.MyJmsListener;
import com.hzh.activemq.listener.MyTopicListener;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jms.connection.SingleConnectionFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.listener.DefaultMessageListenerContainer;

/**
 * @Description activemq的配置
 * @auther hzh
 * @create 2018-11-22 15:14
 */
@Configuration
public class ActiveMqConfig extends RootConfig {


    //还可以配置MessageConverter


    @Autowired
    private MyJmsListener myJmsListener;
    @Autowired
    private MyTopicListener myTopicListener;

    @Bean
    public ActiveMQConnectionFactory activeMQConnectionFactory() {
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
        activeMQConnectionFactory.setBrokerURL(environment.getProperty("mq.brokerURL"));
        activeMQConnectionFactory.setTrustAllPackages(true);
        return activeMQConnectionFactory;
    }

    @Bean
    public SingleConnectionFactory connectionFactory() {
        SingleConnectionFactory singleConnectionFactory = new SingleConnectionFactory();
        singleConnectionFactory.setTargetConnectionFactory(activeMQConnectionFactory());
        return singleConnectionFactory;
    }

    @Bean
    public JmsTemplate jmsTemplate() {
        JmsTemplate jmsTemplate = new JmsTemplate();
        jmsTemplate.setDefaultDestinationName("spring-queue");
        jmsTemplate.setConnectionFactory(connectionFactory());
        return jmsTemplate;
    }

    /**
     * 配置Queue监听器
     *
     * @return
     */
    @Bean
    public DefaultMessageListenerContainer queueMessageListenerContainer() {
        DefaultMessageListenerContainer defaultMessageListenerContainer = new DefaultMessageListenerContainer();
        defaultMessageListenerContainer.setMessageListener(myJmsListener);
        defaultMessageListenerContainer.setDestinationName("spring-queue");
        defaultMessageListenerContainer.setConnectionFactory(connectionFactory());
        return defaultMessageListenerContainer;
    }



    //配置Pub/sub监听器

    @Bean
    public ActiveMQTopic topic() {
        return new ActiveMQTopic("message-topic");
    }

    @Bean
    public DefaultMessageListenerContainer topicMessageListenerContainer() {
        DefaultMessageListenerContainer defaultMessageListenerContainer = new DefaultMessageListenerContainer();
        defaultMessageListenerContainer.setMessageListener(myTopicListener);
        defaultMessageListenerContainer.setDestination(topic());
        defaultMessageListenerContainer.setConnectionFactory(connectionFactory());
        return defaultMessageListenerContainer;
    }

}
