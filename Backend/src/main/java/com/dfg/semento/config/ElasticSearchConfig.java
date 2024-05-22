package com.dfg.semento.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories
public class ElasticSearchConfig extends ElasticsearchConfiguration {

    @Value("${elasticsearch.host}")
    private String host;
    @Value("${elasticsearch.url}")
    private String url;
    @Value("${elasticsearch.port}")
    private int port;

    @Override
    public ClientConfiguration clientConfiguration() {
        return ClientConfiguration.builder()
                .connectedTo(host)
                .build();
    }

    @Bean(destroyMethod = "close")
    public RestHighLevelClient client(){
        RestClientBuilder builder = RestClient.builder(
                new HttpHost(url, port, "http")
        );

        RestHighLevelClient client = new RestHighLevelClient(builder);
        return client;
    }
}
