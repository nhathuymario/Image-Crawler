import React from "react";

function Tabs({ activeTab, onChange }) {
  return (
    <div className="tabs">
      <button
        className={`tab ${activeTab === "extract" ? "active" : ""}`}
        onClick={() => onChange("extract")}
      >
        Extract Images
      </button>
      <button
        className={`tab ${activeTab === "download" ? "active" : ""}`}
        onClick={() => onChange("download")}
      >
        Download Images
      </button>
    </div>
  );
}

export default Tabs;
