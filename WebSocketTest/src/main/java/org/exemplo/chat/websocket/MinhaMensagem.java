package org.exemplo.chat.websocket;

import javax.json.JsonObject;

public class MinhaMensagem {

    private JsonObject jsonObject;

    public JsonObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }

//    @Override
//    public String toString() {
//        return new Gson().toJson(this);
//    }
}
