package biz.eurosib.lkdkp.kafka;


import org.json.JSONObject;

import java.util.UUID;

public class ResponseDto extends AbstractDto {
    //private String fullResponse;

    private int result;
    private Object data;
    private UUID taskGuid;

    public ResponseDto() {}
    public ResponseDto(String fullResponse) {
        //this.fullResponse = fullResponse;
        JSONObject response = new JSONObject(fullResponse);

        if(response.isNull("response")) {
            this.result = response.getInt("result");
            this.data = response.get("data");
        } else {
            this.result = response.getJSONObject("response").getInt("result");
            this.data = response.getJSONObject("response").getString("data");
        }
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getGuid() {
        JSONObject data = new JSONObject(this.data);
        return data.isNull("GUID") ? null : data.get("GUID").toString();
    }

    public UUID getTaskGuid() {
        return taskGuid;
    }

    public void setTaskGuid(UUID taskGuid) {
        this.taskGuid = taskGuid;
    }
}
