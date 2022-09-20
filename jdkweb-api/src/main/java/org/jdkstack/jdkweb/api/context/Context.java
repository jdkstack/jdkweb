package org.jdkstack.jdkweb.api.context;

import java.util.List;
import java.util.Map;
import org.jdkstack.jdkweb.api.data.ControllerMetaData;
import org.jdkstack.jdkweb.api.data.MethodMetaData;
import org.jdkstack.jdkweb.api.data.ParameterMetaData;

public interface Context {

  void scan(Class<?> application);

  Map<String, List<ParameterMetaData>> getFullyPathsParams();

  void setFullyPathsParams(Map<String, List<ParameterMetaData>> fullyPathsParams);

  Map<String, MethodMetaData> getFullyPaths();

  void setFullyPaths(Map<String, MethodMetaData> fullyPaths);

  Map<String, ControllerMetaData> getControllerMetaDataMap();

  void setControllerMetaDataMap(Map<String, ControllerMetaData> controllerMetaDataMap);

  Map<String, Class<?>> getFullyQualifiedClasses();

  void setFullyQualifiedClasses(Map<String, Class<?>> fullyQualifiedClasses);
}
