package com.hoy.utilities;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;

import java.util.ArrayList;
import java.util.List;

public class ProxyUtils {
	public static final boolean HAS_TO_PROXY = false;

	public static void setProxy(DefaultHttpClient defaultHttpClient, String url) {
		HttpHost proxy = null;
		if (isHasToProxy(url)) {
			/**
			 * Para ejecutar detras de un Proxy.
			 */
			final String PROXY_IP = "<hostname>";
			final int PROXY_PORT = 80;
			defaultHttpClient.getCredentialsProvider().setCredentials(new AuthScope(PROXY_IP, PROXY_PORT), new UsernamePasswordCredentials("<user>", "<pass>"));
			proxy = new HttpHost(PROXY_IP, PROXY_PORT);
		}
		defaultHttpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
	}

	public static boolean isHasToProxy(String url) {
		String hostname = getHostname(url);
		List<String> hostsListWithProxyExclusions = getHostsListWithProxyExclusions();
		boolean hasToProxy = true;
		for (String hostWithExclusion : hostsListWithProxyExclusions) {
			if (hostname.equals(hostWithExclusion)) {
				hasToProxy = false;
			}
		}
		return hasToProxy;
	}

	public static String getHostname(String url) {
		String hostResult = "";
		if (url != null && url.length() > 0) {
			String[] doubleSplit = url.split("//");
			if (doubleSplit.length >= 2) {
				// doubleSplit[0] -> "http";				
				String address = doubleSplit[1];
				String[] simpleSplit = address.split("/");
				if (simpleSplit.length >= 2) {
					String host = simpleSplit[0];
					if (host.indexOf(":") >= 0) {
						String[] hostParts = host.split(":");
						if (hostParts.length >= 2) {
							hostResult = hostParts[0];
						}
					} else {
						hostResult = host;
					}
				}
			}
		}
		return hostResult;
	}

	public static List<String> getHostsListWithProxyExclusions() {
		List<String> hosts = new ArrayList<String>();
		hosts.add("localhost");
		hosts.add("10.0.2.2");
		return hosts;
	}
}
