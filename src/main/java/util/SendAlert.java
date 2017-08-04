package util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class SendAlert implements Job {

	public void execute(JobExecutionContext context) throws JobExecutionException {

		if (!FoxAlertParameters.enviado || FoxAlertParameters.counter == FoxAlertParameters.max_counter) {
			try {
				FoxAlertParameters.enviado = false;
				FoxAlertParameters.counter = 0;
				URL url = new URL("https://api.blinktrade.com/api/v1/BRL/ticker");
				HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
				
				SSLContext sf = SSLContext.getInstance("TLS");
				
				//TrustManager[] tms = getTrustManagerFromKeyStore(new File("C:/_projetos/foxAlert/keystore.jks"), "changeit");
				TrustManager[] tms = getTrustManagerFromKeyStore(new File("/home/vsnepomu/keystore.jks"), "changeit");
				
				RuntimeReplacingTrustManager ltms = new RuntimeReplacingTrustManager((X509TrustManager) tms[0], "initial");
				sf.init(
						null,
						new TrustManager[] { ltms },
						SecureRandom.getInstance("SHA1PRNG"));
				conn.setSSLSocketFactory(sf.getSocketFactory());
				conn.setRequestMethod("GET");
				conn.setRequestProperty("Accept", "application/json");
				if (conn.getResponseCode() != 200) {
					LoggerFoxAlert.getLoggerInstance().logError("Erro no servidor fox bit!");
					return;
				}
				BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

				String output = "";
				String json = "";
				while ((output = br.readLine()) != null) {
					json += output;
				}
				conn.disconnect();
				Gson gson = new GsonBuilder().create();
				FoxInfo p = gson.fromJson(json, FoxInfo.class);
				if (p.last >= FoxAlertParameters.limiteSup || p.last <= FoxAlertParameters.limiteInf) {
					FoxAlertEmail email = new FoxAlertEmail();
					String message = "O valor da cotação atingiu " + p.last;
					email.postMail("vnepomuceno@gmail.com", "FOX ALERT!", message, "vnepomuceno@gmail.com");
					FoxAlertParameters.enviado = true;
				} 

			} catch (java.security.ProviderException e) {
				LoggerFoxAlert.getLoggerInstance().logError(e.getLocalizedMessage());
				LoggerFoxAlert.getLoggerInstance().logError(e.getCause().getLocalizedMessage());
			} catch (MalformedURLException e) {

				LoggerFoxAlert.getLoggerInstance().logError(e.getMessage());

			} catch (IOException e) {

				LoggerFoxAlert.getLoggerInstance().logError(e.getMessage());

			} catch (NoSuchAlgorithmException e) {
				LoggerFoxAlert.getLoggerInstance().logError(e.getMessage());
			} catch (KeyManagementException e) {
				LoggerFoxAlert.getLoggerInstance().logError(e.getMessage());
			} catch (KeyStoreException e) {
				LoggerFoxAlert.getLoggerInstance().logError(e.getMessage());
			} catch (CertificateException e) {
				LoggerFoxAlert.getLoggerInstance().logError(e.getMessage());
			} 
		} else {
			FoxAlertParameters.counter++;
		}
	}
	
	public TrustManager[] getTrustManagerFromKeyStore(File ks, String password) throws NoSuchAlgorithmException, KeyStoreException, CertificateException, IOException, java.security.cert.CertificateException {
		TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		KeyStore tks = KeyStore.getInstance(KeyStore.getDefaultType());
		
		BufferedInputStream fs = new BufferedInputStream(new FileInputStream(ks));
		
		tks.load(fs, password.toCharArray());
		tmf.init(tks);
		
		TrustManager[] tms = tmf.getTrustManagers();
		
		return tms;
	}
	
	private static class RuntimeReplacingTrustManager implements X509TrustManager {
		X509TrustManager m;
		String tmName;
		
		public RuntimeReplacingTrustManager(X509TrustManager m, String name) {
			this.m = m;
			tmName = name;
		}
		
		public void setTM(X509TrustManager m, String name) {
			this.m = m;
			tmName = name;
		}
		
		public void checkClientTrusted(X509Certificate[] chain, String authType)
				throws CertificateException {
			this.m.checkClientTrusted(chain, authType);
		}

		public void checkServerTrusted(X509Certificate[] chain, String authType)
				throws CertificateException {
			this.m.checkServerTrusted(chain, authType);
		}

		public X509Certificate[] getAcceptedIssuers() {
			return this.m.getAcceptedIssuers();
		}
		
	}
}
