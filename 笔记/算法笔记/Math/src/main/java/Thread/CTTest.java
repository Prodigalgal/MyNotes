package Thread;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class CTTest {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CompletableFuture<String> a = CompletableFuture.completedFuture("A").thenApply(String::toLowerCase);
        String s = a.get();
        System.out.println(s);
    }
}
