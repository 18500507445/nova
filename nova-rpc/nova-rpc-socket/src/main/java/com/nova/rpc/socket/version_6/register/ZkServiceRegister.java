package com.nova.rpc.socket.version_6.register;


import com.nova.rpc.socket.version_6.loadbalance.LoadBalance;
import com.nova.rpc.socket.version_6.loadbalance.RandomLoadBalance;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import java.net.InetSocketAddress;
import java.util.List;

public class ZkServiceRegister implements ServiceRegister {

    /**
     * curator 提供的zookeeper客户端
     */
    private final CuratorFramework client;

    /**
     * zookeeper根路径节点
     */
    private static final String ROOT_PATH = "MyRPC";

    /**
     * 初始化负载均衡器， 这里用的是随机， 一般通过构造函数传入
     */
    private final LoadBalance loadBalance = new RandomLoadBalance();

    /**
     * 这里负责zookeeper客户端的初始化，并与zookeeper服务端建立连接
     */
    public ZkServiceRegister() {
        // 指数时间重试
        RetryPolicy policy = new ExponentialBackoffRetry(1000, 3);
        // zookeeper的地址固定，不管是服务提供者还是，消费者都要与之建立连接
        // sessionTimeoutMs 与 zoo.cfg中的tickTime 有关系，
        // zk还会根据minSessionTimeout与maxSessionTimeout两个参数重新调整最后的超时值。默认分别为tickTime 的2倍和20倍
        // 使用心跳监听状态
        this.client = CuratorFrameworkFactory.builder().connectString("127.0.0.1:2181")
                .sessionTimeoutMs(40000).retryPolicy(policy).namespace(ROOT_PATH).build();
        this.client.start();
        System.err.println("zookeeper 连接成功");
    }

    @Override
    public void register(String serviceName, InetSocketAddress serverAddress) {
        try {
            // serviceName创建成永久节点，服务提供者下线时，不删服务名，只删地址
            if (client.checkExists().forPath("/" + serviceName) == null) {
                client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath("/" + serviceName);
            }
            // 路径地址，一个/代表一个节点
            String path = "/" + serviceName + "/" + getServiceAddress(serverAddress);
            // 临时节点，服务器下线就删除节点
            client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path);
        } catch (Exception e) {
            System.err.println("此服务已存在");
        }
    }

    /**
     * 根据服务名返回地址
     *
     * @param serviceName
     * @return
     */
    @Override
    public InetSocketAddress serviceDiscovery(String serviceName) {
        try {
            List<String> strings = client.getChildren().forPath("/" + serviceName);
            // 负载均衡选择器，选择一个
            String string = loadBalance.balance(strings);
            return parseAddress(string);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 地址 -> XXX.XXX.XXX.XXX:port 字符串
     *
     * @param serverAddress
     * @return
     */
    private String getServiceAddress(InetSocketAddress serverAddress) {
        return serverAddress.getHostName() + ":" + serverAddress.getPort();
    }

    /**
     * 字符串解析为地址
     *
     * @param address
     * @return
     */
    private InetSocketAddress parseAddress(String address) {
        String[] result = address.split(":");
        return new InetSocketAddress(result[0], Integer.parseInt(result[1]));
    }
}
