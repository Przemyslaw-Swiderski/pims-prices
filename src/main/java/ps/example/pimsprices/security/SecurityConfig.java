package ps.example.pimsprices.security;

import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import ps.example.pimsprices.security.jwts.AuthEntryPointJwt;
import ps.example.pimsprices.security.jwts.AuthTokenFilter;
import ps.example.pimsprices.security.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
	@Autowired
	UserDetailsServiceImpl userDetailsService;

	@Autowired
	private AuthEntryPointJwt unauthorizedHandler;

	@Bean
	public AuthTokenFilter authenticationJwtTokenFilter() {
		return new AuthTokenFilter();
	}

	@Bean
  public DaoAuthenticationProvider authenticationProvider() {
      DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
       
      authProvider.setUserDetailsService(userDetailsService);
      authProvider.setPasswordEncoder(passwordEncoder());
   
      return authProvider;
  }

	@Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
    return authConfig.getAuthenticationManager();
  }

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
				.csrf(AbstractHttpConfigurer::disable)
				.authorizeHttpRequests(auth -> auth
						.requestMatchers(AntPathRequestMatcher.antMatcher("/api/auth/**")).permitAll()
						.requestMatchers(AntPathRequestMatcher.antMatcher("/api/v1/prices")).permitAll()
						.requestMatchers(AntPathRequestMatcher.antMatcher("/api/auth/signout")).permitAll()
						.anyRequest().authenticated()
				)
				.exceptionHandling(exceptionHandling ->
						exceptionHandling.authenticationEntryPoint(unauthorizedHandler)
				)
				.sessionManagement(sessionManagement ->
						sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				)
				.authenticationProvider(authenticationProvider())
				.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}



//	@Bean
//  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//		http
//				.csrf(AbstractHttpConfigurer::disable)
//				.authorizeHttpRequests((authorizeHttpRequests) ->
//								authorizeHttpRequests
//										.requestMatchers(
//												"/api/auth/signup",
//												"/api/auth/signin",
//												"/api/auth/refreshtoken",
//												"/error"
//										).permitAll() // czy można tutaj wyspecyfikować czy get czy post ?
////										.requestMatchers(
////												"/api/v1/auth-only-categories"
////										).hasRole("admin")
//										.requestMatchers(
//												"/api/v1/prices",
//												"/api/auth/signout"
////										).authenticated()
//										).permitAll()
//				)
//				.exceptionHandling((exceptionHandling) -> exceptionHandling.authenticationEntryPoint(unauthorizedHandler))
//				.sessionManagement((sessionManagement) ->
//						sessionManagement
//								.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//				.authenticationProvider(authenticationProvider())
//				.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
//    return http.build();
//  }
}