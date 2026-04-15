import React from "react";

function TreeNode({
  node,
  selectedNodes,
  expandedNodes,
  onToggleSelect,
  onToggleExpand,
  level = 0,
}) {
  const isSelected = !!selectedNodes[node.id];
  const isExpanded = !!expandedNodes[node.id];
  const hasChildren = node.children && node.children.length > 0;

  return (
    <div>
      <div className="tree-row" style={{ paddingLeft: `${level * 14}px` }}>
        <button
          type="button"
          className={`tree-expander ${hasChildren ? "" : "placeholder"}`}
          onClick={() => hasChildren && onToggleExpand(node.id)}
          aria-label={hasChildren ? "Toggle folder" : "No children"}
        >
          {hasChildren ? (isExpanded ? "▾" : "▸") : "•"}
        </button>
        <input
          type="checkbox"
          checked={isSelected}
          onChange={(e) => onToggleSelect(node.id, e.target.checked)}
        />
        <span className={`tree-label ${node.type}`}>{node.name}</span>
        <span className="tree-count">({node.count})</span>
      </div>

      {hasChildren &&
        isExpanded &&
        node.children.map((child) => (
          <TreeNode
            key={child.id}
            node={child}
            selectedNodes={selectedNodes}
            expandedNodes={expandedNodes}
            onToggleSelect={onToggleSelect}
            onToggleExpand={onToggleExpand}
            level={level + 1}
          />
        ))}
    </div>
  );
}

export default TreeNode;
