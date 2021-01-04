package goveed20.CardPaymentService.exceptions;

public class InvalidBankClientDataException extends RuntimeException {
    public InvalidBankClientDataException(String message) {
        super(message);
    }
}
