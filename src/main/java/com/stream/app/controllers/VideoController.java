package com.stream.app.controllers;

import com.stream.app.entities.Video;
import com.stream.app.io.CreateResponse;
import com.stream.app.services.VideoService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/video")
public class VideoController {

    private final VideoService videoService;
    private static final int CHUNK_SIZE = 1024 * 1024;

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

    @GetMapping("/videos")
    public List<Video> getAllVideos(){
        return videoService.getAllVideos();
    }

    @GetMapping("/stream/range/{videoId}")
    public ResponseEntity<Resource> streamVideoRange(
            @PathVariable String videoId,
            @RequestHeader(value = "Range", required = false) String range
    ){
        System.out.println(range);

        Video video = videoService.getVideoById(videoId);
        Path path = Paths.get(video.getFilePath());

        Resource resource = new FileSystemResource(path);
        String contentType = video.getContentType();

        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        Long fileLength = path.toFile().length();

        if(range == null) {
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(resource);
        }

        Long startRange, endRange;

        String[] ranges = range.replace("bytes=", "").split("-");
        startRange = Long.parseLong(ranges[0]);

        endRange = startRange + CHUNK_SIZE - 1;

        if (endRange >= fileLength){
            endRange = fileLength - 1;
        }

//        if (ranges.length > 1) {
//            endRange = Long.parseLong(ranges[1]);
//        } else {
//            endRange = fileLength - 1;
//        }
//
//        if (endRange > fileLength - 1){
//            endRange = fileLength - 1;
//        }

        InputStream inputStream;

        try {
            inputStream = Files.newInputStream(path);
            inputStream.skip(startRange);
            long contentLength = endRange - startRange + 1;

            byte[] data = new byte[(int) contentLength];
            int read = inputStream.read(data, 0, data.length);

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Content-Range", "bytes " + startRange + "-" + endRange + "/" + fileLength);
            httpHeaders.add("Cache-Control", "no-cache, no-store, must-revalidate");
            httpHeaders.add("Pragma", "no-cache");
            httpHeaders.add("Expires", "0");
            httpHeaders.add("X-Content-Type-Options", "nosniff");
            httpHeaders.setContentLength(contentLength);

            return ResponseEntity
                    .status(HttpStatus.PARTIAL_CONTENT)
                    .headers(httpHeaders)
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(new ByteArrayResource(data));
        } catch (IOException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }
}
