package guru.qa.niffler.service;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SpecificRequestDumperFilterTest {

    @Test
    void doFilterRequestNotHttpServletRequest(@Mock ServletRequest request,
                                              @Mock ServletResponse response,
                                              @Mock FilterChain chain
            , @Mock GenericFilter decorate
    ) throws ServletException, IOException {
        String[] urlPatterns = Arrays.array("someUrl", "someUrl2");
        SpecificRequestDumperFilter specificRequestDumperFilter = new SpecificRequestDumperFilter(decorate, urlPatterns);
        specificRequestDumperFilter.doFilter(request, response, chain);
        verify(chain, times(1)).doFilter(eq(request), eq(response));
    }

    @Test
    void doFilterRequestHttpServletRequest(
            @Mock ServletResponse response,
            @Mock FilterChain chain
            , @Mock GenericFilter decorate, @Mock HttpServletRequest hRequest
    ) throws ServletException, IOException {
        String urlPattern = "someUrl";
        when(hRequest.getRequestURI())
                .thenReturn(urlPattern);

        SpecificRequestDumperFilter specificRequestDumperFilter = new SpecificRequestDumperFilter(decorate, urlPattern);
        specificRequestDumperFilter.doFilter(hRequest, response, chain);
        verify(decorate, times(1)).doFilter(hRequest, response, chain);
    }

    @Test
    void destroy(@Mock GenericFilter decorate) {
        String urlPatterns = "url";
        SpecificRequestDumperFilter specificRequestDumperFilter = new SpecificRequestDumperFilter(decorate, urlPatterns);
        specificRequestDumperFilter.destroy();
        verify(decorate, times(1)).destroy();
    }
}