package Java;

class TokenBucket {
    private long capacity;
    private long windowTimeInSec;
    private long lastRefillTimestamp;
    private long avaiableToken;

    public TokenBucket(long capacity, long windowTimeInSec) {
        this.capacity = capacity;
        this.windowTimeInSec = windowTimeInSec;
        lastRefillTimestamp = System.currentTimeMillis();
        avaiableToken = capacity;
    }

    public synchronized boolean tryConsume() {
        refill();
        if (avaiableToken > 0) {
            avaiableToken--;
            return true;
        } else {
            return false;
        }
    }

    private void refill() {
        long now = System.currentTimeMillis();
        long elapsedTime = now - lastRefillTimestamp;
        long tokenToAdd = (elapsedTime / 1000) * (capacity / windowTimeInSec);
        if (tokenToAdd > 0) {
            avaiableToken = Math.min(capacity, avaiableToken + tokenToAdd);
            lastRefillTimestamp = now;
        }
    }
}