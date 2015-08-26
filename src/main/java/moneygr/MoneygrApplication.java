package moneygr;

import moneygr.domain.repository.user.UserRepository;
import moneygr.thrift.TUserService;

import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TJSONProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.server.TServlet;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

@SpringBootApplication
public class MoneygrApplication {

	@Bean
	public CommandLineRunner runner(UserRepository userRepository,
			PlatformTransactionManager transactionManager) {
		return (args) -> {
			new TransactionTemplate(transactionManager).execute(tx -> {
				userRepository.findAll().forEach(x -> {
					System.out.println(x);
				});
				return null;
			});
		};
	}
	@Bean FilterRegistrationBean crosFilter() {
		FilterRegistrationBean registrationBean = new FilterRegistrationBean();
		registrationBean.setUrlPatterns(Collections.singleton("/*"));
		registrationBean.setFilter(new Filter() {
			@Override
			public void init(FilterConfig filterConfig) throws ServletException {
				// NO-OP
			}

			@Override
			public void doFilter(ServletRequest servletRequest,
					ServletResponse servletResponse, FilterChain filterChain)
					throws IOException, ServletException {
				HttpServletResponse response = (HttpServletResponse) servletResponse;
				response.setHeader("Access-Control-Allow-Origin", "*");
				response.setHeader("Access-Control-Allow-Methods",
						"POST, GET, OPTIONS, PUT, DELETE");
				response.setHeader("Access-Control-Max-Age", "3600");
				response.setHeader("Access-Control-Allow-Headers", "*");
				filterChain.doFilter(servletRequest, servletResponse);
			}

			@Override
			public void destroy() {
				// NO-OP
			}
		});
		return registrationBean;
	}
	@Bean
	PasswordEncoder passwordEncoder() {
		// for backward compatibility
		ShaPasswordEncoder passwordEncoder = new ShaPasswordEncoder(512);
		return new PasswordEncoder() {
			@Override
			public String encode(CharSequence charSequence) {
				return passwordEncoder.encodePassword((String) charSequence, null);
			}

			@Override
			public boolean matches(CharSequence charSequence, String s) {
				return passwordEncoder.isPasswordValid((String) charSequence, s, null);
			}
		};
	}

	@Bean
	TProtocolFactory protocolFactory() {
		return new TJSONProtocol.Factory();
	}

	@Bean
	ServletRegistrationBean userServiceServlet(TUserService.Iface userService) {
		TServlet servlet = new TServlet(new TUserService.Processor<>(userService),
				protocolFactory());
		ServletRegistrationBean registrationBean = new ServletRegistrationBean(servlet,
				"/user");
		return registrationBean;
	}

	public static void main(String[] args) {
		SpringApplication.run(MoneygrApplication.class, args);
	}
}
