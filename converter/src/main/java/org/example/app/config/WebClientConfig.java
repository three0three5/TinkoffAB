package org.example.app.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.security.oauth2.client.AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.InMemoryReactiveOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.InMemoryReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

import static org.example.app.utils.Constants.REGISTRATION_ID;

@Configuration
@RequiredArgsConstructor
public class WebClientConfig {
    private final RatesClientProperties properties;

    @Bean
    public WebClient ratesWebClient(
            WebClient.Builder builder,
            ReactiveClientRegistrationRepository clientRegistrations,
            ReactiveOAuth2AuthorizedClientService authorizedClients) {
        var oauth = new ServerOAuth2AuthorizedClientExchangeFilterFunction(
                new AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager(
                        clientRegistrations, authorizedClients
                )
        );
        oauth.setDefaultClientRegistrationId(REGISTRATION_ID);

        return builder
                .baseUrl(properties.getRatesUrl())
                .filter(oauth)
                .clientConnector(new ReactorClientHttpConnector(httpClient()))
                .build();
    }

    @Bean
    public HttpClient httpClient() {
        return HttpClient.create()
                .responseTimeout(Duration.ofSeconds(10));
    }
}
