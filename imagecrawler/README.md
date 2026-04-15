# Image Crawler Service

Spring Boot service to extract image URLs from a website and download them to local storage.

## Features

- Extract image URLs from HTML pages (`img`, `source`, `srcset`)
- Keep same-domain images only (optional)
- Download images to `downloads/` or custom folder
- Handle filename collisions safely
- Request validation and global exception handling

## Run

```bash
./mvnw spring-boot:run
```

On Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

## API

### 1) Extract image URLs

`POST /api/v1/images/extract`

Request:

```json
{
  "url": "https://example.com",
  "maxImages": 20,
  "includeExternal": false
}
```

Response:

```json
{
  "sourceUrl": "https://example.com",
  "totalFound": 2,
  "imageUrls": ["https://example.com/a.jpg", "https://example.com/b.png"]
}
```

### 2) Download images

`POST /api/v1/images/download`

Request:

```json
{
  "imageUrls": ["https://example.com/a.jpg", "https://example.com/b.png"],
  "targetDirectory": "downloads",
  "overwrite": false
}
```

Response:

```json
{
  "totalRequested": 2,
  "totalDownloaded": 2,
  "savedFiles": [
    "C:\\path\\to\\project\\downloads\\a.jpg",
    "C:\\path\\to\\project\\downloads\\b.png"
  ],
  "failedUrls": []
}
```

## Notes

- Some websites render images via JavaScript; for those pages, add an implementation to `DynamicImageExtractor` using Playwright or Selenium.
- Respect website Terms of Service and robots policies before crawling.
