import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int n = scanner.nextInt();
        SellPipe[] sell_pipes = new SellPipe[n];
        for (int i = 0; i < n; i++) {
            int length, price;
            length = scanner.nextInt();
            price = scanner.nextInt();
            sell_pipes[i] = new SellPipe(length, price);
        }

        int m = scanner.nextInt();
        Pipe[] pipes = new Pipe[m];
        for (int i = 0; i < m; i++) {
            int length = scanner.nextInt();
            pipes[i] = new Pipe(length);
        }

        StrategyLogic logic = new StrategyLogic(pipes, sell_pipes);
        scanner.nextLine();
        String strategy = scanner.nextLine();

        if (strategy.contains("ekonomiczna")) {
            DPState result = logic.ekonomiczna();
            System.out.println(result.toString());
        }
        else if (strategy.contains("ekologiczna")) {
            DPState result = logic.ekologiczna();
            System.out.println(result.toString());
        }
        else if (strategy.contains("maksymalistyczna")) {
            DPState result = logic.maksymalistyczna();
            System.out.println(result.toString());
        }
        else if (strategy.contains("minimalistyczna")) {
            DPState result = logic.minimalistyczna();
            System.out.println(result.toString());
        }
        else {
            //System.out.println("No strat");
        }
    }
}
