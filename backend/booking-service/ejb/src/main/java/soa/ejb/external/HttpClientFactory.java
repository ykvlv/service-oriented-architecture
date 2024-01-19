package soa.ejb.external;

import soa.ejb.exception.ClientException;

import javax.net.ssl.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

public class HttpClientFactory {

    public static Client getJerseyHTTPSClient() throws ClientException {
        SSLContext sslContext = null;
        try {
            sslContext = getSslContext();
        } catch (NoSuchAlgorithmException | KeyManagementException | IOException | KeyStoreException |
                 CertificateException e) {
            throw new ClientException(e.getMessage());
        }
        HostnameVerifier allHostsValid = new LocalHostnameVerifier();

        return ClientBuilder.newBuilder()
                .sslContext(sslContext)
                .hostnameVerifier(allHostsValid)
                .build();
    }

    private static SSLContext getSslContext() throws KeyStoreException, CertificateException, IOException, NoSuchAlgorithmException, KeyManagementException {

        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream is = classloader.getResourceAsStream("keystore/orgdirectory.jks");

        KeyStore myTrustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        myTrustStore.load(is, "orgdirectory".toCharArray());

        TrustManagerFactory tmf = TrustManagerFactory
                    .getInstance(TrustManagerFactory.getDefaultAlgorithm());

        tmf.init(myTrustStore);

        X509TrustManager myTm = null;
        for (TrustManager tm : tmf.getTrustManagers()) {
            if (tm instanceof X509TrustManager) {
                myTm = (X509TrustManager) tm;
                break;
            }
        }

        SSLContext sslContext = SSLContext.getInstance("SSL");
        sslContext.init(null, new TrustManager[] { myTm }, null);

        return sslContext;
    }

    private static class LocalHostnameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return "ticket-service".equalsIgnoreCase(hostname) || "duplicate-ticket-service".equalsIgnoreCase(hostname)
                    || "haproxy".equalsIgnoreCase(hostname);
        }
    }

}
