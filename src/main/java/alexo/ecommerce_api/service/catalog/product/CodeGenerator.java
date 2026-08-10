package alexo.ecommerce_api.service.catalog.product;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public final class CodeGenerator {

    private static final String ALPHABET =
            "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private static final int RANDOM_LENGTH = 12;

    private static final SecureRandom RANDOM = new SecureRandom();

    /**
     * generates unique code for product
     * @return not null string of 20 characters length
     */
    public String generateCode() {
        StringBuilder result = new StringBuilder("PRD-");

        for (int i = 0; i < RANDOM_LENGTH; i++) {
            result.append(ALPHABET.charAt(
                    RANDOM.nextInt(ALPHABET.length())
            ));
        }

        return result.toString();
    }
}