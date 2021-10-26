public class Position {
    private final int bucket;
    private final int positionInBucket;

    public Position(int bucket, int positionInBucket) {
        this.bucket = bucket;
        this.positionInBucket = positionInBucket;
    }

    @Override
    public String toString() {
        return bucket + " " + positionInBucket;
    }

}
