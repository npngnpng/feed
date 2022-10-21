package com.example.feed.domain.like.service;

import com.example.feed.domain.feed.domain.Feed;
import com.example.feed.domain.feed.facade.FeedFacade;
import com.example.feed.domain.like.controller.dto.response.LikeResponse;
import com.example.feed.domain.like.domain.Like;
import com.example.feed.domain.like.domain.repository.LikeRepository;
import com.example.feed.domain.like.exception.FeedNotLikedException;
import com.example.feed.domain.like.facade.LikeFacade;
import com.example.feed.domain.member.domain.Member;
import com.example.feed.domain.member.facade.MemberFacade;
import com.example.feed.domain.user.domain.User;
import com.example.feed.domain.user.facade.UserFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class SubLikeService {

    private final LikeRepository likeRepository;
    private final UserFacade userFacade;
    private final FeedFacade feedFacade;
    private final LikeFacade likeFacade;
    private final MemberFacade memberFacade;

    @Transactional
    public LikeResponse execute(Long feedId) {

        User user = userFacade.getUser();
        Feed feed = feedFacade.getFeed(feedId);
        Member member = memberFacade.getMemberByBoardAndUser(feed.getBoard(), user);
        Like like = likeFacade.getLike(member, feed);

        if (!likeFacade.checkLiked(member, feed)) {
            throw FeedNotLikedException.EXCEPTION;
        }

        likeRepository.delete(like);
        feed.subLikeCounts();
        return new LikeResponse(feed.getLikeCounts());
    }
}
