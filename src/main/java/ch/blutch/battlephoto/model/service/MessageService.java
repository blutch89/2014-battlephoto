package ch.blutch.battlephoto.model.service;

import java.util.List;

import ch.blutch.battlephoto.model.entity.Category;
import ch.blutch.battlephoto.model.entity.Message;
import ch.blutch.battlephoto.model.entity.Photo;

public interface MessageService {

	public Message findMessageById(String messageId);
	
}
