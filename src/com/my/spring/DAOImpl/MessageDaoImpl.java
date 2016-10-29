package com.my.spring.DAOImpl;

import com.my.spring.DAO.BaseDao;
import com.my.spring.DAO.MessageDao;
import com.my.spring.model.Message;
import com.my.spring.model.User;
import com.my.spring.utils.DataWrapper;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/6/22.
 */
@Repository
public class MessageDaoImpl extends BaseDao<Message> implements MessageDao {

    @Override
    public boolean addMessage(Message Message) {
        return save(Message);
    }

    @Override
    public boolean deleteMessage(Long id) {
        return delete(get(id));
    }

    @Override
    public boolean updateMessage(Message Message) {
        return update(Message);
    }

    @SuppressWarnings("unchecked")
	@Override
    public DataWrapper<List<Message>> getMessageList() {
        DataWrapper<List<Message>> retDataWrapper = new DataWrapper<List<Message>>();
        List<Message> ret = new ArrayList<Message>();
        Session session = getSession();
        Criteria criteria = session.createCriteria(Message.class);
//        criteria.addOrder(Order.desc("publishDate"));
        try {
            ret = criteria.list();
        }catch (Exception e){
            e.printStackTrace();
        }
        retDataWrapper.setData(ret);
        return retDataWrapper;
    }

	@Override
	public DataWrapper<List<Message>> getMessageListByUserId(Long userId) {
		 DataWrapper<List<Message>> retDataWrapper = new DataWrapper<List<Message>>();
	        
	        //ret=find("select * from User where userId=?"+userId);
		List<Message> ret = null;
        Session session = getSession();
        Criteria criteria = session.createCriteria(Message.class);
        criteria.add(Restrictions.eq("userId",userId));
        try {
            ret = criteria.list();
        }catch (Exception e){
            e.printStackTrace();
        }
        if (ret != null && ret.size() > 0) {
			retDataWrapper.setData(ret);
		}
		return retDataWrapper;
	}
}