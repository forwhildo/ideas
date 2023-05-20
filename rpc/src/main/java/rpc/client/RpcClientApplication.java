package rpc.client;


import rpc.client.service.AnotherService;
import rpc.client.service.HelloService;

/**
 * @description:
 * @author: wxm
 * @create: 2023-05-20 14:51
 **/
public class RpcClientApplication {
    public static void main(String[] args) {

        RpcClient client = new RpcClient();
        HelloService helloService = client.refer(HelloService.class, "127.0.0.1", 2000);
        System.out.println(helloService.hello());

        AnotherService anotherService = client.refer(AnotherService.class, "127.0.0.1", 3000);
        System.out.println(anotherService.another());
    }
}