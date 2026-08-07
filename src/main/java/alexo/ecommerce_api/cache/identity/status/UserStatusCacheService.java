package alexo.ecommerce_api.cache.identity.status;

import alexo.ecommerce_api.entity.enums.UserStatusCode;
import alexo.ecommerce_api.entity.identity.UserStatusType;
import alexo.ecommerce_api.repository.identity.UserStatusTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class UserStatusCacheService {

    private final UserStatusTypeRepository userStatusTypeRepository;

    @Cacheable("user_status_type")
    public HashMap<UserStatusCode, UserStatusType> getStatusTypes() {
        HashMap<UserStatusCode, UserStatusType> statuses = new HashMap<>();

        userStatusTypeRepository.findAll().forEach(status -> statuses.put(status.getCode(), status));

        return statuses;
    }
}
