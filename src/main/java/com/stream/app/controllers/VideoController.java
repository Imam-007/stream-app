package com.stream.app.controllers;

import com.stream.app.entities.Video;
import com.stream.app.io.CreateResponse;
import com.stream.app.services.VideoService;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/video")
public class VideoController {

    private final VideoService videoService;

    public VideoController(VideoService videoService) {
        this.videoService = videoService;
    }

    @PostMapping("/save")
    public ResponseEntity<?> addVideo(
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam("description") String description
            ){
        Video video = new Video();
        video.setTitle(title);
        video.setDescription(description);
        video.setVideoId(UUID.randomUUID().toString());

       Video savedVideo = videoService.addVideo(video, file);

       if(savedVideo != null){
           return ResponseEntity
                   .status(HttpStatus.OK)
                   .body(video);
       }else {
           return ResponseEntity
                   .status(HttpStatus.INTERNAL_SERVER_ERROR)
                   .body(CreateResponse.builder()
                           .message("Video not uploaded")
                           .success(false)
                           .build()
                   );
       }
    }

    @GetMapping("/stream/{videoId}")
    public ResponseEntity<Resource> stream(
            @PathVariable String videoId
    ){
         Video video = videoService.getVideoById(videoId);
         String contentType = video.getContentType();
         String filePath = video.getFilePath();

         Resource resource = new FileSystemResource(filePath);

         if (contentType == null){
             contentType = "application/octet-stream";
         }

         return ResponseEntity
                 .ok()
                 .contentType(MediaType.parseMediaType(contentType))
                 .body(resource);
    }

    public List<Video> getAllVideos(){
        return videoService.getAllVideos();
    }
}
