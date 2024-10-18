package ps.com.viajeros.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ps.com.viajeros.entities.chat.ChatEntity;
import ps.com.viajeros.entities.chat.MessageEntity;

import java.util.List;
@Repository
public interface MessageRepository extends JpaRepository<MessageEntity, Long> {
    List<MessageEntity> findByChat(ChatEntity chat);
}
