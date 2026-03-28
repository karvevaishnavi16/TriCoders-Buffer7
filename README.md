# TriCoders-Buffer7
# Proactive Smart City Crisis & Fair Response Engine —

A DSA-driven simulation that detects city crises through multi-signal zone monitoring, finds the fastest evacuation routes with automatic rerouting, and distributes aid fairly by prioritizing the most damaged and most neglected zones first — all operating as one unified system.

## Modules
- **Phase 1 — Crisis Detection:** Monitors 3 signals per zone using sliding window. Crisis confirmed when 2+ signals trigger simultaneously.
- **Phase 2 — Evacuation Routing:** Dijkstra's algorithm finds fastest evacuation path on a weighted city graph. Auto-reroutes on congestion.
- **Phase 3 — Fair Aid Distribution:** Priority algorithm scores zones by damage and neglect. Most ignored zones always served first.

