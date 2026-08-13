package alexo.ecommerce_api.cache.identity.status;

import alexo.ecommerce_api.entity.identity.UserStatusType;
import alexo.ecommerce_api.repository.identity.UserStatusTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
@RequiredArgsConstructor
public class UserStatusCacheService {

    private final UserStatusTypeRepository userStatusTypeRepository;

    @Cacheable("user_status_type")
    public HashMap<UserStatusType.UserStatusCode, UserStatusType> getStatusTypes() {
        HashMap<UserStatusType.UserStatusCode, UserStatusType> statuses = new HashMap<>();

        userStatusTypeRepository.findAll().forEach(status -> statuses.put(status.getCode(), status));

        return statuses;
    }
}
