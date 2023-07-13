package com.nova.socket.version_4.client;


import com.nova.rpc.manual.entity.RPCRequest;
import com.nova.rpc.manual.entity.RPCResponse;

public interface RPCClient {

    RPCResponse sendRequest(RPCRequest request);
}
