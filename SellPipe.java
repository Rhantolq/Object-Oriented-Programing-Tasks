public class SellPipe extends Pipe {
    private int price;

    public SellPipe(int length, int price) {
        super(length);
        this.price = price;
    }

    public int getPrice() {
        return price;
    }
}
