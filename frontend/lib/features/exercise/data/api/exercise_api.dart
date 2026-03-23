import 'package:dio/dio.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';

import '../../../../core/network/dio_client.dart';
import '../dto/create_exercise_request_dto.dart';
import '../dto/exercise_dto.dart';

/// Exercise 接口访问层。
///
/// 该类属于 `exercise/data/api`，负责直接调用后端 HTTP 接口。
/// repository 会继续在其上层做 DTO 到领域模型的转换。
class ExerciseApi {
  /// 创建 Exercise API。
  const ExerciseApi(this._dio);

  final Dio _dio;

  /// 调用 `GET /api/exercises` 获取动作列表。
  Future<List<ExerciseDto>> listExercises() async {
    final Response<dynamic> response = await _dio.get('/api/exercises');
    return unwrapApiData<List<ExerciseDto>>(response, (Object? data) {
      final List<dynamic> rawList = data as List<dynamic>? ?? <dynamic>[];
      return rawList
          .map((dynamic item) => ExerciseDto.fromJson(item as Map<String, dynamic>))
          .toList();
    });
  }

  /// 调用 `POST /api/exercises` 创建动作。
  Future<ExerciseDto> createExercise(CreateExerciseRequestDto request) async {
    final Response<dynamic> response = await _dio.post(
      '/api/exercises',
      data: request.toJson(),
    );
    return unwrapApiData<ExerciseDto>(response, (Object? data) {
      return ExerciseDto.fromJson(data as Map<String, dynamic>);
    });
  }
}

/// Exercise API Provider。
final exerciseApiProvider = Provider<ExerciseApi>((ref) {
  return ExerciseApi(ref.watch(dioProvider));
});
