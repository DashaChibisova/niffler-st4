package guru.qa.niffler.service;

import guru.qa.niffler.data.UserEntity;
import guru.qa.niffler.data.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NifflerUserDetailsServiceTest {

  private NifflerUserDetailsService nifflerUserDetailsService;
  private UserEntity testUserEntity;

  @BeforeEach
  void initMockRepository() {
    UserRepository userRepository = new UserRepository.FakeUserRepository();
    testUserEntity = userRepository.findByUsername("correct");
    nifflerUserDetailsService = new NifflerUserDetailsService(userRepository);
  }

  @Test
  void loadUserByUsername() {
    final UserDetails correct = nifflerUserDetailsService.loadUserByUsername("correct");

    final List<SimpleGrantedAuthority> expectedAuthorities = testUserEntity.getAuthorities().stream()
        .map(a -> new SimpleGrantedAuthority(a.getAuthority().name()))
        .toList();

    assertEquals(
        "correct",
        correct.getUsername()
    );
    assertEquals(
        "test-pass",
        correct.getPassword()
    );
    assertEquals(
        expectedAuthorities,
        correct.getAuthorities()
    );

    assertTrue(correct.isAccountNonExpired());
    assertTrue(correct.isAccountNonLocked());
    assertTrue(correct.isCredentialsNonExpired());
    assertTrue(correct.isEnabled());
  }

  @Test
  void loadUserByUsernameNegayive() {
    final UsernameNotFoundException exception = assertThrows(
        UsernameNotFoundException.class,
        () -> nifflerUserDetailsService.loadUserByUsername("incorrect")
    );

    assertEquals(
        "Username: incorrect not found",
        exception.getMessage()
    );
  }
}