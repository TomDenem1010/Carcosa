package com.home.carcosa.boardgame.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.home.carcosa.boardgame.constans.BoardgamePlayWin;

@Entity
@Table(name = "BOARDGAME_PLAY")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class BoardgamePlay {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BOARDGAME_PLAY_SEQ_GEN")
    @SequenceGenerator(name = "BOARDGAME_PLAY_SEQ_GEN", sequenceName = "BOARDGAME_PLAY_SEQ", allocationSize = 1)
    @Column(name = "ID", nullable = false)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "BOARDGAME_ID", nullable = false)
    @ToString.Exclude
    private Boardgame boardgame;

    @Column(name = "PLAY_DATE", nullable = false)
    private LocalDate playDate;

    @Column(name = "PLAYER_COUNT", nullable = false)
    private Integer playerCount;

    @Enumerated(EnumType.STRING)
    @Column(name = "WIN", nullable = false, length = 255)
    private BoardgamePlayWin win;

    @Column(name = "FUN_SCORE", nullable = false)
    private Integer funScore;

    @Column(name = "CREATED_AT", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        if (createdAt == null) {
            createdAt = now;
        }
        if (updatedAt == null) {
            updatedAt = now;
        }
    }

    @PreUpdate
    void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
