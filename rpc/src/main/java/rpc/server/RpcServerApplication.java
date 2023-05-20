package rpc.server;


import rpc.server.service.impl.AnotherServiceImpl;
import rpc.server.service.impl.HelloServiceImpl;

/**
 * @description:
 * @author: wxm
 * @create: 2023-05-20 14:06
 **/
public class RpcServerApplication {
    public static void main(String[] args) {

        RpcServer server = new RpcServer();
        new Thread(() -> server.export(new HelloServiceImpl() , 2000)).start();
        new Thread(() -> server.export(new AnotherServiceImpl(),3000)).start();
        System.out.println("all start");
    }
}