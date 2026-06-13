package ra.project._11_project.exception;

/**
 * 401 Unauthorized
 * Chưa đăng nhập hoặc token không hợp lệ
 */
public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException(String message) {
        super(message);
    }
}