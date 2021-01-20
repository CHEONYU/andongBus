package co.kr.smartplusteam.luna.study.config;

import java.net.InetAddress;

import org.apache.http.HttpHost;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;

/**
 * ElasticSearch Config
 * @author Leo
 *
 */
@EnableElasticsearchRepositories
@Configuration
@Slf4j
public class ElasticSearchConfig implements AutoCloseable {

	@Value("${spring.elasticsearch.clusterName}")
	private String clusterName;

	@Value("${spring.elasticsearch.host}")
	private String host;

	@Value("${spring.elasticsearch.port}")
	private int port;

	@Value("${spring.elasticsearch.resthost}")
	private String resthost;

	@Value("${spring.elasticsearch.restport}")
	private int restport;

	RestClient restClient;

	TransportClient client;

	@PostConstruct
	void init() {
		System.setProperty("es.set.netty.runtime.available.processors", "false");
	}

	@Bean(name = "lunaElasticsearchClient")
	public Client client() throws Exception {
		Settings settings = Settings.builder().put("cluster.name", clusterName).build();

		client = new PreBuiltTransportClient(settings);
		client.addTransportAddress(new TransportAddress(InetAddress.getByName(host), port));
		return client;
	}

	@Bean(name = "elasticsearchTemplate")
	public ElasticsearchOperations elasticsearchTemplate() throws Exception {
		log.info("elasticsearchTemplate");
		return new ElasticsearchTemplate(client());
	}

	@Bean(name = "lunaElasticsearchRestClient")
	public RestClient restClient() {
		restClient = RestClient.builder(new HttpHost(resthost, restport)).build();
		log.info("restClient");
		return restClient;
	}

	@Override
	public void close() throws Exception {
//		log.info("elasticClose()");
		client.close();
		restClient.close();
	}

}
