/*******************************************************************************
 *   The MIT License (MIT)
 *
 *   Copyright (c) 2014-2017 Marc de Verdelhan, 2017-2018 Ta4j Organization 
 *   & respective authors (see AUTHORS)
 *
 *   Permission is hereby granted, free of charge, to any person obtaining a copy of
 *   this software and associated documentation files (the "Software"), to deal in
 *   the Software without restriction, including without limitation the rights to
 *   use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 *   the Software, and to permit persons to whom the Software is furnished to do so,
 *   subject to the following conditions:
 *
 *   The above copyright notice and this permission notice shall be included in all
 *   copies or substantial portions of the Software.
 *
 *   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *   IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 *   FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 *   COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 *   IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 *   CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *******************************************************************************/
package org.ta4j.core.analysis.criteria;

import org.junit.Test;
import org.ta4j.core.*;
import org.ta4j.core.mocks.MockTimeSeries;
import org.ta4j.core.num.Num;

import java.util.function.Function;

import static org.junit.Assert.*;

public class NumberOfBarsCriterionTest extends AbstractCriterionTest {

    public NumberOfBarsCriterionTest(Function<Number, Num> numFunction) {
        super(numFunction);
    }

    @Test
    public void calculateWithNoTrades() {
        MockTimeSeries series = new MockTimeSeries(numFunction, 100, 105, 110, 100, 95, 105);

        AnalysisCriterion numberOfBars = new NumberOfBarsCriterion();
        assertEquals(0, (int) numberOfBars.calculate(series, new BaseTradingRecord()));
    }

    @Test
    public void calculateWithTwoTrades() {
        MockTimeSeries series = new MockTimeSeries(numFunction, 100, 105, 110, 100, 95, 105);
        TradingRecord tradingRecord = new BaseTradingRecord(
                Order.buyAt(0,series), Order.sellAt(2,series),
                Order.buyAt(3,series), Order.sellAt(5,series));

        AnalysisCriterion numberOfBars = new NumberOfBarsCriterion();
        assertEquals(6, numberOfBars.calculate(series, tradingRecord), TestUtils.GENERAL_OFFSET);
    }

    @Test
    public void calculateWithOneTrade() {
        MockTimeSeries series = new MockTimeSeries(numFunction, 100, 95, 100, 80, 85, 70);
        Trade t = new Trade(Order.buyAt(2,series), Order.sellAt(5,series));
        AnalysisCriterion numberOfBars = new NumberOfBarsCriterion();
        assertEquals(4, numberOfBars.calculate(series, t), TestUtils.GENERAL_OFFSET);
    }

    @Test
    public void betterThan() {
        AnalysisCriterion criterion = new NumberOfBarsCriterion();
        assertTrue(criterion.betterThan(3, 6));
        assertFalse(criterion.betterThan(6, 2));
    }
}
