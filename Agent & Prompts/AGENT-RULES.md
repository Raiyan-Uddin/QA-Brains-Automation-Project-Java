# AGENT-RULES

Master rulebook for project agents in `Agent & Prompts`.

## Global Policy
- Agents must follow strict single responsibility.
- Agents must not perform tasks outside their assigned role.
- `docs/test cases/*.txt` files are the source of truth for automation coverage.
- Automation code and TXT test cases must stay in one-to-one parity.

## Agent Responsibilities

### 1) `Test-Case-Writer`
- Sole purpose: write/update module test cases in TXT (CSV-editable format).
- Must not edit automation source code.
- Must not execute terminal commands.

### 2) `Test-Automation-Coder`
- Sole purpose: read module TXT test cases and update automation code accordingly.
- Must not edit test-case TXT files.
- Must not execute terminal commands.
- Must not add extra tests outside TXT scope.

### 3) `Test-Execution-Agent`
- Sole purpose: run project commands/tests in terminal and report outcomes.
- Must not edit test-case TXT files.
- Must not edit automation source code.

### 4) `Test-Reporter-Agent`
- Sole purpose: read Surefire XML reports from `target/surefire-reports/` and generate an HTML report in `docs/Test Reports/`.
- Must not run `mvn test` or any test execution commands.
- Must not edit automation source code.
- Must not edit test-case CSV files.
- Must not modify any file in `src/`, `target/`, or `docs/test cases/`.
- Output files go only to `docs/Test Reports/` with timestamped filenames.

## Strict Sync Rule (TXT to Code)
- Every test case in module TXT must exist in automation.
- Any automation case not listed in module TXT must be removed or TXT must be updated first.
- No hidden, bonus, or speculative test cases are allowed in code.

## Routing Rules
- If request is test-case writing -> use `Test-Case-Writer`.
- If request is code implementation/fix -> use `Test-Automation-Coder`.
- If request is run/execute command or suite -> use `Test-Execution-Agent`.
- If request is generate/build HTML test report -> use `Test-Reporter-Agent`.

## Module Naming Rules
- Store test cases under `docs/test cases`.
- Use file naming convention: `<Module>-TestCases.txt`.
- Keep IDs unique and sequential inside each module.

## Output Format Rules
- Test-case files must remain plain TXT in CSV-compatible format.
- Terminal commands must be copyable and run from project root.
- Agent outputs must be concise and traceable to changed files.

