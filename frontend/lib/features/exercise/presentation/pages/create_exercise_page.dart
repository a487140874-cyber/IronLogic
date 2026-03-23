import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:go_router/go_router.dart';

import '../../../../app/router.dart';
import '../../../../shared/widgets/form_section.dart';
import '../../../../shared/widgets/ironlogic_scaffold.dart';
import '../../application/exercise_providers.dart';
import '../../domain/model/create_exercise_input.dart';

/// 创建动作页。
///
/// 该页面属于 `exercise/presentation`，负责承载 MVP 阶段的最小创建表单。
/// 当前故意只保留基础字段和简单校验，后续再补搜索建议、枚举选择与更细致的表单体验。
class CreateExercisePage extends ConsumerStatefulWidget {
  /// 创建动作页实例。
  const CreateExercisePage({super.key});

  @override
  ConsumerState<CreateExercisePage> createState() => _CreateExercisePageState();
}

class _CreateExercisePageState extends ConsumerState<CreateExercisePage> {
  final GlobalKey<FormState> _formKey = GlobalKey<FormState>();
  final TextEditingController _nameController = TextEditingController();
  final TextEditingController _categoryController = TextEditingController();
  final TextEditingController _primaryMuscleController = TextEditingController();
  final TextEditingController _equipmentTypeController = TextEditingController();
  final TextEditingController _movementPatternController = TextEditingController();

  @override
  void dispose() {
    _nameController.dispose();
    _categoryController.dispose();
    _primaryMuscleController.dispose();
    _equipmentTypeController.dispose();
    _movementPatternController.dispose();
    super.dispose();
  }

  /// 提交创建动作表单。
  Future<void> _submit() async {
    if (!_formKey.currentState!.validate()) {
      return;
    }

    final ExerciseCreateController controller =
        ref.read(exerciseCreateControllerProvider.notifier);
    await controller.submit(
      CreateExerciseInput(
        name: _nameController.text.trim(),
        category: _categoryController.text.trim(),
        primaryMuscle: _primaryMuscleController.text.trim(),
        equipmentType: _equipmentTypeController.text.trim(),
        movementPattern: _movementPatternController.text.trim(),
      ),
    );

    final AsyncValue<void> state = ref.read(exerciseCreateControllerProvider);
    if (!mounted) {
      return;
    }

    state.whenOrNull(
      data: (_) {
        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(content: Text('动作创建成功')),
        );
        context.go(AppRoutes.exercises);
      },
      error: (Object error, StackTrace stackTrace) {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text(error.toString())),
        );
      },
    );
  }

  @override
  Widget build(BuildContext context) {
    final AsyncValue<void> submitState = ref.watch(exerciseCreateControllerProvider);
    final bool isSubmitting = submitState.isLoading;

    return IronLogicScaffold(
      title: '创建动作',
      body: ListView(
        padding: const EdgeInsets.all(16),
        children: <Widget>[
          const Text(
            '先提供 MVP 所需的最小字段。后续可在这一页继续扩展次要肌群、元数据和更好的表单体验。',
          ),
          const SizedBox(height: 16),
          FormSection(
            child: Form(
              key: _formKey,
              child: Column(
                children: <Widget>[
                  TextFormField(
                    controller: _nameController,
                    decoration: const InputDecoration(
                      labelText: '动作名称',
                      hintText: '例如：Barbell Bench Press',
                    ),
                    validator: (String? value) {
                      if (value == null || value.trim().isEmpty) {
                        return '请输入动作名称';
                      }
                      return null;
                    },
                  ),
                  const SizedBox(height: 16),
                  TextFormField(
                    controller: _categoryController,
                    decoration: const InputDecoration(labelText: '动作分类'),
                  ),
                  const SizedBox(height: 16),
                  TextFormField(
                    controller: _primaryMuscleController,
                    decoration: const InputDecoration(labelText: '主要肌群'),
                  ),
                  const SizedBox(height: 16),
                  TextFormField(
                    controller: _equipmentTypeController,
                    decoration: const InputDecoration(labelText: '器械类型'),
                  ),
                  const SizedBox(height: 16),
                  TextFormField(
                    controller: _movementPatternController,
                    decoration: const InputDecoration(labelText: '动作模式'),
                  ),
                  const SizedBox(height: 24),
                  SizedBox(
                    width: double.infinity,
                    child: FilledButton(
                      onPressed: isSubmitting ? null : _submit,
                      child: Text(isSubmitting ? '提交中...' : '提交创建'),
                    ),
                  ),
                ],
              ),
            ),
          ),
        ],
      ),
    );
  }
}
