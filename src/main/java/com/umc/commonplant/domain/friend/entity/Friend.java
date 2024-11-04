package com.umc.commonplant.domain.friend.entity;

import com.umc.commonplant.domain.BaseTime;
import com.umc.commonplant.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.C;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Getter
@Table(name = "friend")
@NoArgsConstructor
@Entity
public class Friend extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "friend_idx")
    private Long friendIdx;

    @Column(name = "sender")
    private String sender;

    @Column(name = "receiver")
    private String receiver;

    @Column(name = "placeCode")
    private String placeCode;

    @Column(name = "status")
    private String status; // WAITING ACCEPTED REJECTED

    @Builder
    public Friend(String sender, String receiver, String placeCode, String status){
        this.sender = sender;
        this.receiver = receiver;
        this.placeCode = placeCode;
        this.status = status; // WAITING, ACCEPTED, REJECTED
    }

//    @ManyToOne(fetch = LAZY)
//    @JoinColumn(name = "user_idx", nullable = false)
//    @OnDelete(action = OnDeleteAction.CASCADE)
//    private User sender;
//
//    @ManyToOne(fetch = LAZY)
//    @JoinColumn(name = "user_idx", nullable = false)
//    @OnDelete(action = OnDeleteAction.CASCADE)
//    private User receiver;

}
