package org.jdkstack.jdkweb.web.controller;

import java.util.HashMap;
import java.util.Map;
import org.jdkstack.jdkweb.web.annotation.GetMapping;
import org.jdkstack.jdkweb.web.annotation.RequestMapping;
import org.jdkstack.jdkweb.web.annotation.RequestParam;
import org.jdkstack.jdkweb.web.annotation.RestController;

/**
 * 基本控制器 内置的Rest服务
 *
 * @author admin
 */
@RestController(singleton = true)
@RequestMapping("/base")
public class BaseController {

  @GetMapping("/get")
  public Object listUser(
      @RequestParam("recipient") final String recipient,
      @RequestParam("x") final String x,
      @RequestParam("y") final String y) {
    final Map<String, Object> data = new HashMap<>();
    data.put("id", 100);
    data.put("name", "name" + recipient + x + y);
    return data;
  }
}
