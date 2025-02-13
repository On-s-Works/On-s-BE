package com.ons.back.commons.config;

import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SmsConfig {

    @Value("${sms.client.key}")
    private String ACCESS_KEY;

    @Value("${sms.client.secret}")
    private String SECRET_KEY;

    @Bean
    public DefaultMessageService messageService() {
        return NurigoApp.INSTANCE.initialize(ACCESS_KEY, SECRET_KEY, "https://api.coolsms.co.kr");
    }
}
