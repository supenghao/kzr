package com.dhk.payment.intercetor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import com.dhk.payment.entity.param.ParamBaseEntity;

/**
 * @ClassName: ParamValidatorAop
 * @Description: TODO(AOP参数验证)
 * @author huangjsh
 * @date 2016年6月27日
 */
@Aspect
@Component
public class ParamValidatorAop {

    private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    
    @Pointcut("execution(* com.dhk.payment.controller..*.*(..))")
    public void anymethod() {}

    @Before("anymethod()")
    public void doBefore(JoinPoint jp) throws Throwable {
        Method method = ((MethodSignature)jp.getSignature()).getMethod();
        
        Annotation[][] annotations = method.getParameterAnnotations();
        if(annotations.length == 0) {
            return;
        }
        else {
            Object[] args = jp.getArgs();
            String msg = "";
            Set<ConstraintViolation<Object>> constraintViolations = null;
            for (int i = 0; i < annotations.length; i++) {
                for (int j = 0; j < annotations[i].length; j++) {
                    if(annotations[i][j] instanceof Validated) {
                        if(args[i] instanceof ParamBaseEntity) {
                            Validated v = (Validated)annotations[i][j];
                            //hibernate validator 验证
                            constraintViolations = validator.validate(args[i],v.value());
                            if(constraintViolations.iterator().hasNext()) {
                                msg = constraintViolations.iterator().next().getMessage();
                                throw new Exception("参数验证错误，msg = "+msg);
                            }
                        }
                    }
                }
            }
        }
    }
}
