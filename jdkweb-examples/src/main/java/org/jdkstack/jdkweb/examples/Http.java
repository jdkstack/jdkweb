package org.jdkstack.jdkweb.examples;

import com.sun.net.httpserver.HttpsConfigurator;
import com.sun.net.httpserver.HttpsParameters;
import com.sun.net.httpserver.HttpsServer;
import java.io.FileInputStream;
import java.net.InetSocketAddress;
import java.security.KeyStore;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.TrustManagerFactory;
import org.jdkstack.logging.jdklog.api.spi.Log;
import org.jdkstack.logging.jdklog.core.factory.LogFactory;

public class Http {

  private static final Log LOG = LogFactory.getLog(Http.class);

  public static void main(String[] args) throws Exception {
    char[] passphrase = "mypass".toCharArray();
    KeyStore ks = KeyStore.getInstance("JKS");
    ks.load(new FileInputStream("mytest.jks"), passphrase);

    KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
    kmf.init(ks, passphrase);

    TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
    tmf.init(ks);

    SSLContext ssl = SSLContext.getInstance("TLS");
    ssl.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

    HttpsServer servers = HttpsServer.create(new InetSocketAddress(8000), 0);
    servers.createContext("/", new RestGetHandler());
    ExecutorService executor = new ThreadPoolExecutor(10,
        200, 60, TimeUnit.SECONDS,
        new LinkedBlockingQueue<>(), new ThreadPoolExecutor.AbortPolicy());
    servers.setExecutor(executor);
    servers.setHttpsConfigurator(new HttpsConfigurator(ssl) {
      public void configure(HttpsParameters params) {

        // get the remote address if needed
        InetSocketAddress remote = params.getClientAddress();

        SSLContext c = getSSLContext();

        // get the default parameters
        SSLParameters sslparams = c.getDefaultSSLParameters();
     /*   if (remote.equals(...) ){
          // modify the default set for client x
        }*/
        params.setSSLParameters(sslparams);
      }
    });
    servers.start();
    LOG.info("HTTP??????????????????:{}", 1);
  }
}
