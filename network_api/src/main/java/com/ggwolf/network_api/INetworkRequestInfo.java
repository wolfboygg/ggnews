package com.ggwolf.network_api;

import java.util.HashMap;

/**
 * 对请求头的封装
 */

public interface INetworkRequestInfo {
    HashMap<String, String> getRequestHeaderMap();

    boolean isDebug();

}
