

class Event {
    enum EventType { ARRIVAL, COMPLETION , PREEMPTION}
    int time;
    EventType type;
    Process process;

    public Event(int time, EventType type, Process process) {
        this.time = time;
        this.type = type;
        this.process = process;
    }
}
