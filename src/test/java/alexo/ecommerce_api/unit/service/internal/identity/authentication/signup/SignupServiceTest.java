package alexo.ecommerce_api.unit.service.internal.identity.authentication.signup;

import alexo.ecommerce_api.cache.identity.status.UserStatusCacheService;
import alexo.ecommerce_api.repository.customer.CustomerCartRepository;
import alexo.ecommerce_api.repository.customer.CustomerRepository;
import alexo.ecommerce_api.repository.customer.CustomerWalletRepository;
import alexo.ecommerce_api.repository.identity.user.UserRepository;
import alexo.ecommerce_api.service.internal.identity.authentication.signup.SignupService;
import alexo.ecommerce_api.service.internal.identity.authority.UserAuthorityService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SignupServiceTest {
    @Mock private UserRepository userRepository;
    @Mock private UserAuthorityService authorityService;
    @Mock private UserStatusCacheService statusCacheService;
    @Mock private CustomerRepository customerRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private CustomerCartRepository cartRepository;
    @Mock private CustomerWalletRepository walletRepository;
    @InjectMocks private SignupService service;

    @Test
    void shouldRejectDuplicateEmailBeforePersisting() {
        var request = new alexo.ecommerce_api.dto.service.internal.identity.authentication.signup.request.UserSignupRequestDTO(
                "duplicate@example.com", "First", "Last", "secret");
        when(userRepository.existsByEmail(request.email())).thenReturn(true);

        assertThatThrownBy(() -> service.createUser(request, null, null))
                .isInstanceOf(IllegalArgumentException.class);
    }
}