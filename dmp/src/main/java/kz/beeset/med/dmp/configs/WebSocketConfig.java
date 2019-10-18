package kz.beeset.med.dmp.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import javax.annotation.Nullable;
import java.security.Principal;
import java.util.Map;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/dmp/patients");
        config.setApplicationDestinationPrefixes("/dmp/patients");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry){
        registry.addEndpoint("/med/med-socket-dmp")
                .setAllowedOrigins("http://localhost:4200", "http://localhost", "http://localhost:8766")
                .setHandshakeHandler(new DefaultHandshakeHandler() {
                    @Nullable
                    @Override
                    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
//                        try {
//                            System.out.println("----------------------------------------------------------");
////                            System.out.println("\n\n\n\nregisterStompEndpoints attributes = " + attributes);
////                            System.out.println("request = " + (request.getHeaders() != null ? new Gson().toJson(request.getHeaders()) : request.getHeaders()));
////                            System.out.println("request.getURI().getQuery() = " + request.getURI().getQuery());
//
////                            final String name = "123123123";
//                            final String token = request.getURI().getQuery();
//
//                            Session session = sessionRepository.findByToken(token);
//                            boolean isTokenExpired = false;
//                            if (session != null) {
//                                Calendar tokenExpirationDateCalendar = Calendar.getInstance();
//                                Date tokenExpireDate = df.parse(session.getTokenExpireDate());
//                                Calendar now = Calendar.getInstance();
//                                tokenExpirationDateCalendar.setTime(tokenExpireDate);
//                                isTokenExpired = tokenExpirationDateCalendar.before(now);
//                            }
//                            if (isTokenExpired) {
//                                System.out.println("TOKEN NOT FOUND");
//                                return null;
//                            }
//                            final String id = session.getUser().getId();
//
//                            System.out.println("determineUser id = " + id + "\n\n\n\n");
//
//
//                            return () -> id;
//                        } catch (Exception e) {
//                            System.err.println("determineUser error");
//                            e.printStackTrace();
//                            return null;
//                        }

//                    }
                        return null;
                    }
                })
                .withSockJS();
    }
}
