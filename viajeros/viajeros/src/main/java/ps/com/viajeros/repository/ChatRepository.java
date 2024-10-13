package ps.com.viajeros.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ps.com.viajeros.entities.chat.ChatEntity;
import ps.com.viajeros.entities.UserEntity;
import ps.com.viajeros.entities.viajes.ViajesEntity;

import java.util.List;
import java.util.Optional;

public interface ChatRepository extends JpaRepository<ChatEntity, Long> {
    Optional<ChatEntity> findByChoferAndPasajeroAndViaje(UserEntity chofer, UserEntity pasajero, ViajesEntity viaje);
    Optional<ChatEntity> findByViajeAndChoferAndPasajero(ViajesEntity viaje, UserEntity choferId, UserEntity pasajeroId);

    // Buscar chats donde el usuario sea el chofer o el pasajero, ordenados por la fecha del último mensaje
    List<ChatEntity> findByChoferOrPasajero(UserEntity chofer, UserEntity pasajero);
}