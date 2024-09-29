package ps.com.viajeros.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "chofer")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChoferEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_chofer")
    private Long idChofer;
    @Column(name = "user_name")
    private  String nameChofer;
}
