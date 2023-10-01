package com.nova.rpc.socket.version_4.client;


import com.nova.rpc.socket.entity.RPCRequest;
import com.nova.rpc.socket.entity.RPCResponse;

public interface RPCClient {

    RPCResponse sendRequest(RPCRequest request);
}
