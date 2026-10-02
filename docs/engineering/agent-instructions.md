# AI Agent Directives & Operating Instructions

---

## 1. Operating Directives
1. **Follow Clean Architecture:** Ensure strict separation across Data, Domain, and Presentation layers.
2. **Never Put Business Logic in UI:** Composable functions must only render `UiState` and emit events.
3. **No Shell File Modifications:** NEVER use `sed`, `awk`, `rm`, or shell redirection. Use built-in IDE tools (`write_file`, `replace_file_content`).
4. **Bilingual Compliance:** Ensure all UI strings and numbers dynamically format for Bengali (`বাংলা`) and English.
5. **Step-by-Step Task Execution:** Execute tasks sequentially from `docs/engineering/task.md`.
