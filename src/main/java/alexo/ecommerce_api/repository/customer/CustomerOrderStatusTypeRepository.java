package alexo.ecommerce_api.repository.customer;

import alexo.ecommerce_api.entity.customer.CustomerOrderStatusType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerOrderStatusTypeRepository extends JpaRepository<CustomerOrderStatusType, Short> {
}

