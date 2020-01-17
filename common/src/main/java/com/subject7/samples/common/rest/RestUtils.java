package com.subject7.samples.common.rest;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.auth.AuthSchemeProvider;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.config.Lookup;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.auth.BasicSchemeFactory;
import org.apache.http.impl.auth.DigestSchemeFactory;
import org.apache.http.impl.client.*;
import org.apache.http.util.EntityUtils;

import java.net.URI;
import java.util.Map;

public class RestUtils {
    public static String executeHttpRequest(String method, String url, Map<String, String> headers, String payload, Authentication authentication) throws Exception {
        HttpUriRequest request = createRequest(method, url, headers, payload);
        CloseableHttpClient httpClient = HttpClients.custom().setRedirectStrategy(new LaxRedirectStrategy()).build();
        HttpClientContext context = createHttpClientContext(request.getURI(), authentication);

        try (CloseableHttpResponse response = httpClient.execute(request, context)) {
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == -1 || statusCode >= 400) {
                System.out.println("[Warning!] Request failed potentially");
            }
            return extractResponse(response.getEntity());
        }
    }

    private static HttpUriRequest createRequest(String method, String url, Map<String, String> headers, String payload) throws Exception {
        if (StringUtils.isBlank(method)) {
            throw new Exception("HTTP method is not provided");
        }
        if (StringUtils.isBlank(url)) {
            throw new Exception("Connection URL is not provided");
        }
        RequestBuilder requestBuilder = RequestBuilder.create(method);
        requestBuilder.setUri(url);

        //Set headers
        if (headers != null && headers.size() > 0) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                requestBuilder.setHeader(entry.getKey(), entry.getValue());
            }
        }

        //Set payload
        if (!StringUtils.isEmpty(payload)) {
            EntityBuilder entityBuilder = EntityBuilder.create().setText(payload);
            requestBuilder.setEntity(entityBuilder.build());
        }

        return requestBuilder.build();
    }

    private static HttpClientContext createHttpClientContext(URI uri, Authentication authentication) throws Exception {
        HttpClientContext context = HttpClientContext.create();
        if (authentication == null) {
            return context;
        }

        if (authentication.getUsername() == null) {
            throw new Exception("Username is not provided");
        }
        if (authentication.getPassword() == null) {
            throw new Exception("Password is not provided");
        }
        //add authentication
        CredentialsProvider credProvider = new BasicCredentialsProvider();
        credProvider.setCredentials(
                new AuthScope(uri.getHost(), uri.getPort()),
                new UsernamePasswordCredentials(authentication.getUsername(), authentication.getPassword()));
        AuthCache authCache = new BasicAuthCache();
        authCache.put(URIUtils.extractHost(uri), new BasicScheme());
        context.setCredentialsProvider(credProvider);
        context.setAuthCache(authCache);
        Lookup<AuthSchemeProvider> authProviders;
        if (AuthenticationType.DIGEST.equals(authentication.getAuthenticationType())){
            authProviders =
                    RegistryBuilder.<AuthSchemeProvider>create().register(AuthSchemes.DIGEST, new DigestSchemeFactory()).build();
        } else {
            authProviders =
                    RegistryBuilder.<AuthSchemeProvider>create().register(AuthSchemes.BASIC, new BasicSchemeFactory()).build();
        }
        context.setAuthSchemeRegistry(authProviders);

        return context;
    }

    private static String extractResponse(HttpEntity entity) throws Exception {
        if (entity == null) {
            return null;
        }
        // WARNING: you cannot read the same CloseableHttpResponse twice
        return EntityUtils.toString(entity);
    }
}
