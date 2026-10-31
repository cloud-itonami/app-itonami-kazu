# sheets-mcp-component (Go App Component)

> 2026-10-31（JUN-700）: この MCP endpoint 群の edge Worker は退役した。以下は旧構成の記録。公開方式は `../README.md`。

This component exposes Sheets MCP endpoints on `/api/mcp` and `/{nanoid}/api/mcp`.

## Endpoints

- `POST /api/mcp` (JSON-RPC 2.0: `tools/list`, `tools/call`)
- `GET /api/mcp/tools`
- `POST /api/mcp/tools/{tool}/call`
- `POST https://{nanoid}.itonami.app/api/mcp`
- `GET /{nanoid}/api/mcp/tools`
- `POST /{nanoid}/api/mcp/tools/{tool}/call`

## Required Environment

- `CLERK_JWKS_URL`
- `REGISTRY_GET_PERFORMER_URL` (default: `http://r3g1stry.itonami-performers-org-org_34dKrNTTK3cNixZzHIzzFwLw1s4.svc.cluster.local:8080/get-performer`)

## Optional Environment

- `CLERK_ISSUER`
- `CLERK_AUDIENCE`
- `REGISTRY_TIMEOUT_MS`
- `REGISTRY_MAX_RETRIES`
- `REGISTRY_CACHE_TTL_SEC`
- `SHEETS_BACKEND_BRIDGE_URL` (if set, forwards tool calls to backend bridge)
- `BACKEND_TIMEOUT_MS`
- `MCP_ALLOWED_ORIGINS` (comma-separated)

## Build

```bash
itonami build
```

## Deploy

```bash
kubectl apply -f <repo-deploy-config>
```
