package org.jdkstack.jdkweb.examples;

import org.jdkstack.jdkweb.api.context.Context;
import org.jdkstack.jdkweb.web.annotation.ComponentScan;
import org.jdkstack.jdkweb.web.annotation.Filter;
import org.jdkstack.jdkweb.web.application.Application;
import org.jdkstack.jdkweb.web.context.ApplicationContext;

/**
 * 启动类,扫描要管理的Bean.
 *
 * <p>Another description after blank line.
 *
 * @author admin
 */
@ComponentScan(
    value = {
        "org.jdkstack.jdkweb.examples.controller.BaseController",
        "org.jdkstack.jdkweb.examples.controller.UserController"},
    excludeFilters = {@Filter(String.class), @Filter(String.class)}
)
public final class StartApplication {

  /** 上下文环境. */
  private static final Context CONTEXT = new ApplicationContext();

  private StartApplication() {
    //
  }

  /**
   * This is a method description.
   *
   * <p>Another description after blank line.
   *
   * @return context.
   * @author admin
   */
  public static Context context() {
    return CONTEXT;
  }

  static {
    Application.run(StartApplication.class, CONTEXT);
  }
}
