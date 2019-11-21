package com.github.smartbuf.springcloud.config;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author sulin
 * @since 2019-11-21 19:19:06
 */
@Configuration
public class OKHttpConfiguration {

    @Bean
    public OkHttpClient okHttpClient() {
        return new OkHttpClient.Builder()
            .retryOnConnectionFailure(true)
            .connectionPool(new ConnectionPool())
            .build();
    }

}
