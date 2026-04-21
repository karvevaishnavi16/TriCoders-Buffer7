# TriCoders-Buffer7

## Smart City Crisis & Fair Response Engine

A DSA-driven Java simulation for smart-city emergency management. The project detects crises from multi-signal zone data, predicts how risk may spread across nearby zones, computes the fastest evacuation routes, and distributes aid fairly so that both high-damage and repeatedly ignored zones receive attention.

The system is organized as a menu-based console application with three core phases and two future-scope demos:

- `Phase 1` - Crisis Detection
- `Phase 2` - Evacuation Routing
- `Phase 3` - Fair Aid Distribution
- `Demo` - Ambulance Dispatcher
- `Demo` - Hospital Assigner

## Project Description

This project models a city as interconnected zones and simulates how authorities could respond during an urban emergency.

- Sensor readings are loaded from a CSV file for each zone over multiple time ticks.
- Each zone is evaluated using environmental signal, SOS count, and infrastructure stress.
- A crisis is confirmed when multiple danger indicators are active together.
- Once a zone becomes critical, the system predicts nearby zones that may be affected next.
- Evacuation routes are then computed to the nearest safe zone using shortest-path logic.
- Finally, limited aid is distributed using a fairness-based priority score that considers damage, vulnerability, and neglect.

The result is a single unified workflow that combines detection, prediction, routing, and equitable response.

## Key Features

- Multi-signal crisis detection from CSV-based city data
- Zone-wise risk evaluation using threshold logic
- Risk ranking of zones using a heap
- Spread prediction from critical zones using BFS
- Fastest evacuation route calculation using Dijkstra's algorithm
- Dynamic rerouting when congestion changes road cost
- Fair aid allocation using a custom priority score
- Severity-based ambulance dispatch demo
- Least-loaded hospital assignment demo

## DSA Concepts Used

This project applies multiple data structures and algorithms across different modules.

### 1. Graph

Used to model the city road network, where:

- vertices represent zones
- weighted edges represent travel time between connected zones

Implemented in:

- `src/city/CityGraph.java`
- `src/phase2/EvacuationRouter.java`
- `src/phase1/SpreadPredictor.java`

### 2. Breadth-First Search (BFS)

Used in spread prediction to identify nearby zones that may be affected when a crisis starts in a critical zone.

- starts from a critical zone
- explores neighboring zones level by level
- stops after 2 hops to show likely short-range spread

Implemented in:

- `src/phase1/SpreadPredictor.java`

### 3. Dijkstra's Algorithm

Used in evacuation routing to find the fastest path from a selected danger zone to the nearest safe zone.

- works on the weighted city graph
- computes minimum travel time
- supports rerouting after congestion changes edge weights

Implemented in:

- `src/phase2/EvacuationRouter.java`
- `src/city/CityGraph.java`

### 4. Heap / Priority Queue

Used wherever the system must repeatedly choose the highest-priority or lowest-load item efficiently.

Applications in this project:

- `RiskHeap` uses a max-heap to rank zones by risk score
- `AidDistributor` uses a priority queue to choose the highest-priority zones for aid
- `AmbulanceDispatcher` uses a priority queue to serve highest-severity emergency calls first
- `HospitalAssigner` uses a priority queue to assign patients to the least-loaded hospital

Implemented in:

- `src/phase1/RiskHeap.java`
- `src/phase3/AidDistributor.java`
- `src/phase2/AmbulanceDispatcher.java`
- `src/phase3/HospitalAssigner.java`

### 5. Sliding Window

The project includes a signal monitoring component that stores recent readings for each zone using a fixed-size window.

- helps track recent signal history
- keeps only the latest readings per zone
- demonstrates rolling monitoring of sensor values

Implemented in:

- `src/phase1/SignalMonitor.java`

### 6. HashMap

Used extensively for fast lookup and storage.

Examples:

- zone name to `Zone` object mapping
- graph adjacency list representation
- zone name to aid record mapping
- distance, parent, and level maps in traversal and shortest-path logic

Implemented across:

- `src/Main.java`
- `src/city/CityGraph.java`
- `src/phase1/SpreadPredictor.java`
- `src/phase2/EvacuationRouter.java`
- `src/phase3/AidDistributor.java`

### 7. Queue

Used in BFS for spread prediction to process zones in level order.

Implemented in:

- `src/phase1/SpreadPredictor.java`

### 8. Set

Used to prevent duplicate processing and track served or visited zones.

Examples:

- visited zones in BFS
- zones already alerted for spread prediction
- safe zones in evacuation routing
- zones served in the current aid cycle

Implemented across:

- `src/phase1/SignalSimulator.java`
- `src/phase1/SpreadPredictor.java`
- `src/phase2/EvacuationRouter.java`
- `src/phase3/AidDistributor.java`

## Module Breakdown

### Phase 1 - Crisis Detection

This phase reads zone-wise readings from `data/mock_data.csv` and evaluates each zone.

- Inputs: environmental signal, SOS count, infrastructure stress, zone type, vulnerability
- Logic: a zone becomes critical when 2 or more abnormal indicators occur together
- Output: alerts, crisis detection messages, and spread prediction warnings

Core classes:

- `src/phase1/SignalSimulator.java`
- `src/phase1/SpreadPredictor.java`
- `src/phase1/RiskHeap.java`
- `src/city/Zone.java`

### Phase 2 - Evacuation Routing

This phase computes safe evacuation paths through a weighted road network.

- city modeled as a graph
- safe zones defined as `H`, `I`, and `J`
- shortest route computed using Dijkstra's algorithm
- route recalculated when road congestion is simulated

Core classes:

- `src/city/CityGraph.java`
- `src/phase2/EvacuationRouter.java`

### Phase 3 - Fair Aid Distribution

This phase distributes limited aid using a fairness-based score.

Priority is computed from:

- damage level
- vulnerability bonus
- neglect history
- number of times aid has already been received

This ensures aid is not repeatedly sent only to the same zones, and ignored zones gain higher priority over time.

Core classes:

- `src/phase3/AidDistributor.java`
- `src/phase3/AidRecord.java`
- `src/phase3/FairnessScorer.java`

### Future Scope Demos

#### Ambulance Dispatcher

- handles emergency calls by severity
- highest severity is dispatched first

Class:

- `src/phase2/AmbulanceDispatcher.java`

#### Hospital Assigner

- assigns patients to the least-loaded hospital
- helps balance occupancy across hospitals

Class:

- `src/phase3/HospitalAssigner.java`

## Project Structure

```text
src/
  Main.java
  city/
    CityGraph.java
    Zone.java
  phase1/
    SignalMonitor.java
    SignalSimulator.java
    SpreadPredictor.java
    RiskHeap.java
  phase2/
    EvacuationRouter.java
    AmbulanceDispatcher.java
  phase3/
    AidDistributor.java
    AidRecord.java
    FairnessScorer.java
    HospitalAssigner.java
data/
  mock_data.csv
```

## How to Run

Compile:

```bash
javac -d out $(find src -name '*.java')
```

Run:

```bash
java -cp out Main
```

## Menu Options

- `1` - Run Phase 1: Crisis Detection
- `2` - View Final Zone Status
- `3` - View Risk Heap: Zone Rankings
- `4` - Run Phase 2: Evacuation Routing
- `5` - Run Phase 3: Fair Aid Distribution
- `6` - Demo: Ambulance Dispatcher
- `7` - Demo: Hospital Assigner
- `8` - Exit

## Team Summary

This project demonstrates how core DSA concepts can be combined into a practical smart-city emergency response system. Instead of using algorithms in isolation, the project integrates them into a realistic workflow:

- detect the crisis
- predict nearby spread
- evacuate efficiently
- distribute aid fairly
- extend toward medical response and hospital load balancing

## Tech Stack

- Java
- CSV-based simulation data
- Console-based interaction
