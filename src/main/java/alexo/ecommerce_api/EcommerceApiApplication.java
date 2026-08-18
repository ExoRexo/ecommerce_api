package alexo.ecommerce_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class EcommerceApiApplication {

	static void main(String[] args) {
		SpringApplication.run(EcommerceApiApplication.class, args);
	}

}
