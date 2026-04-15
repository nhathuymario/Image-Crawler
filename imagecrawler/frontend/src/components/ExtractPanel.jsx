import React from "react";
import TreeNode from "./TreeNode";

function ExtractPanel({
  extractUrl,
  setExtractUrl,
  maxImages,
  setMaxImages,
  includeExternal,
  setIncludeExternal,
  productOnly,
  setProductOnly,
  extractLoading,
  extractError,
  extractResult,
  treeNodes,
  selectedNodes,
  expandedNodes,
  selectedUrls,
  folderFilter,
  setFolderFilter,
  filteredFolderOptions,
  onExtract,
  onToggleSelect,
  onToggleExpand,
  onSelectAllRoots,
  onClearSelectedTree,
  onSelectVisibleFolders,
  onClearVisibleFolders,
  onCopyToDownload,
}) {
  return (
    <section className="section">
      <h2>Extract Images from URL</h2>
      <form onSubmit={onExtract} className="form">
        <div className="form-group">
          <label htmlFor="url">Website URL *</label>
          <input
            id="url"
            type="url"
            placeholder="https://example.com"
            value={extractUrl}
            onChange={(e) => setExtractUrl(e.target.value)}
            required
          />
        </div>

        <div className="form-group">
          <label htmlFor="maxImages">Max Images</label>
          <input
            id="maxImages"
            type="number"
            min="1"
            max="500"
            value={maxImages}
            onChange={(e) =>
              setMaxImages(Number.parseInt(e.target.value || "0", 10) || 1)
            }
          />
        </div>

        <div className="form-group checkbox">
          <input
            id="includeExternal"
            type="checkbox"
            checked={includeExternal}
            onChange={(e) => setIncludeExternal(e.target.checked)}
          />
          <label htmlFor="includeExternal">Include external images</label>
        </div>

        <div className="form-group checkbox">
          <input
            id="productOnly"
            type="checkbox"
            checked={productOnly}
            onChange={(e) => setProductOnly(e.target.checked)}
          />
          <label htmlFor="productOnly">
            Uu tien loc anh san pham (bo qua icon/logo)
          </label>
        </div>
        <p className="hint">
          Tip: Nhieu website luu anh tren CDN khac domain, nen de bat muc nay.
        </p>

        <button
          type="submit"
          disabled={extractLoading}
          className="btn btn-primary"
        >
          {extractLoading ? "Extracting..." : "Extract Images"}
        </button>
      </form>

      {extractError && <div className="error">{extractError}</div>}

      {extractResult && (
        <div className="result">
          <h3>Found {extractResult.totalFound} images</h3>
          {extractResult.totalFound === 0 && (
            <p className="hint">
              Khong thay anh trong HTML tinh. Hay thu bat external images hoac
              trang nay render anh bang JavaScript.
            </p>
          )}

          {treeNodes.length > 0 && (
            <div className="tree-wrap">
              <div className="tree-head">
                <h4>Chon theo cay thu muc URL</h4>
                <div className="tree-actions">
                  <button
                    type="button"
                    className="btn btn-tertiary"
                    onClick={onSelectAllRoots}
                  >
                    Chon tat ca
                  </button>
                  <button
                    type="button"
                    className="btn btn-tertiary"
                    onClick={onClearSelectedTree}
                  >
                    Bo chon
                  </button>
                </div>
              </div>
              <p className="hint">
                Tick folder/file ban muon tai. Neu khong tick gi se copy toan bo
                ket qua.
              </p>

              <div className="folder-picker">
                <div className="folder-picker-head">
                  <h4>Chon nhanh theo danh sach thu muc</h4>
                  <div className="tree-actions">
                    <button
                      type="button"
                      className="btn btn-tertiary"
                      onClick={onSelectVisibleFolders}
                    >
                      Chon danh sach
                    </button>
                    <button
                      type="button"
                      className="btn btn-tertiary"
                      onClick={onClearVisibleFolders}
                    >
                      Bo danh sach
                    </button>
                  </div>
                </div>
                <input
                  type="text"
                  className="folder-search"
                  placeholder="Tim thu muc, vd: Products/Images/42"
                  value={folderFilter}
                  onChange={(e) => setFolderFilter(e.target.value)}
                />
                <div className="folder-list">
                  {filteredFolderOptions.map((item) => (
                    <label key={item.id} className="folder-item">
                      <input
                        type="checkbox"
                        checked={!!selectedNodes[item.id]}
                        onChange={(e) =>
                          onToggleSelect(item.id, e.target.checked)
                        }
                      />
                      <span className="folder-path">{item.path}</span>
                      <span className="folder-count">({item.count})</span>
                    </label>
                  ))}
                  {filteredFolderOptions.length === 0 && (
                    <div className="folder-empty">
                      Khong co thu muc phu hop.
                    </div>
                  )}
                </div>
              </div>

              <div className="tree-panel">
                {treeNodes.map((node) => (
                  <TreeNode
                    key={node.id}
                    node={node}
                    selectedNodes={selectedNodes}
                    expandedNodes={expandedNodes}
                    onToggleSelect={onToggleSelect}
                    onToggleExpand={onToggleExpand}
                  />
                ))}
              </div>
              <p className="hint">Dang chon {selectedUrls.length} URL</p>
            </div>
          )}

          <div className="image-list">
            {extractResult.imageUrls.map((url, idx) => (
              <div key={idx} className="image-item">
                <span className="index">{idx + 1}.</span>
                <span className="url">{url}</span>
              </div>
            ))}
          </div>
          <button
            type="button"
            className="btn btn-secondary"
            onClick={onCopyToDownload}
          >
            Copy to Download
          </button>
        </div>
      )}
    </section>
  );
}

export default ExtractPanel;
