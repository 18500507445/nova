package com.nova.login.oauth2.auth.config;

import cn.hutool.core.lang.UUID;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.web.SecurityFilterChain;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Duration;

/**
 * @author: wzh
 * @description: jwt-token配置类
 * @date: 2024/12/24 10:30
 */
@Configuration
public class JwtTokenConfig {

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {

        http.apply(new OAuth2AuthorizationServerConfigurer());
        http.csrf().disable()
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .antMatchers("/oauth2/*").permitAll()
                                .antMatchers("/introspect/*").permitAll()
                                .antMatchers("/issuer/*").permitAll()
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
                // 添加2个是告诉你，可以添加多个
                .scope("hello")
                .scope("nova")
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                // 必须要配置一个重定向地址
                .redirectUri("xxxxxxx")
                .clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).requireProofKey(false).build())
                .tokenSettings(TokenSettings.builder().accessTokenTimeToLive(Duration.ofHours(1)).build())
                .build();

        return new InMemoryRegisteredClientRepository(registeredClient);
    }

    @Bean
    public JWKSource<SecurityContext> jwkSource() {
        KeyPair keyPair = generateRsaKey();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        RSAKey rsaKey = new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID(UUID.randomUUID().toString())
                .build();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return new ImmutableJWKSet<>(jwkSet);
    }

    private static KeyPair generateRsaKey() {
        KeyPair keyPair;
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            keyPair = keyPairGenerator.generateKeyPair();
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
        return keyPair;
    }

    @Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }

    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder().build();
    }

    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> oAuth2TokenCustomizer() {
        return context -> {
            JwtClaimsSet.Builder claims = context.getClaims();
            // 将权限信息放入jwt的claims中，这里可以注入一个 Mapper去查询数据库 （可以从 context中拿到 client_id）
            claims.claim("companyId", "自定义参数");
        };
    }
}

