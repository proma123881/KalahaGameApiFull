package com.kalah.mapper;

import com.kalah.model.KalahaGame;
import com.kalah.model.MoveResponse;
import com.kalah.model.NewGameResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.util.DefaultUriBuilderFactory;

import javax.servlet.http.HttpServletRequest;

/**
 * GameMapper Class to map the response object
 *
 * @author Proma Chowdhury
 * @version 1.0
 */
@Component
@RequiredArgsConstructor
public class KalahaGameMapper {

    private final HttpServletRequest request;


    /**
     * Mapper to map the game details to the NewResponse object
     *
     * @param game created game object
     * @return NewGameResponse object with {id,uri} variables
     */
    public NewGameResponse getNewGameResponse(KalahaGame game) {
        return NewGameResponse.builder().id(String.valueOf(game.getId()))
                .uri(new DefaultUriBuilderFactory().builder()
                        .scheme(request.getScheme())
                        .host(request.getServerName())
                        .port(request.getServerPort())
                        .path("games/" + game.getId())
                        .build().toString()).build();
    }

    /**
     * Mapper to map the game details to the MoveResponse object
     *
     * @param game updated game object
     * @return MoveResponse object with {id,url status} variables
     */
    public MoveResponse getMovedGameResponse(KalahaGame game) {
        return MoveResponse.builder()
                .id(String.valueOf(game.getId()))
                .status(game.getBoard())
                .url(new DefaultUriBuilderFactory().builder()
                        .scheme(request.getScheme())
                        .host(request.getServerName())
                        .port(request.getServerPort())
                        .path("games/" + game.getId())
                        .build().toString()).build();
    }

}
