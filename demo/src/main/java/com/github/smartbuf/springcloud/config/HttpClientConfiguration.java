//package com.github.smartbuf.springcloud.config;
//
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.config.RequestConfig;
//import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
//import org.apache.http.impl.client.HttpClientBuilder;
//import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.util.concurrent.TimeUnit;
//
//@Configuration
//public class HttpClientConfiguration {
//
//    @Bean
//    public HttpClient getHttpClient() {
//        System.out.println("===== Apache httpclient initializing===");
//        RequestConfig.Builder configBuilder = RequestConfig.custom();
//        configBuilder.setSocketTimeout(5 * 1000);
//        configBuilder.setConnectTimeout(5 * 1000);
//        RequestConfig defaultRequestConfig = configBuilder.build();
//
//        final PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(30, TimeUnit.SECONDS);
//        connManager.setMaxTotal(10);
//        connManager.setDefaultMaxPerRoute(1);
//
//        HttpClientBuilder builder = HttpClientBuilder.create();
//        builder.setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy());
//        builder.setConnectionManager(connManager);
//        builder.setDefaultRequestConfig(defaultRequestConfig);
//
//        return builder.build();
//    }
//
//}
