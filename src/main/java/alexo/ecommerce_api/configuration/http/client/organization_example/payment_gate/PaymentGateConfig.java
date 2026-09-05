package alexo.ecommerce_api.configuration.http.client.organization_example.payment_gate;

import alexo.ecommerce_api.http.client.organization_example.payment_gate.PaymentGateHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class PaymentGateConfig {

    @Bean
    public PaymentGateHttpClient paymentGateHttpClient(RestClient.Builder builder) {
        return new PaymentGateHttpClient(builder
                .baseUrl("https://example.com")
                .defaultHeader("Authorization", "Bearer token-token")
                .defaultHeader("Content-Type", "application/json")
                .build()
        );
    }

}
