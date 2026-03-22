package com.home.carcosa.boardgame.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "BOARDGAME_GROUP", uniqueConstraints = {
        @UniqueConstraint(name = "UK_BOARDGAME_GROUP_BOARDGAME", columnNames = { "BOARDGAME_ID" })
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class BoardgameGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BOARDGAME_GROUP_SEQ_GEN")
    @SequenceGenerator(name = "BOARDGAME_GROUP_SEQ_GEN", sequenceName = "BOARDGAME_GROUP_ID_SEQ", allocationSize = 1)
    @Column(name = "ID", nullable = false)
    @EqualsAndHashCode.Include
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "BOARDGAME_ID", nullable = false, unique = true)
    @ToString.Exclude
    private Boardgame boardgame;

    @Column(name = "GROUP_ID", nullable = false)
    private Long groupId;

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
