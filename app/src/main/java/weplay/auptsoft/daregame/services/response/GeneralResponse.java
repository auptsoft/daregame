package weplay.auptsoft.daregame.services.response;

/**
 * Created by Andrew on 6.3.19.
 */

public class GeneralResponse<T> {
    private String message;
    private T data;

    public GeneralResponse(String message, T data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
