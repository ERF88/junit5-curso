package com.github.erf88.suite;

import com.github.erf88.domain.UsuarioTest;
import com.github.erf88.service.UsuarioServiceTest;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

//@Suite
@SuiteDisplayName("Suite de Tests")
//@SelectClasses(value = { UsuarioTest.class, UsuarioServiceTest.class })
@SelectPackages(value = { "com.github.erf88.service", "com.github.erf88.domain" })
public class SuiteTest {
}
