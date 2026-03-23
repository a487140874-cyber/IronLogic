import 'package:dio/dio.dart';

/// 前端统一 API 异常。
///
/// 该类属于 `core/network`，用于把 Dio 原始异常转换成页面更容易消费的错误对象。
/// 这样 feature 层无需反复解析响应结构，可以更专注业务状态本身。
class ApiException implements Exception {
  /// 创建统一 API 异常。
  const ApiException({
    required this.message,
    this.statusCode,
    this.code,
  });

  /// 面向页面展示的错误信息。
  final String message;

  /// HTTP 状态码。
  final int? statusCode;

  /// 后端业务码。
  final int? code;

  /// 从 Dio 异常中提取更稳定的前端错误信息。
  static ApiException fromDioException(DioException exception) {
    final Object? payload = exception.response?.data;
    if (payload is Map<String, dynamic>) {
      return ApiException(
        message: payload['message']?.toString() ?? '请求失败，请稍后重试',
        statusCode: exception.response?.statusCode,
        code: payload['code'] is int ? payload['code'] as int : null,
      );
    }

    return ApiException(
      message: exception.message ?? '网络请求失败，请稍后重试',
      statusCode: exception.response?.statusCode,
    );
  }

  @override
  String toString() => message;
}
