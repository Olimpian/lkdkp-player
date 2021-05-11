package biz.eurosib.lkdkp.kafka;


import org.json.JSONObject;

public class ResponseDto extends AbstractDto {
    private String fullResponse;

    private String result;
    private String data;

    public ResponseDto() {}
    public ResponseDto(String fullResponse) {
        this.fullResponse = fullResponse;
        JSONObject response = new JSONObject(fullResponse);

        this.result = response.getJSONObject("response").get("result").toString();
        this.data = response.getJSONObject("response").get("data").toString();
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getFullResponse() {
        return fullResponse;
    }

    public void setFullResponse(String fullResponse) {
        this.fullResponse = fullResponse;
    }

//    public boolean ikOK() {
//
//    }

    public String getGuid() {
        JSONObject data = new JSONObject(this.data);
        return data.get("GUID").toString();
    }

}
