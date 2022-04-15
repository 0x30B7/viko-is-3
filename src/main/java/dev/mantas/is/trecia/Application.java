package dev.mantas.is.trecia;

public class Application {

    public static void main(String[] args) {
        UIApplication app = new UIApplication(new BigIntegerRSA());
        app.open();
    }

}
