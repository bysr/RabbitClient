package com.example;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Send {

    private final static String QUEUE_NAME = "hello";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
//        factory.setHost("localhost");
        factory.setHost("192.168.2.100");
//        factory.setHost("127.0.0.1");
        factory.setUsername("admin");
        factory.setPassword("password");
//        factory.setVirtualHost(virtualHost);
//        factory.setPort(portNumber);
        /**
         * 或者使用url来创建connectiong
         * */
//        factory.setUri("amqp://username:password@hostName:portNumber/virtualHost");


        Connection connection = factory.newConnection();
        /**创建channel*/
        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        String message = "abc";
        channel.basicPublish("", QUEUE_NAME, null, message.getBytes("UTF-8"));
        System.out.println(" [x] Sent '" + message + "'");
        /**断开连接时，需要关闭所有的channel和connection*/
        channel.close();
        connection.close();
    }
}
