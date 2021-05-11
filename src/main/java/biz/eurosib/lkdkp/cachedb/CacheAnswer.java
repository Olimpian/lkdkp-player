package biz.eurosib.lkdkp.cachedb;

import javax.persistence.*;

@Entity
@Table(name = "cache_answer")
public class CacheAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String request;
    @Column(length = 32767)
    private String data;

    public CacheAnswer() {}
    public CacheAnswer(String request, String data) {
        this.request = request;
        this.data = data;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "CacheAnswer{" +
                ", request='" + this.request + '\'' +
                ", data='" + this.data + '\'' +
                '}';
    }
}
