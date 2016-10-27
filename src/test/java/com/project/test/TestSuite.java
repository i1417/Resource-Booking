package com.project.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)

@Suite.SuiteClasses({
   TestBookingsAPIController.class,
   TestResourceAPIController.class,
   TestUserAPIController.class
})
public class TestSuite {

}
