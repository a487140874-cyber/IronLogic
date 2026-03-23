import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:go_router/go_router.dart';

import '../../../../app/router.dart';
import '../../../../core/utils/date_utils.dart';
import '../../../../shared/widgets/form_section.dart';
import '../../../../shared/widgets/ironlogic_scaffold.dart';
import '../../application/program_providers.dart';
import '../../domain/model/create_program_input.dart';

/// 创建 Program 页。
///
/// 该页面属于 `program/presentation`，承担 Program 最小创建流程。
/// 本轮先把字段提交跑通，后续再扩展 block、session template 等深层编辑能力。
class CreateProgramPage extends ConsumerStatefulWidget {
  /// 创建 Program 页实例。
  const CreateProgramPage({super.key});

  @override
  ConsumerState<CreateProgramPage> createState() => _CreateProgramPageState();
}

class _CreateProgramPageState extends ConsumerState<CreateProgramPage> {
  static const List<String> _goalTypes = <String>[
    'HYPERTROPHY',
    'STRENGTH',
    'GENERAL_FITNESS',
  ];

  static const List<String> _statuses = <String>[
    'DRAFT',
    'ACTIVE',
    'ARCHIVED',
  ];

  final GlobalKey<FormState> _formKey = GlobalKey<FormState>();
  final TextEditingController _nameController = TextEditingController();
  final TextEditingController _descriptionController = TextEditingController();
  final TextEditingController _startDateController = TextEditingController();
  final TextEditingController _endDateController = TextEditingController();

  String _goalType = _goalTypes.first;
  String _status = _statuses.first;

  @override
  void dispose() {
    _nameController.dispose();
    _descriptionController.dispose();
    _startDateController.dispose();
    _endDateController.dispose();
    super.dispose();
  }

  /// 选择日期并写回文本框。
  Future<void> _pickDate(TextEditingController controller) async {
    final DateTime now = DateTime.now();
    final DateTime? picked = await showDatePicker(
      context: context,
      initialDate: now,
      firstDate: DateTime(now.year - 2),
      lastDate: DateTime(now.year + 5),
    );

    if (picked != null) {
      controller.text = AppDateUtils.toDateInput(picked);
    }
  }

  /// 提交创建 Program 表单。
  Future<void> _submit() async {
    if (!_formKey.currentState!.validate()) {
      return;
    }

    final ProgramCreateController controller =
        ref.read(programCreateControllerProvider.notifier);
    await controller.submit(
      CreateProgramInput(
        name: _nameController.text.trim(),
        goalType: _goalType,
        status: _status,
        description: _descriptionController.text.trim().isEmpty
            ? null
            : _descriptionController.text.trim(),
        startDate: _startDateController.text.trim().isEmpty
            ? null
            : _startDateController.text.trim(),
        endDate: _endDateController.text.trim().isEmpty
            ? null
            : _endDateController.text.trim(),
      ),
    );

    final AsyncValue<void> state = ref.read(programCreateControllerProvider);
    if (!mounted) {
      return;
    }

    state.whenOrNull(
      data: (_) {
        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(content: Text('Program 创建成功')),
        );
        context.go(AppRoutes.programs);
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
    final AsyncValue<void> submitState = ref.watch(programCreateControllerProvider);
    final bool isSubmitting = submitState.isLoading;

    return IronLogicScaffold(
      title: '创建 Program',
      body: ListView(
        padding: const EdgeInsets.all(16),
        children: <Widget>[
          const Text(
            '本页先只支持 Program 的基础字段创建。Block、SessionTemplate 和更复杂的编排将在后续轮次补齐。',
          ),
          const SizedBox(height: 16),
          FormSection(
            child: Form(
              key: _formKey,
              child: Column(
                children: <Widget>[
                  TextFormField(
                    controller: _nameController,
                    decoration: const InputDecoration(labelText: 'Program 名称'),
                    validator: (String? value) {
                      if (value == null || value.trim().isEmpty) {
                        return '请输入 Program 名称';
                      }
                      return null;
                    },
                  ),
                  const SizedBox(height: 16),
                  DropdownButtonFormField<String>(
                    value: _goalType,
                    decoration: const InputDecoration(labelText: '目标类型'),
                    items: _goalTypes
                        .map(
                          (String value) => DropdownMenuItem<String>(
                            value: value,
                            child: Text(value),
                          ),
                        )
                        .toList(),
                    onChanged: (String? value) {
                      if (value != null) {
                        setState(() {
                          _goalType = value;
                        });
                      }
                    },
                  ),
                  const SizedBox(height: 16),
                  DropdownButtonFormField<String>(
                    value: _status,
                    decoration: const InputDecoration(labelText: '状态'),
                    items: _statuses
                        .map(
                          (String value) => DropdownMenuItem<String>(
                            value: value,
                            child: Text(value),
                          ),
                        )
                        .toList(),
                    onChanged: (String? value) {
                      if (value != null) {
                        setState(() {
                          _status = value;
                        });
                      }
                    },
                  ),
                  const SizedBox(height: 16),
                  TextFormField(
                    controller: _descriptionController,
                    decoration: const InputDecoration(labelText: '描述'),
                    maxLines: 3,
                  ),
                  const SizedBox(height: 16),
                  TextFormField(
                    controller: _startDateController,
                    readOnly: true,
                    decoration: InputDecoration(
                      labelText: '开始日期',
                      suffixIcon: IconButton(
                        icon: const Icon(Icons.calendar_today),
                        onPressed: () => _pickDate(_startDateController),
                      ),
                    ),
                  ),
                  const SizedBox(height: 16),
                  TextFormField(
                    controller: _endDateController,
                    readOnly: true,
                    decoration: InputDecoration(
                      labelText: '结束日期',
                      suffixIcon: IconButton(
                        icon: const Icon(Icons.calendar_today),
                        onPressed: () => _pickDate(_endDateController),
                      ),
                    ),
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
