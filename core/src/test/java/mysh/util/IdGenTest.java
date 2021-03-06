package mysh.util;

import org.junit.Ignore;
import org.junit.Test;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.LongAdder;


/**
 * IdGenTest
 *
 * @since 2018/04/04
 */
public class IdGenTest {
	
	@Test
	@Ignore
	public void gen() throws InterruptedException {
		final Set<Long> m = Collections.newSetFromMap(new ConcurrentHashMap<>(8_000_000));
		int n = 7;
		ExecutorService e = Executors.newFixedThreadPool(n);
		
		final LongAdder total = new LongAdder();
		final LongAdder conflict = new LongAdder();
		while (n-- > 0) {
			e.execute(() -> {
				while (true) {
					long id = IdGen.timeBasedDistId(3);
					if (!m.add(id)) {
						// System.out.println("conflict " + id);
						conflict.increment();
					}
//					Thread.yield();
					total.increment();
				}
			});
		}
		Thread.sleep(5000);
		e.shutdownNow();
		
		System.out.println(String.format("result: %d/%d, %f", conflict.longValue(), total.longValue(),
				conflict.longValue() * 1.0 / total.longValue()));
		System.exit(0);
	}
}
