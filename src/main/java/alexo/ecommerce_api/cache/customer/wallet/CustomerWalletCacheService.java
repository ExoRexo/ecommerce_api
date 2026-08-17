package alexo.ecommerce_api.cache.customer.wallet;

import alexo.ecommerce_api.entity.customer.order.CustomerOrderStatusType;
import alexo.ecommerce_api.entity.customer.order.OrderItemReservationStatusType;
import alexo.ecommerce_api.entity.customer.wallet.CustomerWalletTransactionPurposeType;
import alexo.ecommerce_api.repository.customer.CustomerWalletTransactionPurposeTypeRepository;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@AllArgsConstructor
public class CustomerWalletCacheService {

    private CustomerWalletTransactionPurposeTypeRepository customerWalletTransactionPurposeTypeRepository;
    private static final String CUSTOMER_WALLET_TRANSACTION_TYPE_CACHE_KEY = "customer.wallet.CustomerWalletTransactionPurposeType";

    @Cacheable(CUSTOMER_WALLET_TRANSACTION_TYPE_CACHE_KEY)
    public Map<CustomerWalletTransactionPurposeType.CustomerWalletTransactionPurposeCode, CustomerWalletTransactionPurposeType> getCustomerWalletTransactionPurposeTypes() {
        HashMap<CustomerWalletTransactionPurposeType.CustomerWalletTransactionPurposeCode, CustomerWalletTransactionPurposeType> purposes = new HashMap<>();

        customerWalletTransactionPurposeTypeRepository.findAll().forEach(customerTransactionPurpose -> purposes.put(customerTransactionPurpose.getCode(), customerTransactionPurpose));

        return purposes;
    }
}
