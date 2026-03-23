import 'package:dio/dio.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';

import '../constants/app_constants.dart';
import 'api_exception.dart';

/// Dio 客户端 Provider。
///
/// 该 Provider 属于 `core/network`，负责提供全局统一的 HTTP 客户端。
/// 当前只做最小封装：基础地址、超时和统一异常转换，后续可在这里扩展日志、鉴权头等能力。
final dioProvider = Provider<Dio>((ref) {
  final dio = Dio(
    BaseOptions(
      baseUrl: AppConstants.defaultApiBaseUrl,
      connectTimeout: const Duration(seconds: 10),
      receiveTimeout: const Duration(seconds: 10),
      sendTimeout: const Duration(seconds: 10),
      headers: <String, Object?>{
        'Content-Type': 'application/json',
      },
    ),
  );

  dio.interceptors.add(
    InterceptorsWrapper(
      onError: (error, handler) {
        handler.reject(
          DioException(
            requestOptions: error.requestOptions,
            response: error.response,
            type: error.type,
            error: ApiException.fromDioException(error),
            message: error.message,
          ),
        );
      },
    ),
  );

  return dio;
});

/// 解析后端统一响应体中的 `data` 字段。
///
/// 后端当前所有接口都包裹在 `ApiResponse<T>` 中，因此前端统一在这里解包，
/// 可以减少每个 feature API 重复写相同模板代码。
T unwrapApiData<T>(
  Response<dynamic> response,
  T Function(Object? data) parser,
) {
  final dynamic body = response.data;
  if (body is! Map<String, dynamic>) {
    throw const ApiException(message: '接口返回结构不正确');
  }

  final int? code = body['code'] is int ? body['code'] as int : null;
  if (code != 0) {
    throw ApiException(
      message: body['message']?.toString() ?? '请求失败，请稍后重试',
      statusCode: response.statusCode,
      code: code,
    );
  }

  return parser(body['data']);
}
