package com.umc.commonplant.domain.revision.entity;

import com.umc.commonplant.domain.BaseTime;
import lombok.Getter;
import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Getter
@Table(name = "RevisionHistory")
@Entity
@RevisionEntity
public class RevisionHistory extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column @RevisionNumber
    private Long revisionId;

    @Column @RevisionTimestamp
    private Long revisionTimeStamp;

    public LocalDateTime getRevisionTimeStamp() {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(revisionTimeStamp), ZoneId.systemDefault());
    }

}

