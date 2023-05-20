package rpc.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @description:
 * @author: wxm
 * @create: 2023-05-20 14:08
 **/
public class RpcServer {

    public void export (Object service,int port)  {
        try {
            checkServiceAndPort(service,port);
            start(service,port);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void start(Object service, int port) throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);

        while (true){
            Socket server = serverSocket.accept();

            new Thread(() -> {

                try {
                    ObjectInputStream in  = new ObjectInputStream(server.getInputStream());
                    ObjectOutputStream out = new ObjectOutputStream(server.getOutputStream());
                    try {

                        //反射获取方法（客户端接口方法）并且调用 一一对应客户端
                        String methodName = in.readUTF();
                        Class<?>[] parameterTypes  = (Class<?>[]) in.readObject();
                        Object[] args  = (Object[]) in.readObject();

                        Method method = service.getClass().getMethod(methodName, parameterTypes);
                        Object result = method.invoke(service, args);

                        out.writeObject(result);
                    } catch (Exception e) {
                        out.writeObject(e.fillInStackTrace());
                    }finally {
                        in.close();
                        out.close();
                        server.close();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }).start();
        }


    }

    private void checkServiceAndPort(Object service,int port){
        if (service == null) {
            throw new IllegalArgumentException("service instance == null");
        }
        if (port <= 0 || port > 65535) {
            throw new IllegalArgumentException("Invalid port " + port);
        }
    }
}