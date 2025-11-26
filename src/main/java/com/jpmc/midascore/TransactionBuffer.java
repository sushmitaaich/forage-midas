package com.jpmc.midascore;

import com.jpmc.midascore.foundation.Transaction;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

/**
 * Simple thread-safe FIFO buffer used to store Transactions received by the listener.
 */
@Component
public class TransactionBuffer {
    private final ConcurrentLinkedQueue<Transaction> queue = new ConcurrentLinkedQueue<>();

    public void add(Transaction tx) {
        if (tx != null) queue.add(tx);
    }

    public List<Transaction> firstN(int n) {
        List<Transaction> tmp = new ArrayList<>(n);
        int i = 0;
        for (Transaction t : queue) {
            tmp.add(t);
            if (++i >= n) break;
        }
        return Collections.unmodifiableList(tmp);
    }

    public List<Transaction> all() {
        return queue.stream().collect(Collectors.toList());
    }

    public void clear() {
        queue.clear();
    }
}
