package rpc.client;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.Socket;

/**
 * @description:
 * @author: wxm
 * @create: 2023-05-20 14:33
 **/
public class RpcClient {

    public <T> T refer(Class<T> interfaceClass,String host,int port){
        check(interfaceClass,host,port);

        return (T)Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class[]{interfaceClass}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                Socket client = new Socket(host,port);
                ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());
                ObjectInputStream in  = new ObjectInputStream(client.getInputStream());
                try {
                    out.writeUTF(method.getName());
                    out.writeObject(method.getParameterTypes());
                    out.writeObject(args);

                    Object result = in.readObject();
                    if (result instanceof Throwable) {
                        throw (Throwable) result;
                    }
                    return result;
                }catch (Exception e){
                    e.printStackTrace();
                    throw new RuntimeException("error");
                }finally {
                    in.close();
                    out.close();
                    client.close();
                }

            }
        });

    }


    private <T> void check(Class<T> interfaceClass, String host, int port) {
        if (interfaceClass == null)
            throw new IllegalArgumentException("Interface class == null");
        if (! interfaceClass.isInterface())
            throw new IllegalArgumentException("The " + interfaceClass.getName() + " must be interface class!");
        if (host == null || host.length() == 0)
            throw new IllegalArgumentException("Host == null!");
        if (port <= 0 || port > 65535)
            throw new IllegalArgumentException("Invalid port " + port);
    }

}