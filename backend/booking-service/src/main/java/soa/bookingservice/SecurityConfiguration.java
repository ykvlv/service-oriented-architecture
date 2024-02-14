package soa.bookingservice;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class SecurityConfiguration {

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http // disable all inbuilt security methods
				.httpBasic(AbstractHttpConfigurer::disable)
				.csrf(AbstractHttpConfigurer::disable)
				.formLogin(AbstractHttpConfigurer::disable)
				.logout(AbstractHttpConfigurer::disable)
				.sessionManagement(httpSecuritySessionManagementConfigurer ->
						httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		http // set cors configuration
				.cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(
						request -> {
							CorsConfiguration cors = new CorsConfiguration();
							List<String> arr = new ArrayList<>();
							arr.add("*");

							cors.setAllowedMethods(
									arr
							);
							cors.setAllowedOrigins(
									arr
							);
							cors.setAllowedHeaders(
									arr
							);
							return cors;
						}
				));
		return http.build();
	}
}