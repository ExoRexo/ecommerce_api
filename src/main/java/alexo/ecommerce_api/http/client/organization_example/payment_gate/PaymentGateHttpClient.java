package alexo.ecommerce_api.http.client.organization_example.payment_gate;

import alexo.ecommerce_api.http.client.core.AbstractHttpClient;
import org.springframework.web.client.RestClient;

public class PaymentGateHttpClient extends AbstractHttpClient {
    public PaymentGateHttpClient(RestClient restClient) {
        super(restClient);
    }
}
