const jsonHeaders = { "Content-Type": "application/json" };

const parseError = async (response) => {
  try {
    const body = await response.json();
    return body?.message || `Error: ${response.status}`;
  } catch {
    return `Error: ${response.status}`;
  }
};

export const extractImages = async (payload) => {
  const response = await fetch("/api/v1/images/extract", {
    method: "POST",
    headers: jsonHeaders,
    body: JSON.stringify(payload),
  });

  if (!response.ok) {
    throw new Error(await parseError(response));
  }

  return response.json();
};

export const downloadImages = async (payload) => {
  const response = await fetch("/api/v1/images/download", {
    method: "POST",
    headers: jsonHeaders,
    body: JSON.stringify(payload),
  });

  if (!response.ok) {
    throw new Error(await parseError(response));
  }

  return response.json();
};
