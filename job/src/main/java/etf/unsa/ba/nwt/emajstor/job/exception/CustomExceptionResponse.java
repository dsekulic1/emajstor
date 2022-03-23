package etf.unsa.ba.nwt.emajstor.job.exception;

import java.sql.Timestamp;

public class CustomExceptionResponse {

    private Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    private Integer status;
    private String error;
    private String message;
    private String path;

    public CustomExceptionResponse() {
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
