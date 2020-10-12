package com.insider.hackernews.service;

import java.util.concurrent.TimeUnit;

import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import reactor.netty.tcp.TcpClient;

/**
 * A generic class providing rest services
 * @author Mithun
 *
 */
@Service
public class RestService {
	
	private final WebClient webClient;
	private static final String HACKER_NEWS_BASE_URL = "https://hacker-news.firebaseio.com/v0/";

	public RestService() {
		TcpClient tcpClient = TcpClient.create().option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 30000)
				.doOnConnected(connection -> {
					connection.addHandlerLast(new ReadTimeoutHandler(30000, TimeUnit.MILLISECONDS));
					connection.addHandlerLast(new WriteTimeoutHandler(30000, TimeUnit.MILLISECONDS));
				});
		this.webClient = WebClient.builder()
				.baseUrl(HACKER_NEWS_BASE_URL)
				.clientConnector(new ReactorClientHttpConnector(reactor.netty.http.client.HttpClient.from(tcpClient)))
				.build();
	}

	public WebClient getWebClient() {
		return webClient;
	}
}