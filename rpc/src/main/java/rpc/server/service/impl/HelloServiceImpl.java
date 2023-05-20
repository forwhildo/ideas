package rpc.server.service.impl;

import rpc.client.service.HelloService;

/**
 * @description:
 * @author: wxm
 * @create: 2023-05-20 14:05
 **/
public class HelloServiceImpl implements HelloService {

    @Override
    public String hello() {
        return "hello , RPC";
    }
}