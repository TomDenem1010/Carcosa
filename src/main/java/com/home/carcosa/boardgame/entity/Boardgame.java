package com.home.carcosa.boardgame.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
import java.util.ArrayList;
import java.util.List;

import com.home.carcosa.boardgame.constans.BoardgameStatus;
import com.home.carcosa.boardgame.constans.BoardgameType;

@Entity
@Table(name = "BOARDGAME", uniqueConstraints = {
        @UniqueConstraint(name = "UK_BOARDGAME_BGG_LINK", columnNames = { "BGG_LINK" })
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class Boardgame {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BOARDGAME_SEQ_GEN")
    @SequenceGenerator(name = "BOARDGAME_SEQ_GEN", sequenceName = "BOARDGAME_SEQ", allocationSize = 1)
    @Column(name = "ID", nullable = false)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "NAME", nullable = false, length = 255)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE", nullable = false, length = 255)
    private BoardgameType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false, length = 255)
    private BoardgameStatus status;

    @Column(name = "BGG_LINK", nullable = false, length = 255)
    private String bggLink;

    @Column(name = "CREATED_AT", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT", nullable = false)
    private LocalDateTime updatedAt;

    @OneToOne(mappedBy = "boardgame", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ToString.Exclude
    private BoardgameGroup group;

    @OneToMany(mappedBy = "boardgame", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @ToString.Exclude
    private List<BoardgamePlay> plays = new ArrayList<>();

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
