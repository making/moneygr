package moneygr.infra.logging;

import lombok.extern.slf4j.Slf4j;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Slf4j
public class ServiceTraceLogger {
	@Around("@within(org.springframework.stereotype.Service)")
	public Object trace(ProceedingJoinPoint joinPoint) throws Throwable {
		if (log.isInfoEnabled()) {
			log.info("[START] {}", joinPoint.getSignature());
		}
		try {
			Object ret = joinPoint.proceed();
			if (log.isInfoEnabled()) {
				log.info("[END  ] {}", joinPoint.getSignature());
			}
			return ret;
		}
		catch (Exception e) {
			if (log.isWarnEnabled()) {
				log.warn("[FAIL ] " + joinPoint.getSignature(), e);
			}
			throw e;
		}
	}
}
