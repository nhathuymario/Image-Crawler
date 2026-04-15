import React from "react";

function DownloadPanel({
  downloadUrls,
  setDownloadUrls,
  targetDir,
  setTargetDir,
  overwrite,
  setOverwrite,
  downloadLoading,
  downloadError,
  downloadResult,
  onDownload,
}) {
  return (
    <section className="section">
      <h2>Download Images</h2>
      <form onSubmit={onDownload} className="form">
        <div className="form-group">
          <label htmlFor="urls">Image URLs (one per line) *</label>
          <textarea
            id="urls"
            placeholder="https://example.com/img1.jpg\nhttps://example.com/img2.png"
            value={downloadUrls}
            onChange={(e) => setDownloadUrls(e.target.value)}
            rows="6"
            required
          />
        </div>

        <div className="form-group">
          <label htmlFor="targetDir">Target Directory</label>
          <input
            id="targetDir"
            type="text"
            placeholder="downloads"
            value={targetDir}
            onChange={(e) => setTargetDir(e.target.value)}
          />
        </div>

        <div className="form-group checkbox">
          <input
            id="overwrite"
            type="checkbox"
            checked={overwrite}
            onChange={(e) => setOverwrite(e.target.checked)}
          />
          <label htmlFor="overwrite">Overwrite existing files</label>
        </div>

        <button
          type="submit"
          disabled={downloadLoading}
          className="btn btn-primary"
        >
          {downloadLoading ? "Downloading..." : "Download Images"}
        </button>
      </form>

      {downloadError && <div className="error">{downloadError}</div>}

      {downloadResult && (
        <div className="result">
          <h3>
            Downloaded {downloadResult.totalDownloaded} of{" "}
            {downloadResult.totalRequested} images
          </h3>

          {downloadResult.savedFiles.length > 0 && (
            <div className="saved-files">
              <h4>Saved Files:</h4>
              <ul>
                {downloadResult.savedFiles.map((file, idx) => (
                  <li key={idx}>{file}</li>
                ))}
              </ul>
            </div>
          )}

          {downloadResult.failedUrls.length > 0 && (
            <div className="failed-urls">
              <h4>Failed URLs:</h4>
              <ul>
                {downloadResult.failedUrls.map((url, idx) => (
                  <li key={idx}>{url}</li>
                ))}
              </ul>
            </div>
          )}
        </div>
      )}
    </section>
  );
}

export default DownloadPanel;
