package etf.unsa.ba.nwt.emajstor.review.rabbitmq;

public class RabbitMQStatus {
    private int status;
    private String id;

    public RabbitMQStatus() {
    }

    public RabbitMQStatus(int status, String id) {
        this.status = status;
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
