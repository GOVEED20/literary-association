package goveed20.PaymentConcentrator.exceptions;


import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class StatusCodeException extends RuntimeException {
    private final HttpStatus statusCode;

    public StatusCodeException(HttpStatus statusCode) {
        this.statusCode = statusCode;
    }
}
