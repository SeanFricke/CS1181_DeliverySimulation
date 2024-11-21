package Datatypes;

/**
 * Tuple-like value pair for a train block
 */
public class TrainBlock {
    public final int blockStart, blockLength;

    /**
     * TrainBlock Constructor
     * @param blockStart Timestamp that train arrives at tracks
     * @param blockLength Length that train is on tracks
     */
    public TrainBlock(int blockStart, int blockLength) {
        this.blockStart = blockStart;
        this.blockLength = blockLength;
    }

    @Override
    public String toString() {
        return "[Start=" + blockStart + ", Length=" + blockLength + "]";
    }
}
