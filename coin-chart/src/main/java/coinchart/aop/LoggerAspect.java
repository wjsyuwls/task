package coinchart.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Slf4j
public class LoggerAspect {

    @Around("execution(* coinchart..web.*Controller.*(..)) || execution(* coinchart..service.*Impl.*(..)) || execution(* coinchart..repository.*Mapper.*(..))")
    public Object logPrint(ProceedingJoinPoint joinPoint) throws Throwable {

        String name = joinPoint.getSignature().getDeclaringTypeName();
        String type = "";

        if (name.indexOf("Controller") > -1) {
            type = "Controller : ";
        }
        else if (name.indexOf("Service") > -1) {
            type = "ServiceImpl : ";
        }
        else if (name.indexOf("Mapper") > -1) {
            type = "Mapper : ";
        }

        log.debug(type + name + "." + joinPoint.getSignature().getName() + "()");

        return joinPoint.proceed();
    }
}
