package com.example.feed.domain.board.service;

import com.example.feed.domain.board.domain.Board;
import com.example.feed.domain.board.facade.BoardFacade;
import com.example.feed.domain.member.controller.dto.response.MemberElement;
import com.example.feed.domain.member.controller.dto.response.MemberListResponse;
import com.example.feed.domain.member.domain.Member;
import com.example.feed.domain.member.domain.repository.MemberJpaRepository;
import com.example.feed.domain.user.domain.User;
import com.example.feed.domain.user.facade.UserFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class QueryJoinBoardMemberService {

    private final UserFacade userFacade;
    private final BoardFacade boardFacade;
    private final MemberJpaRepository memberJpaRepository;

    @Transactional(readOnly = true)
    public MemberListResponse execute() {
        User user = userFacade.getUser();
        Board board = boardFacade.getBoardByAdmin(user);
        List<MemberElement> memberList = memberJpaRepository.findAllByBoardAndApproved(board, false)
                .stream()
                .map(this::memberListBuilder)
                .toList();

        return new MemberListResponse(memberList);
    }

    private MemberElement memberListBuilder(Member member) {
        return MemberElement.builder()
                .id(member.getId())
                .name(member.getName())
                .memberProfileImage(member.getMemberProfileImage())
                .build();
    }
}
