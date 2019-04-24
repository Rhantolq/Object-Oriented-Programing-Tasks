import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;

public class StrategyLogic {
    private Pipe[] pipes;
    private SellPipe[] sell_pipes;
    private HashMap<BitSet, DPState> hashmap;

    public StrategyLogic(Pipe[] pipes, SellPipe[] sell_pipes) {
        this.pipes = pipes;
        this.sell_pipes = sell_pipes;
    }

    // Recursive method that calculates the most optimal solution for splitting pipes
    // if [compareByCost] is set to true then calculated solution minimizes the
    // cost of buying pipes, and if false then the calculated solution minimizes
    // the length of pipe waste
    // This function uses the dynamic programming approach and stores previously
    // calculated states
    private DPState calculateState(BitSet bitset, boolean compareByCost) {
        long total_size = 0;
        int iter = 0;
        int[] current_pipe_sizes = new int[bitset.cardinality()];

        for (int i = 0; i < pipes.length; i++) {
            if (bitset.get(i)) {
                total_size += pipes[i].getLength();
                current_pipe_sizes[iter++] = pipes[i].getLength();
            }
        }

        DPState best = new DPState(Long.MAX_VALUE, Long.MAX_VALUE, new DividedPipe());
        for (SellPipe sell_pipe : sell_pipes) {
            if (sell_pipe.getLength() >= total_size) {
                if (compareByCost) {
                    if (sell_pipe.getPrice() < best.getTotalCost()) {
                        best = new DPState(sell_pipe.getPrice(), sell_pipe.getLength() - total_size,
                                new DividedPipe(sell_pipe, current_pipe_sizes));
                    }
                }
                else {
                    if (sell_pipe.getLength() - total_size < best.getWasteLength()) {
                        best = new DPState(sell_pipe.getPrice(), sell_pipe.getLength() - total_size,
                                new DividedPipe(sell_pipe, current_pipe_sizes));
                    }
                }
            }
        }

        BitSet lookup_set = new BitSet(bitset.size());
        BitSet iter_set = new BitSet(bitset.cardinality());

        iter_set.set(0);

        while (iter_set.cardinality() < bitset.cardinality()) {
            lookup_set.clear();
            int j = 0;
            for (int i = 0; i < lookup_set.size(); i++) {
                if (bitset.get(i)) {
                    if (iter_set.get(j++)) {
                        lookup_set.set(i);
                    }
                }
            }

            BitSet lookup_set2 = (BitSet)lookup_set.clone();
            lookup_set2.xor(bitset);

            DPState substate1 = hashmap.containsKey(lookup_set)
                    ? hashmap.get(lookup_set) : calculateState(lookup_set, compareByCost);
            DPState substate2 = hashmap.containsKey(lookup_set2)
                    ? hashmap.get(lookup_set2) : calculateState(lookup_set2, compareByCost);

            DPState compared = substate1.add(substate2);
            if (compareByCost) {
                if (compared.compareToByCost(best) < 0) {
                    best = compared;
                }
            }
            else {
                if (compared.compareToByWaste(best) < 0) {
                    best = compared;
                }
            }

            int pos = iter_set.nextClearBit(0);
            iter_set.flip(0, pos);
            iter_set.set(pos);
        }

        hashmap.put(bitset, best);
        return best;
    }

    // assumes that pipes and sell_pipes are sorted
    private DPState greedy(boolean choose_max) {
        DPState final_state = new DPState();
        BitSet used = new BitSet(pipes.length);
        used.flip(0, pipes.length);

        while (used.cardinality() > 0) {
            int index = used.previousSetBit(pipes.length);
            SellPipe chosen_pipe = new SellPipe(Integer.MAX_VALUE, Integer.MAX_VALUE);
            if (choose_max) {
                chosen_pipe = sell_pipes[sell_pipes.length - 1];
            }
            else {
                for (int j = 0; j < sell_pipes.length; j++) {
                    if (sell_pipes[j].getLength() >= pipes[index].getLength()) {
                        chosen_pipe = sell_pipes[j];
                        break;
                    }
                }
            }

            ArrayList<Pipe> dividers = new ArrayList<>();

            long current_size = pipes[index].getLength();
            dividers.add(pipes[index]);
            used.clear(index);
            index = used.previousSetBit(index - 1);

            while (index >= 0) {
                if (pipes[index].getLength() + current_size <= chosen_pipe.getLength()) {
                    current_size += pipes[index].getLength();
                    dividers.add(pipes[index]);
                    used.clear(index);
                }
                index = used.previousSetBit(index - 1);
            }

            int[] lengths = new int[dividers.size()];
            for (int i = 0; i < dividers.size(); i++) {
                lengths[i] = dividers.get(i).getLength();
            }
            DPState this_state = new DPState(
                    chosen_pipe.getPrice(),
                    chosen_pipe.getLength() -  current_size,
                    new DividedPipe(chosen_pipe, lengths)
            );
            final_state = final_state.add(this_state);
        }
        return final_state;
    }

    public DPState ekonomiczna() {
        hashmap = new HashMap<BitSet, DPState>();
        BitSet target = new BitSet(pipes.length);
        hashmap.put(target, new DPState());
        target.flip(0, pipes.length);
        return calculateState(target, true);
    }

    public DPState ekologiczna() {
        hashmap = new HashMap<BitSet, DPState>();
        BitSet target = new BitSet(pipes.length);
        hashmap.put(target, new DPState());
        target.flip(0, pipes.length);
        return calculateState(target, false);
    }

    public DPState minimalistyczna() {
        return greedy(false);
    }

    public DPState maksymalistyczna() {
        return greedy(true);
    }
}
