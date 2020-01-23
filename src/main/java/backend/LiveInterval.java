package backend;

class LiveInterval {
    String name;
    Integer startpoint, endpoint, length;

    LiveInterval(String name, Integer startpoint) {
        this.name = name;
        this.startpoint = startpoint;
        this.endpoint = this.startpoint;
        this.length = 0;
    }

    void update(Integer point) {
        this.endpoint = point;
        this.length = this.endpoint - this.startpoint;
    }
}