package com.stream.app.services;

import com.stream.app.entities.Video;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface VideoService {

    public Video addVideo(Video video, MultipartFile file);

    public List<Video> getAllVideos();

    Video getVideoById(String videoId);

    String processVideo(String videoId, MultipartFile file);
}
