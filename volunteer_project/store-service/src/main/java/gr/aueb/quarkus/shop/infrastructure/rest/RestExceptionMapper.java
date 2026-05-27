package gr.aueb.quarkus.shop.infrastructure.rest;

import gr.aueb.quarkus.shop.common.BusinessRuleException;

import gr.aueb.quarkus.shop.common.ResourceNotFoundException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.util.logging.Logger;

@Provider
public class RestExceptionMapper implements ExceptionMapper<Exception> {

  private static final Logger LOG = Logger.getLogger(RestExceptionMapper.class.getName());

  @Override
  public Response toResponse(Exception exception) {
    Response errorResponse = mapExceptionToResponse(exception);
    return errorResponse;
  }
  
  private Response mapExceptionToResponse(Exception exception) {
    // Use response from WebApplicationException as they are v
    if (exception instanceof WebApplicationException) {
      // Overwrite error message
      Response originalErrorResponse = ((WebApplicationException) exception).getResponse();
      return Response.fromResponse(originalErrorResponse)
                     .entity(originalErrorResponse.getStatusInfo().getReasonPhrase())
                     .build();
    }
    // Special mappings
    else if (exception instanceof IllegalArgumentException) {
      return Response.status(400).entity(exception.getMessage()).build();
    } else if (exception instanceof BusinessRuleException){
      return Response.status(409).entity(exception.getMessage()).build();
    } else if (exception instanceof ResourceNotFoundException){
      return Response.status(404).entity(exception.getMessage()).build();
    }
    // Use 500 (Internal Server Error) for all other
    else {
      LOG.info(exception.getMessage());
      exception.printStackTrace();
      return Response.serverError().entity("Internal Server Error").build();
    }
  }
}
