package com.starking.webflux.exceptions;

import java.util.Map;
import java.util.Optional;

import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.error.ErrorAttributeOptions.Include;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Mono;

/**
 * @author pedroRhamon
 */

@Component
@Order(-2)
public class GlobalExceptionHandler extends AbstractErrorWebExceptionHandler {

	public GlobalExceptionHandler(ErrorAttributes errorAttributes, 
			ResourceProperties resourceProperties,
			ApplicationContext applicationContext,
			ServerCodecConfigurer codecConfigurer) {
		super(errorAttributes, resourceProperties, applicationContext);
		this.setMessageWriters(codecConfigurer.getWriters());
	}

	@Override
	protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
		return RouterFunctions.route(RequestPredicates.all(), this::formatErrorResponse);
	}
	
	private Mono<ServerResponse> formatErrorResponse(ServerRequest request) {
		String query = request.uri().getQuery();
		ErrorAttributeOptions errorAttributeOptions = isErrorAttribute(query);
		
		Map<String, Object> errorAttributes = getErrorAttributes(request, errorAttributeOptions);
		int status =(int) Optional.ofNullable(errorAttributes.get("status")).orElse(500);
		
		return ServerResponse.status(status)
				.contentType(MediaType.APPLICATION_JSON)
				.body(BodyInserters.fromValue(errorAttributes));
	}

	private ErrorAttributeOptions isErrorAttribute(String query) {
		ErrorAttributeOptions errorAttributeOptions = isTraceEnabled(query)
				? ErrorAttributeOptions.of(Include.STACK_TRACE)
				: ErrorAttributeOptions.defaults();
		return errorAttributeOptions;
	}
	
	private boolean isTraceEnabled(String query) {
		return !StringUtils.isEmpty(query) && query.contains("trace=true");
	}

}