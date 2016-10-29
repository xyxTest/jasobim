package com.my.spring.service;

import com.my.spring.model.Message;
import com.my.spring.utils.DataWrapper;

import java.util.List;

/**
 * Created by Administrator on 2016/6/22.
 */
public interface MessageService {
	DataWrapper<Void> addMessage(Message message,String token);
    DataWrapper<Void> deleteMessage(Long id ,String token );
    //DataWrapper<Void> updateMessage(Message message,String token);
    DataWrapper<List<Message>> getMessageList(String token);
    DataWrapper<List<Message>> getMessageListByUserId(Long userId,String token);
}