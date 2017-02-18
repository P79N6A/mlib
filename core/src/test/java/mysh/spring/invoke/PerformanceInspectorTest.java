package mysh.spring.invoke;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * @author zhangzhixian<hzzhangzhixian@corp.netease.com>
 * @since 2017/2/17
 */
@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@Component("PerformanceInspectorTest")
public class PerformanceInspectorTest  {

	@Resource(name = "PerformanceInspectorTest")
	PerformanceInspectorTest self;

	@Test
	public void test() {
		self.f();
	}

	public void f() {
		self.a();
		self.b();
		self.c();
	}

	public void a() {
		self.b();
	}

	public void b() {
		self.c();
	}

	public void c() {
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
