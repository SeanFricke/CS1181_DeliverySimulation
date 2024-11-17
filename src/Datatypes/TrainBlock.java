package Datatypes;

public class TrainBlock {
    public final int blockStart, blockLength;

    public TrainBlock(int blockStart, int blockLength) {
        this.blockStart = blockStart;
        this.blockLength = blockLength;
    }

    public int getBlockEnd(){
        return blockStart + blockLength;
    }

    @Override
    public String toString() {
        return "[Start=" + blockStart + ", Length=" + blockLength + "]";
    }
}
