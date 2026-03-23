import 'package:flutter/material.dart';

/// 通用加载组件。
///
/// 该组件属于 `shared/widgets`，用于统一核心页面的加载状态展示。
/// MVP 阶段先保持简单，确保用户能明确感知当前正在请求数据。
class LoadingView extends StatelessWidget {
  /// 创建通用加载组件。
  const LoadingView({super.key, this.message = '加载中...'});

  /// 加载提示文案。
  final String message;

  @override
  Widget build(BuildContext context) {
    return Center(
      child: Column(
        mainAxisSize: MainAxisSize.min,
        children: <Widget>[
          const CircularProgressIndicator(),
          const SizedBox(height: 12),
          Text(message),
        ],
      ),
    );
  }
}
