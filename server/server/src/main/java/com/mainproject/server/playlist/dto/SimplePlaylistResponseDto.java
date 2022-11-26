package com.mainproject.server.playlist.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class SimplePlaylistResponseDto {
    private long playlistId;

    private String title;

    private List<PlaylistItemResponseDto> playlistItems;

    private String thumbnail;

    private boolean status;

    private List<String> category;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;
    
}