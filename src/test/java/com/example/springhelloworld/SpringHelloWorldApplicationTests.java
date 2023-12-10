package com.example.springhelloworld;

import com.example.springhelloworld.domain.BinanceAvgPriceStreamMessage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import java.util.function.Function;

@SpringBootTest
class SpringHelloWorldApplicationTests {

	@Autowired
	ApplicationContext ctx;

	@Autowired
	Function<String, BinanceAvgPriceStreamMessage> textToPrice;

	@Test
	void contextLoads() {
		var priceFetcher = ctx.getBean(SymbolPriceFetcher.class);
		priceFetcher.startPriceStream(false);
	}

	@Test
	void converterWorks(){
		var price = textToPrice.apply("""
    {"e":"avgPrice","E":1702246092670,"s":"BTCUSDT","i":"5m","w":"43822.0","T":1702246092669}
				""");
		Assertions.assertNotNull(price);
		Assertions.assertEquals(43822.0, price.getW());
	}

}
