package com.nova.login.oauth2.auth.config;


import cn.hutool.core.lang.UUID;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.web.SecurityFilterChain;

import java.time.Duration;

/**
 * @author: wzh
 * @description: 普通-token配置类
 * @date: 2024/12/24 10:04
 */
//@Configuration
public class OpaqueTokenConfig {

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
        http.apply(new OAuth2AuthorizationServerConfigurer());
        http.csrf().disable()
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .antMatchers("/oauth2/*").permitAll()
                                .antMatchers("/introspect/*").permitAll()
                                .anyRequest().authenticated()
                )
                .formLogin();

        return http.build();
    }

    @Bean
    public RegisteredClientRepository registeredClientRepository() {
        RegisteredClient registeredClient = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId("nova")
                .clientSecret("{noop}nova")
                .clientAuthenticationMethods(authMethods -> {
                    authMethods.add(ClientAuthenticationMethod.CLIENT_SECRET_POST);
                })
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .clientSettings(ClientSettings.builder()
                        .requireAuthorizationConsent(true)
                        .requireProofKey(false)
                        .build())
                .tokenSettings(TokenSettings.builder()
                        .accessTokenFormat(OAuth2TokenFormat.REFERENCE)
                        .idTokenSignatureAlgorithm(SignatureAlgorithm.RS256)
                        .accessTokenTimeToLive(Duration.ofHours(1))
                        .reuseRefreshTokens(true)
                        .build())
                .build();

        return new InMemoryRegisteredClientRepository(registeredClient);
    }

    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder().build();
    }

}
