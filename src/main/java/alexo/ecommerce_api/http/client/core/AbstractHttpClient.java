package alexo.ecommerce_api.http.client.core;

import org.springframework.web.client.RestClient;

abstract public class AbstractHttpClient {
    private final RestClient restClient;

    protected AbstractHttpClient(RestClient restClient) {
        this.restClient = restClient;
    }

    protected RestClient.RequestHeadersUriSpec<?> get() {
        return restClient.get();
    }

    protected RestClient.RequestHeadersUriSpec<?> head() {
        return restClient.head();
    }

    protected RestClient.RequestBodyUriSpec post() {
        return restClient.post();
    }

    protected RestClient.RequestBodyUriSpec put() {
        return restClient.put();
    }

    protected RestClient.RequestBodyUriSpec patch() {
        return restClient.patch();
    }

    protected RestClient.RequestHeadersUriSpec<?> delete() {
        return restClient.delete();
    }

    protected RestClient.RequestHeadersUriSpec<?> options() {
        return restClient.options();
    }
}
