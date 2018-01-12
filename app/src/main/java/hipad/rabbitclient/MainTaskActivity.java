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

import hipad.rabbitclient.utils.RecvTask1;
import hipad.rabbitclient.utils.RecvTask2;
import hipad.rabbitclient.utils.SendTask;

public class MainTaskActivity extends AppCompatActivity {


    private EditText et_content;
    private Button btn_send;
    private TextView tv_recv;
    ConnectionFactory factory;
    StringBuilder builder = new StringBuilder();
    private RecvTask1 recv1;
    private RecvTask2 recv2;
    private SendTask sendTask;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            et_content.setText("");
            builder.append(msg.what + "===>").append(msg.obj).append("\n");
            tv_recv.setText(builder.toString());

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_task);

        initView();
        initRabClient();
        initData();

    }

    private void initData() {
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = et_content.getText().toString().toString();
                sendTask.sendMessage(msg);
            }
        });

    }


    private void initRabClient() {

        factory = new ConnectionFactory();
        factory.setHost("192.168.2.100");
        factory.setPort(5672);
        factory.setUsername("admin");
        factory.setPassword("password");

        recv1 = new RecvTask1(factory, handler);
        recv1.start();
        recv2 = new RecvTask2(factory, handler);
        recv2.start();
        sendTask = new SendTask(factory);
        sendTask.start();


    }


    private void initView() {
        et_content = (EditText) findViewById(R.id.et_content);
        btn_send = (Button) findViewById(R.id.btn_send);
        tv_recv = (TextView) findViewById(R.id.tv_receive);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        recv1.close();
        recv2.close();
        sendTask.close();
    }
}
