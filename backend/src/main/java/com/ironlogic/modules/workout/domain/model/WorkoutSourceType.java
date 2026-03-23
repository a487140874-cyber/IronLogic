package com.ironlogic.modules.workout.domain.model;

/**
 * WorkoutSession 的来源类型。
 *
 * <p>模板训练和手动训练在业务语义上不同，因此必须显式记录来源，
 * 避免后续 progression 混淆两种训练。
 */
public enum WorkoutSourceType {
    TEMPLATE,
    MANUAL
}
