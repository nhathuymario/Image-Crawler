import { useMemo, useState } from "react";
import {
  buildUrlTree,
  collectFolderOptions,
  collectSelectedUrls,
} from "../utils/urlTree";
import { extractImages } from "../services/imageApi";

export function useExtractController() {
  const [extractUrl, setExtractUrl] = useState("");
  const [maxImages, setMaxImages] = useState(20);
  const [includeExternal, setIncludeExternal] = useState(true);
  const [productOnly, setProductOnly] = useState(true);
  const [extractLoading, setExtractLoading] = useState(false);
  const [extractResult, setExtractResult] = useState(null);
  const [extractError, setExtractError] = useState("");
  const [treeNodes, setTreeNodes] = useState([]);
  const [nodeUrlMap, setNodeUrlMap] = useState({});
  const [selectedNodes, setSelectedNodes] = useState({});
  const [expandedNodes, setExpandedNodes] = useState({});
  const [folderFilter, setFolderFilter] = useState("");

  const selectedUrls = useMemo(
    () => collectSelectedUrls(selectedNodes, nodeUrlMap),
    [selectedNodes, nodeUrlMap],
  );

  const folderOptions = useMemo(
    () => collectFolderOptions(treeNodes),
    [treeNodes],
  );

  const filteredFolderOptions = useMemo(() => {
    const query = folderFilter.trim().toLowerCase();
    if (!query) {
      return folderOptions;
    }
    return folderOptions.filter((item) =>
      item.path.toLowerCase().includes(query),
    );
  }, [folderFilter, folderOptions]);

  const handleExtractSubmit = async (e) => {
    e.preventDefault();
    if (!extractUrl.trim()) {
      setExtractError("URL is required");
      return;
    }

    setExtractLoading(true);
    setExtractError("");
    setExtractResult(null);

    try {
      const data = await extractImages({
        url: extractUrl,
        maxImages,
        includeExternal,
        productOnly,
      });

      setExtractResult(data);
      const built = buildUrlTree(data.imageUrls || []);
      setTreeNodes(built.tree);
      setNodeUrlMap(built.nodeUrlMap);

      const expanded = {};
      built.tree.forEach((root) => {
        expanded[root.id] = true;
      });
      setExpandedNodes(expanded);
      setSelectedNodes({});
    } catch (error) {
      setExtractError(error.message || "Failed to extract images");
    } finally {
      setExtractLoading(false);
    }
  };

  const toggleTreeSelection = (nodeId, checked) => {
    setSelectedNodes((prev) => ({ ...prev, [nodeId]: checked }));
  };

  const toggleTreeExpand = (nodeId) => {
    setExpandedNodes((prev) => ({ ...prev, [nodeId]: !prev[nodeId] }));
  };

  const selectAllRoots = () => {
    const all = {};
    treeNodes.forEach((node) => {
      all[node.id] = true;
    });
    setSelectedNodes(all);
  };

  const clearSelectedTree = () => {
    setSelectedNodes({});
  };

  const selectVisibleFolders = () => {
    setSelectedNodes((prev) => {
      const next = { ...prev };
      filteredFolderOptions.forEach((item) => {
        next[item.id] = true;
      });
      return next;
    });
  };

  const clearVisibleFolders = () => {
    setSelectedNodes((prev) => {
      const next = { ...prev };
      filteredFolderOptions.forEach((item) => {
        delete next[item.id];
      });
      return next;
    });
  };

  return {
    extractUrl,
    setExtractUrl,
    maxImages,
    setMaxImages,
    includeExternal,
    setIncludeExternal,
    productOnly,
    setProductOnly,
    extractLoading,
    extractResult,
    extractError,
    treeNodes,
    selectedNodes,
    expandedNodes,
    selectedUrls,
    folderFilter,
    setFolderFilter,
    filteredFolderOptions,
    handleExtractSubmit,
    toggleTreeSelection,
    toggleTreeExpand,
    selectAllRoots,
    clearSelectedTree,
    selectVisibleFolders,
    clearVisibleFolders,
  };
}
