public class DividedPipe {
    private int pipe;
    private int[] dividers;

    public DividedPipe(Pipe pipe, int[] dividers) {
        this.pipe = pipe.getLength();
        this.dividers = dividers;
    }

    public DividedPipe() {}

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        string.append(pipe);
        for (int divisor : dividers) {
            string.append(" ");
            string.append(divisor);
        }
        return string.toString();
    }
}
