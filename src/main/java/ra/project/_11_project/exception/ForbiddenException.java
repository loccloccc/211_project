package ra.project._11_project.exception;

/**
 * 403 Forbidden
 * Không đủ quyền truy cập
 */
public class ForbiddenException extends RuntimeException {

    public ForbiddenException(String message) {
        super(message);
    }
}