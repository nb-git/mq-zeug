//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package de.innogy.microserviceexample.rabbitmqsenderservice.rabbit.util;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.AMQP.BasicProperties;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Function;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.AbstractMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.amqp.rabbit.listener.adapter.AbstractAdaptableMessageListener;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.amqp.rabbit.support.DefaultMessagePropertiesConverter;
import org.springframework.amqp.rabbit.support.MessagePropertiesConverter;
import org.springframework.amqp.rabbit.support.RabbitExceptionTranslator;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class TestRabbitTemplate extends RabbitTemplate implements ApplicationContextAware, SmartInitializingSingleton {
    private static final String REPLY_QUEUE = "testRabbitTemplateReplyTo";
    private final Map<String, TestRabbitTemplate.Listeners> listeners = new HashMap();
    private ApplicationContext applicationContext;
    private String replyAddress;
    private MessagePropertiesConverter messagePropertiesConverter = new DefaultMessagePropertiesConverter();

    @Autowired
    private RabbitListenerEndpointRegistry registry;

    public TestRabbitTemplate(ConnectionFactory connectionFactory) {
        super(connectionFactory);
        this.setReplyAddress("testRabbitTemplateReplyTo");
    }

    public String getReplyAddress() {
        return replyAddress;
    }

    public void setReplyAddress(String replyAddress) {
        this.replyAddress = replyAddress;
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public void afterSingletonsInstantiated() {
        this.registry.getListenerContainers().stream().map((container) -> {
            return (AbstractMessageListenerContainer)container;
        }).forEach((c) -> {
            String[] var2 = c.getQueueNames();
            int var3 = var2.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                String queue = var2[var4];
                this.setupListener(c, queue);
            }

        });
        this.applicationContext.getBeansOfType(AbstractMessageListenerContainer.class).values().stream().forEach((container) -> {
            String[] var2 = container.getQueueNames();
            int var3 = var2.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                String queue = var2[var4];
                this.setupListener(container, queue);
            }

        });
    }

    private void setupListener(AbstractMessageListenerContainer container, String queue) {
        ((TestRabbitTemplate.Listeners)this.listeners.computeIfAbsent(queue, (v) -> {
            return new TestRabbitTemplate.Listeners();
        })).listeners.add(container.getMessageListener());
    }

    protected boolean useDirectReplyTo() {
        return false;
    }

    protected void sendToRabbit(Channel channel, String exchange, String routingKey, boolean mandatory, Message message) throws IOException {
        TestRabbitTemplate.Listeners listeners = (TestRabbitTemplate.Listeners)this.listeners.get(routingKey);
        if (listeners == null) {
            throw new IllegalArgumentException("No listener for " + routingKey);
        } else {
            try {
                this.invoke(listeners.next(), message, channel);
            } catch (Exception var8) {
                throw RabbitExceptionTranslator.convertRabbitAccessException(var8);
            }
        }
    }

    protected Message doSendAndReceiveWithFixed(String exchange, String routingKey, Message message, CorrelationData correlationData) {
        TestRabbitTemplate.Listeners listeners = (TestRabbitTemplate.Listeners)this.listeners.get(routingKey);
        if (listeners == null) {
            throw new IllegalArgumentException("No listener for " + routingKey);
        } else {
            Channel channel = (Channel)Mockito.mock(Channel.class);
            AtomicReference<Message> reply = new AtomicReference();
            Object listener = listeners.next();
            if (listener instanceof AbstractAdaptableMessageListener) {
                try {
                    AbstractAdaptableMessageListener adapter = (AbstractAdaptableMessageListener)listener;
                    ((Channel)BDDMockito.willAnswer((i) -> {
                        Envelope envelope = new Envelope(1L, false, "", "testRabbitTemplateReplyTo");
                        reply.set(MessageBuilder.withBody((byte[])i.getArgument(4)).andProperties(this.messagePropertiesConverter.toMessageProperties((BasicProperties)i.getArgument(3), envelope, "UTF-8")).build());
                        return null;
                    }).given(channel)).basicPublish(ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyBoolean(), (BasicProperties)ArgumentMatchers.any(BasicProperties.class), (byte[])ArgumentMatchers.any(byte[].class));
                    message.getMessageProperties().setReplyTo("testRabbitTemplateReplyTo");
                    adapter.onMessage(message, channel);
                } catch (Exception var10) {
                    throw RabbitExceptionTranslator.convertRabbitAccessException(var10);
                }

                return (Message)reply.get();
            } else {
                throw new IllegalStateException("sendAndReceive not supported for " + listener.getClass().getName());
            }
        }
    }

    private void invoke(Object listener, Message message, Channel channel) {
        if (listener instanceof ChannelAwareMessageListener) {
            try {
                ((ChannelAwareMessageListener)listener).onMessage(message, channel);
            } catch (Exception var5) {
                throw RabbitExceptionTranslator.convertRabbitAccessException(var5);
            }
        } else {
            if (!(listener instanceof MessageListener)) {
                throw new IllegalStateException("Listener of type " + listener.getClass().getName() + " is not supported");
            }

            ((MessageListener)listener).onMessage(message);
        }

    }

    private static class Listeners {
        private final List<Object> listeners = new ArrayList();
        private volatile Iterator<Object> iterator;

        Listeners() {
        }

        private synchronized Object next() {
            if (this.iterator == null || !this.iterator.hasNext()) {
                this.iterator = this.listeners.iterator();
            }

            return this.iterator.next();
        }
    }
}
