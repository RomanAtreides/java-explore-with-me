package ru.practicum.ewm.compilation.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm.event.model.Event;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "compilations")
public class Compilation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "compilation_of_events",
            joinColumns = @JoinColumn(name = "compilation_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id")
    )
    Set<Event> events;

    @Column(name = "pinned")
    Boolean pinned;

    @Column(name = "title", nullable = false, unique = true)
    String title;

    @Override
    public int hashCode() {
        return Objects.hash(title);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Compilation other = (Compilation) obj;
        return Objects.equals(title, other.title);
    }

    @Override
    public String toString() {
        return "Compilation{" +
                "id=" + id +
                ", events_size=" + events.size() +
                ", pinned=" + pinned +
                ", title='" + title + '\'' +
                '}';
    }
}
