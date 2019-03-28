package weplay.auptsoft.daregame.services.response;

/**
 * Created by Andrew on 1.3.19.
 */

public class ResultObject {
    private String status;
    private String data;

    public ResultObject(String status, String data) {
        this.status = status;
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
