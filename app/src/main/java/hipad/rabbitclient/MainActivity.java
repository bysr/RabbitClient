package hipad.rabbitclient;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.rabbitmq.client.ConnectionFactory;

import hipad.rabbitclient.utils.RecvImp;
import hipad.rabbitclient.utils.SendImp;

public class MainActivity extends AppCompatActivity {
    private EditText et_content;
    private Button btn_send;
    private TextView tv_recv;
    ConnectionFactory factory;
    StringBuilder builder = new StringBuilder();
    /*生产者*/
    SendImp send;
    /*消费者*/
    RecvImp recv;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            et_content.setText("");
            builder.append(msg.obj).append("\n");
            tv_recv.setText(builder.toString());

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initRabClient();
        initDate();

    }

    private void initRabClient() {

        factory = new ConnectionFactory();
        factory.setHost("192.168.2.100");
        factory.setPort(5672);
        factory.setUsername("admin");
        factory.setPassword("password");

        recv = new RecvImp(factory, handler);
        recv.start();
        send = new SendImp(factory);
        send.start();

    }

    private void initDate() {

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = et_content.getText().toString().toString();
                send.sendMessage(msg);
            }
        });

    }


    private void initView() {
        et_content = (EditText) findViewById(R.id.et_content);
        btn_send = (Button) findViewById(R.id.btn_send);
        tv_recv = (TextView) findViewById(R.id.tv_receive);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        send.close();
        recv.close();

    }
}
