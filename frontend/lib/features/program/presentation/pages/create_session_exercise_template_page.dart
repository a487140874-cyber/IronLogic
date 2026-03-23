import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:go_router/go_router.dart';

import '../../../../app/router.dart';
import '../../../../shared/widgets/error_view.dart';
import '../../../../shared/widgets/form_section.dart';
import '../../../../shared/widgets/ironlogic_scaffold.dart';
import '../../../../shared/widgets/loading_view.dart';
import '../../../exercise/application/exercise_providers.dart';
import '../../../exercise/domain/model/exercise.dart';
import '../../application/program_providers.dart';
import '../../domain/model/create_session_exercise_template_input.dart';

/// 创建 SessionExerciseTemplate 页。
///
/// 该页面属于 `program/presentation`，用于往某个 SessionTemplate 中添加动作模板。
/// 当前故意只做简单下拉选择，不实现复杂搜索组件。
class CreateSessionExerciseTemplatePage extends ConsumerStatefulWidget {
  /// 创建页面。
  const CreateSessionExerciseTemplatePage({
    required this.templateId,
    super.key,
  });

  /// 所属模板 id。
  final int templateId;

  @override
  ConsumerState<CreateSessionExerciseTemplatePage> createState() =>
      _CreateSessionExerciseTemplatePageState();
}

class _CreateSessionExerciseTemplatePageState
    extends ConsumerState<CreateSessionExerciseTemplatePage> {
  final GlobalKey<FormState> _formKey = GlobalKey<FormState>();
  final TextEditingController _orderNoController = TextEditingController(text: '1');
  final TextEditingController _targetSetsController = TextEditingController(text: '3');
  final TextEditingController _targetRepsController = TextEditingController();
  final TextEditingController _targetWeightController = TextEditingController();
  final TextEditingController _targetWeightUnitController =
      TextEditingController(text: 'kg');
  final TextEditingController _restSecondsController = TextEditingController();
  final TextEditingController _intensityModeController = TextEditingController();

  int? _selectedExerciseId;

  @override
  void dispose() {
    _orderNoController.dispose();
    _targetSetsController.dispose();
    _targetRepsController.dispose();
    _targetWeightController.dispose();
    _targetWeightUnitController.dispose();
    _restSecondsController.dispose();
    _intensityModeController.dispose();
    super.dispose();
  }

  /// 提交模板动作创建请求。
  Future<void> _submit() async {
    if (!_formKey.currentState!.validate() || _selectedExerciseId == null) {
      if (_selectedExerciseId == null) {
        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(content: Text('请选择动作')),
        );
      }
      return;
    }

    final SessionExerciseTemplateCreateController controller = ref.read(
      sessionExerciseTemplateCreateControllerProvider(widget.templateId).notifier,
    );
    await controller.submit(
      CreateSessionExerciseTemplateInput(
        exerciseId: _selectedExerciseId!,
        orderNo: int.parse(_orderNoController.text.trim()),
        targetSets: int.parse(_targetSetsController.text.trim()),
        targetReps: int.tryParse(_targetRepsController.text.trim()),
        targetWeight: double.tryParse(_targetWeightController.text.trim()),
        targetWeightUnit: _targetWeightUnitController.text.trim().isEmpty
            ? null
            : _targetWeightUnitController.text.trim(),
        restSeconds: int.tryParse(_restSecondsController.text.trim()),
        intensityMode: _intensityModeController.text.trim().isEmpty
            ? null
            : _intensityModeController.text.trim(),
      ),
    );

    final AsyncValue<void> state = ref.read(
      sessionExerciseTemplateCreateControllerProvider(widget.templateId),
    );
    if (!mounted) {
      return;
    }

    state.whenOrNull(
      data: (_) {
        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(content: Text('模板动作添加成功')),
        );
        context.go(AppRoutes.sessionTemplateDetail(widget.templateId));
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
    final AsyncValue<List<Exercise>> exercisesAsync = ref.watch(exerciseListProvider);
    final AsyncValue<void> submitState = ref.watch(
      sessionExerciseTemplateCreateControllerProvider(widget.templateId),
    );
    final bool isSubmitting = submitState.isLoading;

    return IronLogicScaffold(
      title: '添加模板动作',
      body: exercisesAsync.when(
        loading: () => const LoadingView(message: '正在加载动作列表...'),
        error: (Object error, StackTrace stackTrace) {
          return ErrorView(
            message: error.toString(),
            onRetry: () => ref.invalidate(exerciseListProvider),
          );
        },
        data: (List<Exercise> exercises) {
          return ListView(
            padding: const EdgeInsets.all(16),
            children: <Widget>[
              const Text('MVP 阶段先使用简单下拉选择动作。后续可补搜索、筛选与更强的处方编辑体验。'),
              const SizedBox(height: 16),
              FormSection(
                child: Form(
                  key: _formKey,
                  child: Column(
                    children: <Widget>[
                      DropdownButtonFormField<int>(
                        value: _selectedExerciseId,
                        decoration: const InputDecoration(labelText: '动作'),
                        items: exercises
                            .map(
                              (Exercise exercise) => DropdownMenuItem<int>(
                                value: exercise.id,
                                child: Text(exercise.name),
                              ),
                            )
                            .toList(),
                        onChanged: (int? value) {
                          setState(() => _selectedExerciseId = value);
                        },
                      ),
                      const SizedBox(height: 16),
                      TextFormField(
                        controller: _orderNoController,
                        keyboardType: TextInputType.number,
                        decoration: const InputDecoration(labelText: '顺序号'),
                        validator: (String? value) =>
                            int.tryParse(value ?? '') == null ? '请输入有效顺序号' : null,
                      ),
                      const SizedBox(height: 16),
                      TextFormField(
                        controller: _targetSetsController,
                        keyboardType: TextInputType.number,
                        decoration: const InputDecoration(labelText: '目标组数'),
                        validator: (String? value) =>
                            int.tryParse(value ?? '') == null ? '请输入有效组数' : null,
                      ),
                      const SizedBox(height: 16),
                      TextFormField(
                        controller: _targetRepsController,
                        keyboardType: TextInputType.number,
                        decoration: const InputDecoration(labelText: '目标次数'),
                      ),
                      const SizedBox(height: 16),
                      TextFormField(
                        controller: _targetWeightController,
                        keyboardType: const TextInputType.numberWithOptions(decimal: true),
                        decoration: const InputDecoration(labelText: '目标重量'),
                      ),
                      const SizedBox(height: 16),
                      TextFormField(
                        controller: _targetWeightUnitController,
                        decoration: const InputDecoration(labelText: '重量单位'),
                      ),
                      const SizedBox(height: 16),
                      TextFormField(
                        controller: _restSecondsController,
                        keyboardType: TextInputType.number,
                        decoration: const InputDecoration(labelText: '休息秒数'),
                      ),
                      const SizedBox(height: 16),
                      TextFormField(
                        controller: _intensityModeController,
                        decoration: const InputDecoration(labelText: '强度模式'),
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
          );
        },
      ),
    );
  }
}
