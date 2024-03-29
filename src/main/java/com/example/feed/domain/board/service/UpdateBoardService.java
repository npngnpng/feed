package com.example.feed.domain.board.service;

import com.example.feed.domain.board.controller.dto.request.UpdateBoardRequest;
import com.example.feed.domain.board.domain.Board;
import com.example.feed.domain.board.facade.BoardFacade;
import com.example.feed.domain.user.domain.User;
import com.example.feed.domain.user.facade.UserFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UpdateBoardService {

    private final BoardFacade boardFacade;
    private final UserFacade userFacade;

    @Transactional
    public void execute(UpdateBoardRequest request) {
        User admin = userFacade.getUser();
        Board board = boardFacade.getBoardByAdmin(admin);

        board.update(
                request.getIntroduction(),
                request.getBoardProfileImage()
        );
    }
}
