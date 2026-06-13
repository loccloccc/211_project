package ra.project._11_project.exception;

/**
 * 400 Bad Request
 * Dữ liệu đầu vào không hợp lệ
 */
public class BadRequestException extends RuntimeException {

    public BadRequestException(String message) {
        super(message);
    }
}