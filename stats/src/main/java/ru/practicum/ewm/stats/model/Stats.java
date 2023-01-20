package ru.practicum.ewm.stats.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDateTime;

@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "stats")
public class Stats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id;

    @Column(name = "app")
    String app;

    @Column(name = "uri")
    String uri;

    @Column(name = "ip")
    String ip;

    @Column(name = "view_date")
    LocalDateTime timestamp;

    @Override
    public int hashCode() {
        return 42;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Stats other = (Stats) obj;
        return id != null && id.equals(other.getId());
    }

    @Override
    public String toString() {
        return "Stats{" +
                "id=" + id +
                ", app='" + app + '\'' +
                ", uri='" + uri + '\'' +
                ", ip='" + ip + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
