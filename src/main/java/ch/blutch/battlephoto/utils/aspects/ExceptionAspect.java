package ch.blutch.battlephoto.utils.aspects;

import java.io.IOException;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import ch.blutch.battlephoto.utils.exceptions.ExceptionUtil;

@Component
@Aspect
public class ExceptionAspect {

//	@Around("execution(* ch.blutch.battlephoto..*(..))")
//	public Object catchException(ProceedingJoinPoint joinPoint) {
//		try {
//			return joinPoint.proceed();
//		} catch (Throwable t) {System.out.println("2&&&&&&&&&&&&&");
//			// Redirige l'utilisateur
//			try {
//				ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
//				ec.redirect("/" + ec.getContextName().toLowerCase() + "/exception");
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//			
//			return null;
//		}
//	}
	
//	@AfterThrowing(pointcut = "execution(* ch.blutch.battlephoto.controller.TemplateController.*(..))", throwing= "error")
//	public void logAfterThrowing(JoinPoint joinPoint, Throwable error) {
//		System.out.println("%%%%%");
//	}
	
//	@AfterThrowing(pointcut = "execution(* ch.blutch.battlephoto..*(..))", throwing= "error")
//	public void logAfterThrowing(JoinPoint joinPoint, Throwable error) {System.out.println("!!!!");
//		ExceptionUtil.handleGlobalException(error);
//	}
	
}
