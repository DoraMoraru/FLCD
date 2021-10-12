public class Position {
    private int bucket;
    private int positionInBucket;

    public Position(int bucket, int positionInBucket) {
        this.bucket = bucket;
        this.positionInBucket = positionInBucket;
    }

    @Override
    public String toString() {
        return bucket + " " + positionInBucket;
    }

}
