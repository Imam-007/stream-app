package com.stream.app.services.impl;

import com.stream.app.entities.Video;
import com.stream.app.repositories.VideoRepository;
import com.stream.app.services.VideoService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Service
public class VideoServiceImpl implements VideoService {

    @Value("${files.video}")
    String DIR;

    @Value("{file.video.hsl}")
    String HSL_DIR;

    private final VideoRepository videoRepository;

    public VideoServiceImpl(VideoRepository videoRepository) {
        this.videoRepository = videoRepository;
    }

    @PostConstruct
    public void init(){

        File file = new File(DIR);
        if (!file.exists()){
            file.mkdir();
        }

        File file1 = new File(HSL_DIR);
        if (!file1.exists()){
            file1.mkdir();
        }
    }

    @Override
    public Video addVideo(Video video, MultipartFile file) {

        try {
            String fileName = file.getOriginalFilename();
            String contentType = file.getContentType();
            InputStream inputStream = file.getInputStream();

            String cleanFileName = StringUtils.cleanPath(fileName);
            String cleanFolder = StringUtils.cleanPath(DIR);

            Path path = Paths.get(cleanFolder, cleanFileName);
            System.out.println(path);

            Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);

            video.setContentType(contentType);
            video.setFilePath(path.toString());

            return videoRepository.save(video);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Video> getAllVideos() {
        return videoRepository.findAll();
    }

    @Override
    public Video getVideoById(String videoId) {
       return videoRepository.findById(videoId).orElseThrow(()->new RuntimeException("Video Not Found"));
    }

    @Override
    public String processVideo(String videoId, MultipartFile file) {

        Video video = this.getVideoById(videoId);
        String filePath = video.getFilePath();
        Path videoPath = Paths.get(filePath);

        String output360p = HSL_DIR + videoId + "/360/";
        String output720p = HSL_DIR + videoId + "/720/";
        String output1080p = HSL_DIR + videoId + "/1080/";

        try {
            Files.createDirectories(Paths.get(output360p));
            Files.createDirectories(Paths.get(output720p));
            Files.createDirectories(Paths.get(output1080p));
        } catch (IOException e) {
            throw new RuntimeException("Video processing fail !!!!");
        }
        return "";
    }
}
