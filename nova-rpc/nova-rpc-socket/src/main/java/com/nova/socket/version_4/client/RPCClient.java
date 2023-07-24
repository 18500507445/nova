package com.nova.socket.version_4.client;


import com.nova.socket.entity.RPCRequest;
import com.nova.socket.entity.RPCResponse;

public interface RPCClient {

    RPCResponse sendRequest(RPCRequest request);
}
