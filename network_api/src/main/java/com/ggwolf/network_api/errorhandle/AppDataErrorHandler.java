package com.ggwolf.network_api.errorhandle;

import com.ggwolf.network_api.beans.BaseResponse;

import io.reactivex.functions.Function;

public class AppDataErrorHandler implements Function<BaseResponse, BaseResponse> {
    @Override
    public BaseResponse apply(BaseResponse response) throws Exception {
        // response中code码不为0，则为错误
        if (response instanceof BaseResponse && response.showapiResCode != 0) {
            throw new RuntimeException(response.showapiResCode + "" + (response.showapiResError != null ? response.showapiResError : ""));
        }
        return response;
    }
}
