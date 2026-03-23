import 'package:dio/dio.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';

import '../../../../core/network/dio_client.dart';
import '../dto/current_recommendation_dto.dart';

/// progression 接口访问层。
///
/// 该类属于 `progression/data/api`，负责调用推荐训练相关接口。
class ProgressionApi {
  /// 创建 progression API。
  const ProgressionApi(this._dio);

  final Dio _dio;

  /// 调用 `GET /api/progression/programs/{programId}/current-recommendation`。
  Future<CurrentRecommendationDto?> getCurrentRecommendation(int programId) async {
    final Response<dynamic> response = await _dio.get(
      '/api/progression/programs/$programId/current-recommendation',
    );
    return unwrapApiData<CurrentRecommendationDto?>(response, (Object? data) {
      if (data == null) {
        return null;
      }
      return CurrentRecommendationDto.fromJson(data as Map<String, dynamic>);
    });
  }
}

/// progression API Provider。
final progressionApiProvider = Provider<ProgressionApi>((ref) {
  return ProgressionApi(ref.watch(dioProvider));
});
