package code;

import com.blade.Blade;

public class Main {
    public static void main(String[] args) {
        Blade.of()
                .start(Main.class, args);
    }
}