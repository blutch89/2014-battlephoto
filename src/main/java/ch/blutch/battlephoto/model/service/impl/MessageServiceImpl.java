package ch.blutch.battlephoto.model.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import ch.blutch.battlephoto.model.dao.impl.MessageDaoImpl;
import ch.blutch.battlephoto.model.dao.impl.PhotoDaoImpl;
import ch.blutch.battlephoto.model.entity.Category;
import ch.blutch.battlephoto.model.entity.Message;
import ch.blutch.battlephoto.model.entity.Photo;
import ch.blutch.battlephoto.model.service.MessageService;
import ch.blutch.battlephoto.model.service.PhotoService;

@Service("messageService")
@Transactional(readOnly = true)
public class MessageServiceImpl extends GenericServiceImpl<Message> implements MessageService {

	@Autowired
	private MessageDaoImpl messageDao;

	@Override
	public Message findMessageById(String messageId) {
		return messageDao.findMessageById(messageId);
	}

	
	
	// !!!! ne pas oublier les transactions à mettre dans les services uniquement
	
}
