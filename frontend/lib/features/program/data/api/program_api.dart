import 'package:dio/dio.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';

import '../../../../core/network/dio_client.dart';
import '../dto/create_program_request_dto.dart';
import '../dto/program_dto.dart';

/// Program 接口访问层。
///
/// 该类属于 `program/data/api`，只负责发起 HTTP 请求与解析原始响应体。
class ProgramApi {
  /// 创建 Program API。
  const ProgramApi(this._dio);

  final Dio _dio;

  /// 调用 `GET /api/programs` 获取 Program 列表。
  Future<List<ProgramDto>> listPrograms() async {
    final Response<dynamic> response = await _dio.get('/api/programs');
    return unwrapApiData<List<ProgramDto>>(response, (Object? data) {
      final List<dynamic> rawList = data as List<dynamic>? ?? <dynamic>[];
      return rawList
          .map((dynamic item) => ProgramDto.fromJson(item as Map<String, dynamic>))
          .toList();
    });
  }

  /// 调用 `POST /api/programs` 创建 Program。
  Future<ProgramDto> createProgram(CreateProgramRequestDto request) async {
    final Response<dynamic> response = await _dio.post(
      '/api/programs',
      data: request.toJson(),
    );
    return unwrapApiData<ProgramDto>(response, (Object? data) {
      return ProgramDto.fromJson(data as Map<String, dynamic>);
    });
  }
}

/// Program API Provider。
final programApiProvider = Provider<ProgramApi>((ref) {
  return ProgramApi(ref.watch(dioProvider));
});
