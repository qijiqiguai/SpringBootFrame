package tech.qi.core;



/**
 * @author wangqi
 */
public class ServiceException extends RuntimeException{
    private static final long serialVersionUID = 703489799884576939L;

    private int status = Constants.DEFAULT_EXCEPTION;

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(int status, String message) {
        super(message);
        this.status = status;
    }

    public ServiceException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public ServiceException(int status, String message, Throwable throwable) {
        super(message, throwable);
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
