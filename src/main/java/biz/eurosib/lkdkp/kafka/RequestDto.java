package biz.eurosib.lkdkp.kafka;


import java.util.UUID;

public class RequestDto extends AbstractDto {
    private String data;
    private UUID taskGuid;

    public RequestDto() {}

    public RequestDto(String data, UUID taskGuid) {
        this.data = data;
        this.taskGuid = taskGuid;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public UUID getTaskGuid() {
        return taskGuid;
    }

    public void setTaskGuid(UUID taskGuid) {
        this.taskGuid = taskGuid;
    }
}
