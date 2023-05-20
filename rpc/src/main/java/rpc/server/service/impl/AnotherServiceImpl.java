package rpc.server.service.impl;


import rpc.server.service.AnotherService;

/**
 * @description:
 * @author: wxm
 * @create: 2023-05-20 15:00
 **/
public class AnotherServiceImpl implements AnotherService {
    @Override
    public String another(){
        return "another";
    }
}