package alexo.ecommerce_api.repository.customer;

import alexo.ecommerce_api.entity.customer.CustomerOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerOrderRepository extends JpaRepository<CustomerOrder, Long> {
}

