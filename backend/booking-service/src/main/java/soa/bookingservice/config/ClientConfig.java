package soa.bookingservice.config;

import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

@Configuration
public class ClientConfig {
	@Value("${trust.store}")
	private Resource trustStore;

	@Value("${trust.store.password}")
	private String trustStorePassword;

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder
				//.setConnectTimeout(Duration.ofMillis(60000))
				//.setReadTimeout(Duration.ofMillis(60000))
				.requestFactory(() -> {
					try {
						return validateSSL();
					} catch (IOException | CertificateException | NoSuchAlgorithmException | KeyStoreException |
							 KeyManagementException e) {
						throw new RuntimeException(e);
					}
				})
				.build();
	}

	private HttpComponentsClientHttpRequestFactory validateSSL() throws IOException, CertificateException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
		SSLContext sslContext = SSLContextBuilder
				.create()
				.loadTrustMaterial(trustStore.getURL(), trustStorePassword.toCharArray())
				.build();
		SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext, new LocalHostnameVerifier());
		Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory> create()
				.register("https", csf)
				.register("http", new PlainConnectionSocketFactory())
				.build();

		HttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
		CloseableHttpClient httpClient = HttpClients.custom()
				.setConnectionManager(connectionManager)
				.evictExpiredConnections()
				.build();
		return new HttpComponentsClientHttpRequestFactory(httpClient);
	}

	private static class LocalHostnameVerifier implements HostnameVerifier {
		@Override
		public boolean verify(String hostname, SSLSession session) {
			return "first-service".equalsIgnoreCase(hostname) || "duplicate-first-service".equals(hostname)
					|| "haproxy".equalsIgnoreCase(hostname);
		}
	}

}
