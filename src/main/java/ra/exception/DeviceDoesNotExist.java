package ra.exception;

public class DeviceDoesNotExist extends RuntimeException {
    public DeviceDoesNotExist(String message) {
        super(message);
    }
}
