import { useState } from "react";
import { downloadImages } from "../services/imageApi";

export function useDownloadController() {
  const [downloadUrls, setDownloadUrls] = useState("");
  const [targetDir, setTargetDir] = useState("downloads");
  const [overwrite, setOverwrite] = useState(false);
  const [downloadLoading, setDownloadLoading] = useState(false);
  const [downloadResult, setDownloadResult] = useState(null);
  const [downloadError, setDownloadError] = useState("");

  const handleDownloadSubmit = async (e) => {
    e.preventDefault();
    if (!downloadUrls.trim()) {
      setDownloadError("At least one URL is required");
      return;
    }

    const urls = downloadUrls
      .split("\n")
      .map((u) => u.trim())
      .filter((u) => u.length > 0);

    if (urls.length === 0) {
      setDownloadError("Invalid URLs");
      return;
    }

    setDownloadLoading(true);
    setDownloadError("");
    setDownloadResult(null);

    try {
      const data = await downloadImages({
        imageUrls: urls,
        targetDirectory: targetDir || "downloads",
        overwrite,
      });
      setDownloadResult(data);
    } catch (error) {
      setDownloadError(error.message || "Failed to download images");
    } finally {
      setDownloadLoading(false);
    }
  };

  return {
    downloadUrls,
    setDownloadUrls,
    targetDir,
    setTargetDir,
    overwrite,
    setOverwrite,
    downloadLoading,
    downloadResult,
    downloadError,
    handleDownloadSubmit,
  };
}
