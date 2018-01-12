package hipad.rabbitclient.utils;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by wangyawen on 2018/1/10 0010.
 */

public class RecvTask1 extends Thread {


    ConnectionFactory factory;
    Connection connection = null;
    Channel channel;
    Handler handler;
    private final static String TASK_QUEUE_NAME = "task_queue";

    public RecvTask1(ConnectionFactory factory, Handler handler) {
        this.factory = factory;
        this.handler = handler;
    }

    @Override
    public void run() {
        try {
            /**创建一个连接*/
            connection = factory.newConnection();
            /**创建channel，声明一个通道*/
            channel = connection.createChannel();
            /**声明要关注的队列*/
            channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);

            //每次从队列获取的数量
            channel.basicQos(1);


            /**告诉服务器我们需要那个频道的消息，如果频道中有消息，就会执行回调函数handleDelivery*/
            Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
                        throws IOException {
                    String message = new String(body, "UTF-8");
                    Log.d("vivi", "handleDelivery: " + message);
                    Message msg = new Message();
                    msg.what = 1;
                    msg.obj = message;
                    handler.sendMessage(msg);

                }
            };
            boolean autoAck = false;
            /**自动回复队列应答 -- RabbitMQ中的消息确认机制*/
            channel.basicConsume(TASK_QUEUE_NAME, autoAck, consumer);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
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
