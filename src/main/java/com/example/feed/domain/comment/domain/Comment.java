package com.example.feed.domain.comment.domain;

import com.example.feed.domain.feed.domain.Feed;
import com.example.feed.domain.member.domain.Member;
import com.example.feed.global.entity.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Comment extends BaseTimeEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feed_id", nullable = false)
    private Feed feed;

    @Column(columnDefinition = "VARCHAR(300)", nullable = false)
    private String comment;

    @Builder
    public Comment(Member member, Feed feed, String comment) {
        this.member = member;
        this.feed = feed;
        this.comment = comment;
    }

    public void update(String comment) {
        this.comment = comment;
    }
}
