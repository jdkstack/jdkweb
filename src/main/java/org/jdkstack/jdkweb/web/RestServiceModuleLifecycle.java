package org.jdkstack.jdkweb.web;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import org.jdkstack.jdkweb.web.annotation.GetMapping;
import org.jdkstack.jdkweb.web.annotation.PostMapping;
import org.jdkstack.jdkweb.web.annotation.RestController;
import org.jdkstack.jdkweb.web.data.ControllerMetaData;
import org.jdkstack.jdkweb.web.data.MethodMetaData;

/**
 * rest服务模块的生命周期
 *
 * @author admin
 */
public final class RestServiceModuleLifecycle extends AbstractRestServiceModuleLifecycle {

  private Map<String, String> fullyQualifiedNames = new HashMap<>();
  /**
   * 存储RestController的根URL路径和全限定名的映射,把类主动加载到就jvm中
   */
  private Map<String, Class<?>> fullyQualifiedClasses = new HashMap<>();

  public void doInit() throws Exception {
    //手动配置Rest类静态信息
    //每增加一个Rest类,都需要在这个配置
    fullyQualifiedNames.put("user", "org.jdkstack.jdkweb.web.controller.BaseController");
    fullyQualifiedNames.put("base", "org.jdkstack.jdkweb.web.controller.UserController");
    //加载Rest类到jvm,保存类元数据和方法元数据
    for (Map.Entry<String, String> entry : fullyQualifiedNames.entrySet()) {
      //Rest类简单名称(类名)
      String basePath = entry.getKey();
      //Rest类全限定名(包名+类型)
      String fullyQualifiedName = entry.getValue();
      //throw new RuntimeException("开始加载Rest Controller {} 全限定名 {}", basePath, fullyQualifiedName);
      //用Class.forName方法返回类的Class
      Class<?> classObj = classForName(fullyQualifiedName);
      //如果为空,jvm中不存在这个类
      if (classObj != null) {
        fullyQualifiedClasses.put(basePath, classObj);
      }
    }
  }

  public void doStart() {
    for (Map.Entry<String, Class<?>> entry : fullyQualifiedClasses.entrySet()) {
      //如果存在,利用反射,获取类的方法,注解,参数等数据,用Map保存起来
      //Netty Http服务拦截到客户端发起的Http请求以后,根据Http URL匹配Map中保存的数据
      //包括方法,类,参数名,参数类型等
      // 配置的basePath 和插件中的name,暂时没有使用，但是不代表以后不用，内部使用
      //Rest controller 注解 Request Mapping的值代替了。
      //String basePath = entry.getKey();
      Class<?> classObj = entry.getValue();
      //, basePath
      controllerMetaData(classObj);
    }
    //保存方法元数据
    for (Map.Entry<String, ControllerMetaData> entry : controllerMetaDataMap.entrySet()) {
      String basePath = entry.getKey();
      ControllerMetaData controllerMetaData = entry.getValue();
      Class<?> classObj = controllerMetaData.getClassObj();
      methodMetaData(classObj, basePath);
    }
    //保存参数元数据
    for (Map.Entry<String, MethodMetaData> entry : fullyPaths.entrySet()) {
      String fullPath = entry.getKey();
      MethodMetaData methodMetaData = entry.getValue();
      Method method = methodMetaData.getMethod();
      parameterMetaData(method, fullPath);
    }
  }

  /**
   * get方法路径
   *
   * @param method          方法
   * @param annotationClass 注释类
   * @return {@link String}
   */
  @Override
  public String getMethodPath(Method method, Class<? extends Annotation> annotationClass) {
    String[] methodPath = null;
    //如果Rest 方法注解是GetMapping
    if (annotationClass.equals(GetMapping.class)) {
      methodPath = method.getAnnotation(GetMapping.class).value();
    }
    if (annotationClass.equals(PostMapping.class)) {
      methodPath = method.getAnnotation(PostMapping.class).value();
    }
    return methodPath[0];
  }

  /**
   * 检查元数据(AnnotatedElement 代替 class类) 用于进行检查操作
   *
   * @param classObj 类obj
   * @return boolean
   */
  public boolean checkMetaData(AnnotatedElement classObj) {
    RestController restController = classObj.getAnnotation(RestController.class);
    if (restController == null) {
      //throw new RuntimeException("Controller没有@RestController注解,忽略加载这个类.");
      return false;
    }
    //如果是true,则正常单例加载类,如果是false,停止加载
    return restController.singleton();
  }

  /**
   * 全限定名获取Class类,用来反射获取类信息
   */
  public Class<?> classForName(String fullyQualifiedName) {
    Class<?> classObj = null;
    try {
      classObj = Class.forName(fullyQualifiedName);
    } catch (ClassNotFoundException e) {
      //throw new RuntimeException("全限定名{},没有找到类!", fullyQualifiedName);
    }
    return classObj;
  }

  public Map<String, String> getFullyQualifiedNames() {
    return fullyQualifiedNames;
  }

  public Map<String, Class<?>> getFullyQualifiedClasses() {
    return fullyQualifiedClasses;
  }


  public static void main(String[] args) throws Exception {
    RestServiceModuleLifecycle restServiceModuleLifecycle = new RestServiceModuleLifecycle();
    restServiceModuleLifecycle.doInit();
    restServiceModuleLifecycle.doStart();
  }
}
