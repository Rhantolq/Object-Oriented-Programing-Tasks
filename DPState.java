public class DPState {
    private DividedPipe[] div_pipes;
    private long tot_cost, waste_length;

    public DPState() {
        this.tot_cost = 0;
        this.waste_length = 0;
        this.div_pipes = new DividedPipe[0];
    }

    public DPState(long tot_cost, long waste_length, DividedPipe pipe) {
        this.tot_cost = tot_cost;
        this.waste_length = waste_length;
        this.div_pipes = new DividedPipe[1];
        this.div_pipes[0] = pipe;
    }

    public DPState(long tot_cost, long waste_length, DividedPipe[] pipes) {
        this.tot_cost = tot_cost;
        this.waste_length = waste_length;
        this.div_pipes = pipes;
    }

    public DividedPipe[] getDivPipes() {
        return div_pipes;
    }

    public long getTotalCost() {
        return tot_cost;
    }

    public long getWasteLength() {
        return waste_length;
    }

    public DPState add(DPState state) {
        DividedPipe[] second = state.getDivPipes();
        DividedPipe[] new_div_pipes = new DividedPipe[div_pipes.length + second.length];

        System.arraycopy(div_pipes, 0, new_div_pipes, 0, div_pipes.length);
        System.arraycopy(second, 0, new_div_pipes, div_pipes.length, second.length);

        long new_tot_cost = tot_cost + state.getTotalCost();
        long new_waste_length = waste_length + state.getWasteLength();
        return new DPState(new_tot_cost, new_waste_length, new_div_pipes);
    }

    public int compareToByCost(DPState state) {
        return Long.compare(tot_cost, state.getTotalCost());
    }

    public int compareToByWaste(DPState state) {
        return Long.compare(waste_length, state.getWasteLength());
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        string.append(tot_cost);
        string.append("\n");
        string.append(waste_length);
        string.append("\n");
        for (DividedPipe div_pipe : div_pipes) {
            string.append(div_pipe.toString());
            string.append("\n");
        }
        return string.toString();
    }
}
