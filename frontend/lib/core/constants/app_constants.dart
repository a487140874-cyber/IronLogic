/// 应用级常量。
///
/// 该类属于 `core/constants`，用于存放不会频繁变化的基础配置。
/// 本轮只保留最少常量，避免为了“完美抽象”引入过多配置层。
abstract final class AppConstants {
  /// 应用名称。
  static const String appName = 'IronLogic';

  /// 后端基础地址。
  ///
  /// 默认指向本机 Spring Boot 服务。
  /// 如果在 Android 模拟器中运行，请通过 `--dart-define` 覆盖成 `http://10.0.2.2:8080`。
  static const String defaultApiBaseUrl = String.fromEnvironment(
    'API_BASE_URL',
    defaultValue: 'http://localhost:8080',
  );
}
