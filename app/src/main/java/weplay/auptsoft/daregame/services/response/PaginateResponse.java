package weplay.auptsoft.daregame.services.response;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrew on 6.3.19.
 */

public class PaginateResponse<T> {
    private int total;
    private int per_page;
    private int current_page;
    private int last_page;
    private String next_page_url;
    private String last_page_url;
    private int from;
    private int to;

    private List<T> data;

    public PaginateResponse() {
        this(
                0,
                0,
                0,
                0,
                null,
                null,
                0,
                0,
                new ArrayList<T>()
        );
    }

    public PaginateResponse(int total, int per_page, int current_page, int last_page, String next_page_url, String last_page_url, int from, int to, List<T> data) {
        this.total = total;
        this.per_page = per_page;
        this.current_page = current_page;
        this.last_page = last_page;
        this.next_page_url = next_page_url;
        this.last_page_url = last_page_url;
        this.from = from;
        this.to = to;
        this.data = data;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPer_page() {
        return per_page;
    }

    public void setPer_page(int per_page) {
        this.per_page = per_page;
    }

    public int getCurrent_page() {
        return current_page;
    }

    public void setCurrent_page(int current_page) {
        this.current_page = current_page;
    }

    public int getLast_page() {
        return last_page;
    }

    public void setLast_page(int last_page) {
        this.last_page = last_page;
    }

    public String getNext_page_url() {
        return next_page_url;
    }

    public void setNext_page_url(String next_page_url) {
        this.next_page_url = next_page_url;
    }

    public String getLast_page_url() {
        return last_page_url;
    }

    public void setLast_page_url(String last_page_url) {
        this.last_page_url = last_page_url;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}