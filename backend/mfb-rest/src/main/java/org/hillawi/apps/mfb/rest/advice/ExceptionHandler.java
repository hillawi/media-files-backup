package org.hillawi.apps.mfb.rest.advice;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.zalando.problem.spring.web.advice.ProblemHandling;

/**
 * @author Ahmed Hillawi
 * @since 18/11/22
 */
@ControllerAdvice
public class ExceptionHandler implements ProblemHandling {
}
