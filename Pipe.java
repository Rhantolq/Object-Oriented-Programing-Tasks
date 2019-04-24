public class Pipe {
    private int length;

    public Pipe(int length) {
        this.length = length;
    }

    public int getLength() {
        return length;
    }

    @Override
    public  String toString() {
        return String.valueOf(length);
    }
}
