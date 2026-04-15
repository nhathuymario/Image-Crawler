import React, { useState } from "react";
import "./App.css";
import DownloadPanel from "./components/DownloadPanel";
import ExtractPanel from "./components/ExtractPanel";
import Header from "./components/layout/Header";
import Tabs from "./components/layout/Tabs";
import { useExtractController } from "./hooks/useExtractController";
import { useDownloadController } from "./hooks/useDownloadController";

function App() {
  const [activeTab, setActiveTab] = useState("extract");
  const extract = useExtractController();
  const download = useDownloadController();

  // Copy extracted URLs to download tab
  const handleCopyToDownload = () => {
    if (extract.extractResult && extract.extractResult.imageUrls) {
      const chosen =
        extract.selectedUrls.length > 0
          ? extract.selectedUrls
          : extract.extractResult.imageUrls;
      download.setDownloadUrls(chosen.join("\n"));
      setActiveTab("download");
    }
  };

  return (
    <div className="app">
      <Header />
      <Tabs activeTab={activeTab} onChange={setActiveTab} />

      <main className="container">
        {activeTab === "extract" && (
          <ExtractPanel
            extractUrl={extract.extractUrl}
            setExtractUrl={extract.setExtractUrl}
            maxImages={extract.maxImages}
            setMaxImages={extract.setMaxImages}
            includeExternal={extract.includeExternal}
            setIncludeExternal={extract.setIncludeExternal}
            productOnly={extract.productOnly}
            setProductOnly={extract.setProductOnly}
            extractLoading={extract.extractLoading}
            extractError={extract.extractError}
            extractResult={extract.extractResult}
            treeNodes={extract.treeNodes}
            selectedNodes={extract.selectedNodes}
            expandedNodes={extract.expandedNodes}
            selectedUrls={extract.selectedUrls}
            folderFilter={extract.folderFilter}
            setFolderFilter={extract.setFolderFilter}
            filteredFolderOptions={extract.filteredFolderOptions}
            onExtract={extract.handleExtractSubmit}
            onToggleSelect={extract.toggleTreeSelection}
            onToggleExpand={extract.toggleTreeExpand}
            onSelectAllRoots={extract.selectAllRoots}
            onClearSelectedTree={extract.clearSelectedTree}
            onSelectVisibleFolders={extract.selectVisibleFolders}
            onClearVisibleFolders={extract.clearVisibleFolders}
            onCopyToDownload={handleCopyToDownload}
          />
        )}

        {activeTab === "download" && (
          <DownloadPanel
            downloadUrls={download.downloadUrls}
            setDownloadUrls={download.setDownloadUrls}
            targetDir={download.targetDir}
            setTargetDir={download.setTargetDir}
            overwrite={download.overwrite}
            setOverwrite={download.setOverwrite}
            downloadLoading={download.downloadLoading}
            downloadError={download.downloadError}
            downloadResult={download.downloadResult}
            onDownload={download.handleDownloadSubmit}
          />
        )}
      </main>

      <footer className="footer">
        <p>Image Crawler Service</p>
      </footer>
    </div>
  );
}

export default App;
