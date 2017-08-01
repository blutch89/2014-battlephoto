package ch.blutch.battlephoto.utils.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import ch.blutch.battlephoto.utils.Log4JUtil;


@Component
@Aspect
public class LoggingAspect {

	@Around("execution(* ch.blutch.battlephoto..*(..))")
	public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
		Class targetClass = joinPoint.getTarget().getClass();
		
		Log4JUtil.getLogger().trace("start \"" + targetClass.getSimpleName() + ":" + joinPoint.getSignature().getName() + "\"");
		Object result = joinPoint.proceed();
		Log4JUtil.getLogger().trace("end \"" + targetClass.getSimpleName() + ":" + joinPoint.getSignature().getName() + "\"");
		
		return result;
	}
	
}
