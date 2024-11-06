package ru.practicum.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.model.enums.ParticipationRequestStatus;

import java.time.Instant;

@Entity
@Table(name = "participation_requests")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class ParticipationRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Instant created;
    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event; //event id
    @ManyToOne
    @JoinColumn(name = "requester_id")
    private User requester; //requester id
    @Enumerated(EnumType.STRING)
    private ParticipationRequestStatus status;
}
