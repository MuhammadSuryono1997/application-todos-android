package com.yono.mytodos.api.client;

import com.yono.mytodos.api.ApiHelper;

public class Client {
    public <T> T Client(Class<T> service, String basUrl){
        return ApiHelper.client(basUrl).create(service);
    }
}
