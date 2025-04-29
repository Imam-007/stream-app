# 📽️ Stream App - Spring Boot Video Streaming Application

A streaming platform built with **Spring Boot** and **PostgreSQL** that allows users to upload and stream videos. The application supports:

- Direct video uploads for small files
- Chunked uploads for large files
- HLS (HTTP Live Streaming) using FFmpeg
- Video forwarding support
- Range-based streaming for optimal client performance

---

## 🚀 Features

- Upload videos (small and large)
- Stream videos using HLS format
- Support for byte-range requests for smooth seeking
- Organize videos under courses (planned feature)
- RESTful APIs for managing and streaming videos
- Multipart file upload with content-type preservation

---

## 🧰 Tech Stack

- **Spring Boot** (REST API)
- **PostgreSQL** (Database)
- **FFmpeg** (HLS Video Processing)
- **Lombok** (for boilerplate reduction)
- **JPA/Hibernate** (ORM)
- **Maven** (build tool)

---

## 📁 Project Structure
```
com.stream.app
├── controllers         # REST API Controllers
│   └── VideoController.java
├── entities            # JPA Entities
│   ├── Course.java
│   └── Video.java
├── io                  # DTOs and response wrappers
│   └── CreateResponse.java
├── repositories        # Spring Data JPA Repositories
│   └── VideoRepository.java
├── services            # Service Interfaces
│   └── VideoService.java
├── services.impl       # Service Implementations
│   └── VideoServiceImpl.java
└── resources
└── application.properties
```

---

## 🧪 API Endpoints

### 📤 Upload Video
**POST** `/video/save`

**Request Parameters:**
- `file`: Multipart video file
- `title`: String
- `description`: String

### 🎬 Stream Video
**GET** `/video/stream/{videoId}`  
Streams the entire video file.

**GET** `/video/stream/range/{videoId}`  
Supports byte-range requests for forwarding/rewinding.

### 📃 Get All Videos
**GET** `/video/videos`

---

## 🔁 Video Processing (HLS)

- Videos are processed using FFmpeg (stubbed in `processVideo` method).
- Outputs are stored in three resolutions: **360p**, **720p**, and **1080p**.
- Output path example: `hls/{videoId}/360/`

To process a video via HLS, extend the `processVideo()` method using FFmpeg command like:

```bash
ffmpeg -i input.mp4 -vf scale=w:h -c:a copy output.m3u8

git clone
cd stream-app
sudo apt install ffmpeg
mvn spring-boot:run

```
