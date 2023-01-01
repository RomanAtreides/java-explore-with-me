package ru.practicum.ewm.stats.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "stats")
public class Stats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "app")
    private String app; // Идентификатор сервиса для которого записывается информация

    @Column(name = "uri")
    private String uri; // URI для которого был осуществлен запрос

    @Column(name = "ip")
    private String ip; // IP-адрес пользователя, осуществившего запрос

    @Column(name = "view_date")
    private LocalDateTime timestamp; // Дата и время, когда был совершен запрос к эндпоинту (в формате "yyyy-MM-dd HH:mm:ss")
}
