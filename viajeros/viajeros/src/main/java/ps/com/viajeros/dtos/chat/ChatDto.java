package ps.com.viajeros.dtos.chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatDto {
    private Long id;
    private String name;
    private String lastMessage;
    private LocalDateTime lastMessageDate;
    private String idTrip;
    private String idPassenger;
}
