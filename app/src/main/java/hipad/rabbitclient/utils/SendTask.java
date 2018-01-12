package hipad.rabbitclient.utils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by wangyawen on 2018/1/10 0010.
 */

public class SendTask extends Thread {

    ConnectionFactory factory;
    Connection connection = null;
    Channel channel;
    private final static String TASK_QUEUE_NAME = "task_queue";

    public SendTask(ConnectionFactory factory) {
        this.factory = factory;
    }

    @Override
    public void run() {


        try {
            connection = factory.newConnection();
            /**创建channel*/
            channel = connection.createChannel();
            channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);
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
        for (int i = 0; i < 10; i++) {
            String msg = message + ": " + i;
            try {
                channel.basicPublish("", TASK_QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, msg.getBytes("UTF-8"));
            } catch (IOException e) {
                e.printStackTrace();
            }
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
