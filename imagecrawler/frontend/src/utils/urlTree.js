const sortNodes = (nodes) => {
  const typeOrder = { domain: 0, folder: 1, file: 2 };
  return nodes
    .map((node) => ({
      ...node,
      children: node.children ? sortNodes(node.children) : [],
    }))
    .sort((a, b) => {
      if (typeOrder[a.type] !== typeOrder[b.type]) {
        return typeOrder[a.type] - typeOrder[b.type];
      }
      return a.name.localeCompare(b.name);
    });
};

export const buildUrlTree = (urls) => {
  const roots = [];
  const nodeIndex = new Map();
  const nodeUrls = new Map();

  const ensureNode = (id, name, type, parent) => {
    if (nodeIndex.has(id)) {
      return nodeIndex.get(id);
    }

    const node = { id, name, type, children: [] };
    nodeIndex.set(id, node);
    nodeUrls.set(id, new Set());
    if (parent) {
      parent.children.push(node);
    } else {
      roots.push(node);
    }
    return node;
  };

  urls.forEach((rawUrl, index) => {
    let parsed;
    try {
      parsed = new URL(rawUrl);
    } catch {
      return;
    }

    const hostId = `domain:${parsed.host}`;
    const hostNode = ensureNode(hostId, parsed.host, "domain", null);
    nodeUrls.get(hostId).add(rawUrl);

    const segments = parsed.pathname.split("/").filter(Boolean);
    let parent = hostNode;
    let parentId = hostId;

    if (segments.length === 0) {
      const fileId = `${parentId}/file:${index}`;
      const fallback =
        parsed.pathname && parsed.pathname !== "/" ? parsed.pathname : "index";
      const fileNode = ensureNode(fileId, fallback, "file", parent);
      nodeUrls.get(fileNode.id).add(rawUrl);
      return;
    }

    segments.forEach((segment, segmentIndex) => {
      const isLast = segmentIndex === segments.length - 1;
      const nodeType = isLast ? "file" : "folder";
      const decoded = decodeURIComponent(segment);
      const nodeId = `${parentId}/${nodeType}:${decoded}`;
      const node = ensureNode(nodeId, decoded, nodeType, parent);
      nodeUrls.get(node.id).add(rawUrl);
      parent = node;
      parentId = nodeId;
    });
  });

  const nodeUrlMap = {};
  const withCount = (nodes) =>
    nodes.map((node) => {
      const count = (nodeUrls.get(node.id) || new Set()).size;
      nodeUrlMap[node.id] = Array.from(nodeUrls.get(node.id) || []);
      return {
        ...node,
        count,
        children: withCount(node.children || []),
      };
    });

  const tree = sortNodes(withCount(roots));
  return { tree, nodeUrlMap };
};

export const collectSelectedUrls = (selectedNodes, nodeUrlMap) => {
  const combined = new Set();
  Object.entries(selectedNodes).forEach(([nodeId, checked]) => {
    if (!checked) {
      return;
    }
    const urls = nodeUrlMap[nodeId] || [];
    urls.forEach((url) => combined.add(url));
  });
  return Array.from(combined);
};

export const collectFolderOptions = (treeNodes) => {
  const result = [];

  const walk = (nodes, basePath = "") => {
    nodes.forEach((node) => {
      const currentPath = basePath ? `${basePath}/${node.name}` : node.name;
      if (node.type === "domain" || node.type === "folder") {
        result.push({
          id: node.id,
          path: currentPath,
          count: node.count,
        });
      }
      if (node.children && node.children.length > 0) {
        walk(node.children, currentPath);
      }
    });
  };

  walk(treeNodes);
  return result;
};
