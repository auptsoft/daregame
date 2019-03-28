package weplay.auptsoft.daregame.services.request;

/**
 * Created by Andrew on 6.3.19.
 */

public class GeneralRequest<T> {
    private T data;

    public GeneralRequest(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
