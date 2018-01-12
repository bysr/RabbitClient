package hipad.rabbitclient.utils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by wangyawen on 2018/1/10 0010.
 */

public class SendImp extends Thread {

    ConnectionFactory factory;
    Connection connection = null;
    Channel channel;
    private final static String QUEUE_NAME = "hello";

    public SendImp(ConnectionFactory factory) {
        this.factory = factory;
    }

    @Override
    public void run() {


        try {
            connection = factory.newConnection();
            /**创建channel*/
            channel = connection.createChannel();
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

    }

    /**
     * 发送消息
     *
     * @param message
     */
    public void sendMessage(String message) {
        try {
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 关闭连接
     */
    public void close() {
        try {
            if (channel != null)
                channel.close();
            if (connection != null)
                connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

    }

}
