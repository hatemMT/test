package com.clinics.core.boundary.graphql;

import com.clinics.core.control.exceptions.AppValidationException;
import com.clinics.core.control.exceptions.AppointmentNotFound;
import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class GraphQlExceptionsHandler extends DataFetcherExceptionResolverAdapter {


    @Override
    protected GraphQLError resolveToSingleError(Throwable ex, DataFetchingEnvironment env) {
        if (ex instanceof AppointmentNotFound) {
            return GraphqlErrorBuilder.newError()
                                      .errorType(ErrorType.BAD_REQUEST)
                                      .message(ex.getMessage())
                                      .path(env.getExecutionStepInfo().getPath())
                                      .location(env.getField().getSourceLocation())
                                      .build();
        } else {
            return null;
        }
    }

    @Override
    protected List<GraphQLError> resolveToMultipleErrors(Throwable ex, DataFetchingEnvironment env) {
        if (ex instanceof AppValidationException) {
            return ((AppValidationException) ex)
                    .getErrors().stream()
                    .map(message -> GraphqlErrorBuilder.newError()
                                                       .errorType(ErrorType.BAD_REQUEST)
                                                       .message(ex.getMessage())
                                                       .path(env.getExecutionStepInfo()
                                                                .getPath())
                                                       .location(env.getField()
                                                                    .getSourceLocation())
                                                       .build())
                    .collect(Collectors.toList());
        } else {
            return null;
        }
    }
}