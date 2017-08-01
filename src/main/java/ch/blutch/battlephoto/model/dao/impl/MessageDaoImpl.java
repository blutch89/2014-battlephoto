package ch.blutch.battlephoto.model.dao.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ch.blutch.battlephoto.model.dao.MessageDao;
import ch.blutch.battlephoto.model.dao.PhotoDao;
import ch.blutch.battlephoto.model.entity.Category;
import ch.blutch.battlephoto.model.entity.Message;
import ch.blutch.battlephoto.model.entity.Note;
import ch.blutch.battlephoto.model.entity.Photo;
import ch.blutch.battlephoto.model.utils.ModelParameters;

@Repository("messageDao")
public class MessageDaoImpl extends GenericDaoImpl<Message> implements MessageDao {

	@Autowired
	private GenericDaoImpl<Note> genericDao;
	
	public MessageDaoImpl() {
		super(Message.class);
	}

	@Override
	public Message findMessageById(String messageId) {
		Message message = (Message) getCurrentSession().createCriteria(Message.class)
			.add(Restrictions.eq("messageId", messageId))
			.uniqueResult();
		
		return message;
	}
	
}
