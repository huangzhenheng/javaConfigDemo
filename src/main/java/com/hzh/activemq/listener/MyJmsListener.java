package com.hzh.activemq.listener;

import com.google.gson.Gson;
import com.hzh.pojo.User;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * @Description
 * @auther hzh
 * @create 2018-11-22 14:40
 */
@Component
public class MyJmsListener implements MessageListener {


    @Override
    public void onMessage(Message message) {

        if (message instanceof TextMessage){
            TextMessage textMessage = (TextMessage) message;
            try {
                if (textMessage.getJMSType().equals("user")){
                    System.out.println(textMessage.getJMSType());
                }
            } catch (JMSException e) {
                e.printStackTrace();
            }

            try {
                User user=new Gson().fromJson(textMessage.getText(),User.class);
                System.out.println(user.getName());
            } catch (JMSException e) {
                e.printStackTrace();
            }

        }

    }
}


