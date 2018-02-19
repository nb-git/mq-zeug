package de.innogy.microserviceexample.rabbitmqsenderservice.rabbit;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class RabbitSenderServiceTest {

    @Mock
    private RabbitTemplate rabbitTemplateMock;

    private RabbitSenderService testSubject;

    @Before public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSendingMessage(){
        // given
        doNothing().when(rabbitTemplateMock).convertAndSend(anyString(), anyString(), anyString());
        testSubject = new RabbitSenderService(rabbitTemplateMock);

        // when
        testSubject.sendGreetingMessage();

        // then
        verify(rabbitTemplateMock, times(1)).convertAndSend(anyString(), anyString(), anyString());
    }
}