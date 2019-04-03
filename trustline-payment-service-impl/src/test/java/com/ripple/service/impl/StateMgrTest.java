package com.ripple.service.impl;

import com.ripple.service.impl.localstate.StateMgr;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.math.BigDecimal;

import static com.ripple.service.impl.localstate.StateMgr.ChangeType.CREDIT;
import static com.ripple.service.impl.localstate.StateMgr.ChangeType.DEBIT;

/**
 * @author rpurigella
 */
public class StateMgrTest {

    private StateMgr stateMgr;

    @BeforeClass
    public void init() {
        stateMgr = new StateMgr();
    }

    @Test
    public void test_Credit_Debit() {
        BigDecimal expected;
        BigDecimal value;

        expected = BigDecimal.ZERO;
        Assert.assertEquals(stateMgr.getBalance(), expected);

        value = new BigDecimal(10.50);
        stateMgr.changeState(value, CREDIT);
        expected = expected.subtract(value);
        Assert.assertEquals(stateMgr.getBalance(), expected);

        value = new BigDecimal(10);
        stateMgr.changeState(value, CREDIT);
        expected = expected.subtract(value);
        Assert.assertEquals(stateMgr.getBalance(), expected);

        value = new BigDecimal(9.50);
        stateMgr.changeState(value, DEBIT);
        expected = expected.add(value);
        Assert.assertEquals(stateMgr.getBalance(), expected);
    }

}

