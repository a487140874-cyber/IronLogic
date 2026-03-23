import 'package:dio/dio.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';

import '../../../../core/network/dio_client.dart';
import '../dto/save_workout_sets_request_dto.dart';
import '../dto/workout_detail_dto.dart';
import '../dto/workout_history_item_dto.dart';
import '../dto/workout_set_dto.dart';

/// workout 接口访问层。
///
/// 该类属于 `workout/data/api`，直接负责调用训练执行相关接口。
class WorkoutApi {
  /// 创建 workout API。
  const WorkoutApi(this._dio);

  final Dio _dio;

  /// 调用从模板开始训练接口。
  Future<WorkoutDetailDto> startWorkoutFromTemplate(int templateId) async {
    final Response<dynamic> response = await _dio.post(
      '/api/workouts/from-template/$templateId',
    );
    return unwrapApiData<WorkoutDetailDto>(response, (Object? data) {
      return WorkoutDetailDto.fromJson(data as Map<String, dynamic>);
    });
  }

  /// 调用训练详情接口。
  Future<WorkoutDetailDto> getWorkoutDetail(int workoutId) async {
    final Response<dynamic> response = await _dio.get('/api/workouts/$workoutId');
    return unwrapApiData<WorkoutDetailDto>(response, (Object? data) {
      return WorkoutDetailDto.fromJson(data as Map<String, dynamic>);
    });
  }

  /// 调用整列表覆盖保存 sets 接口。
  Future<List<WorkoutSetDto>> saveWorkoutSets(
    int workoutExerciseId,
    SaveWorkoutSetsRequestDto request,
  ) async {
    final Response<dynamic> response = await _dio.put(
      '/api/workout-exercises/$workoutExerciseId/sets',
      data: request.toJson(),
    );
    return unwrapApiData<List<WorkoutSetDto>>(response, (Object? data) {
      final List<dynamic> rawList = data as List<dynamic>? ?? <dynamic>[];
      return rawList
          .map((dynamic item) => WorkoutSetDto.fromJson(item as Map<String, dynamic>))
          .toList();
    });
  }

  /// 调用完成训练接口。
  Future<WorkoutDetailDto> finishWorkout(int workoutId) async {
    final Response<dynamic> response =
        await _dio.post('/api/workouts/$workoutId/finish');
    return unwrapApiData<WorkoutDetailDto>(response, (Object? data) {
      return WorkoutDetailDto.fromJson(data as Map<String, dynamic>);
    });
  }

  /// 调用训练历史接口。
  Future<List<WorkoutHistoryItemDto>> listWorkoutHistory() async {
    final Response<dynamic> response = await _dio.get('/api/workouts/history');
    return unwrapApiData<List<WorkoutHistoryItemDto>>(response, (Object? data) {
      final List<dynamic> rawList = data as List<dynamic>? ?? <dynamic>[];
      return rawList
          .map(
            (dynamic item) =>
                WorkoutHistoryItemDto.fromJson(item as Map<String, dynamic>),
          )
          .toList();
    });
  }
}

/// workout API Provider。
final workoutApiProvider = Provider<WorkoutApi>((ref) {
  return WorkoutApi(ref.watch(dioProvider));
});
