package Java;

public class LeakyBucket {
    private long capacity;
    private long leaksIntervalInMillis;
    private long lastLeakTimestamp;
    private long size;

    public LeakyBucket(long capacity, long leaksIntervalInMillis) {
        this.capacity = capacity;
        this.leaksIntervalInMillis = leaksIntervalInMillis;
        lastLeakTimestamp = System.currentTimeMillis();
        size = 0;
    }

    public synchronized boolean tryConsume(int drop) {
        leak();

        if (size + drop > capacity) {
            return false;
        }
        size += drop;
        return true;
    }

    private void leak() {
        long now = System.currentTimeMillis();
        long elapsedTime = now - lastLeakTimestamp;
        long numToLeak = elapsedTime / leaksIntervalInMillis;
        if (numToLeak > 0) {
            size -= Math.max(numToLeak, size);
            lastLeakTimestamp = now;
        }
    }
}
