// package com.naonao.grab12306ticket.version.springboot.configuration;
//
// import lombok.Getter;
// import lombok.Setter;
// import org.apache.http.client.HttpClient;
// import org.apache.http.client.config.RequestConfig;
// import org.apache.http.conn.HttpClientConnectionManager;
// import org.apache.http.impl.client.HttpClientBuilder;
// import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
// import org.springframework.boot.context.properties.ConfigurationProperties;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.http.client.ClientHttpRequestFactory;
// import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
// import org.springframework.http.converter.HttpMessageConverter;
// import org.springframework.http.converter.StringHttpMessageConverter;
// import org.springframework.web.client.RestTemplate;
//
// import java.nio.charset.StandardCharsets;
// import java.util.List;
//
//
// /**
//  * @program: SpringBoot
//  * @description:
//  * @author: Wen lyuzhao
//  * @create: 2019-05-14 17:20
//  **/
//
// // @EnableConfigurationProperties
// @Configuration
// @ConfigurationProperties(prefix = "http", ignoreInvalidFields = true)
// @Getter
// @Setter
// public class HttpClientConfiguration {
//
//     private Integer maxTotal;
//
//     private Integer defaultMaxPerRoute;
//
//     private Integer connectionTimeOut;
//
//     private Integer connectionRequestTimeOut;
//
//     private Integer socketTimeOut;
//
//     @Bean
//     public HttpClientConnectionManager httpClientConnectionManager(){
//         PoolingHttpClientConnectionManager poolingHttpClientConnectionManager = new PoolingHttpClientConnectionManager();
//         poolingHttpClientConnectionManager.setMaxTotal(maxTotal);
//         poolingHttpClientConnectionManager.setDefaultMaxPerRoute(defaultMaxPerRoute);
//         return poolingHttpClientConnectionManager;
//     }
//
//     @Bean
//     public RequestConfig requestConfig(){
//         return RequestConfig
//                 .custom()
//                 .setConnectTimeout(connectionRequestTimeOut)
//                 .setSocketTimeout(socketTimeOut)
//                 .build();
//     }
//
//     @Bean
//     public HttpClient httpClient(HttpClientConnectionManager manager, RequestConfig config){
//         return HttpClientBuilder
//                 .create()
//                 .setConnectionManager(manager)
//                 .setDefaultRequestConfig(config)
//                 .build();
//     }
//
//     @Bean
//     public ClientHttpRequestFactory clientHttpRequestFactory(HttpClient httpClient){
//         return new HttpComponentsClientHttpRequestFactory(httpClient);
//     }
//
//     @Bean
//     public RestTemplate restTemplate(ClientHttpRequestFactory clientHttpRequestFactory){
//         RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory);
//         List<HttpMessageConverter<?>> httpMessageConverterList = restTemplate.getMessageConverters();
//         for (HttpMessageConverter<?> httpMessageConverter: httpMessageConverterList){
//             if (httpMessageConverter instanceof StringHttpMessageConverter){
//                 ((StringHttpMessageConverter) httpMessageConverter).setDefaultCharset(StandardCharsets.UTF_8);
//             }
//         }
//         return restTemplate;
//     }
// }
