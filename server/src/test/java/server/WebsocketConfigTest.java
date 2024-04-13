package server;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

public class WebsocketConfigTest {

    @Test
    public void testRegisterStompEndpoints() {
        StompEndpointRegistry registry = Mockito.mock(StompEndpointRegistry.class);
        WebsocketConfig websocketConfig = new WebsocketConfig();
        websocketConfig.registerStompEndpoints(registry);
        Mockito.verify(registry).addEndpoint("/websocket");
    }

    @Test
    public void testConfigureMessageBroker() {
        MessageBrokerRegistry registry = Mockito.mock(MessageBrokerRegistry.class);
        WebsocketConfig websocketConfig = new WebsocketConfig();
        websocketConfig.configureMessageBroker(registry);
        Mockito.verify(registry).enableSimpleBroker("/topic");
        Mockito.verify(registry).setApplicationDestinationPrefixes("/app");
    }
}
